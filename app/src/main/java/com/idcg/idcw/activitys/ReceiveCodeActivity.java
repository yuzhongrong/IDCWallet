package com.idcg.idcw.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import com.idcg.idcw.iprovider.AccountAddressInfoProviderServices;
import com.idcg.idcw.iprovider.MoneyBagListProviderServices;

import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.utils.ZXingUtils;
import com.bumptech.glide.Glide;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2018/1/22.
 */

public class ReceiveCodeActivity extends BaseWalletActivity implements View.OnClickListener {
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.btn_copy_address)
    LinearLayout btnCopyAddress;
    @BindView(R.id.btn_save_address_photo)
    LinearLayout btnSaveAddressPhoto;
    @BindView(R.id.img_receive)
    ImageView imgReceive;
    @BindView(R.id.tv_receive_currency)
    TextView tvReceiveCurrency;

    private String currency;
    private Dialog dialog;
    private View inflater;
    private TextView tv_sure;
    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片
    private String coinFive;
    private String coinThree;
    private Bitmap res;
    private String logoUrl;
    private TextView tv_activity_update;
    private List<WalletAssetBean> mDataBeansList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    //ToastUtils.showToast(ReceiveCodeActivity.this, "开始保存图片...");
                    break;
                case SAVE_SUCCESS:
                    ToastUtil.show(getString(R.string.save_success));
                    break;
                case SAVE_FAILURE:
                    //ToastUtils.showToast(ReceiveCodeActivity.this, "图片保存失败,请稍后再试...");
                    break;
            }
        }
    };

    @Autowired
    AccountAddressInfoProviderServices accountAddressInfoProviderServices;
    @Autowired
    MoneyBagListProviderServices moneyBagListProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_receive_page;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initSavePhotoDialog();//初始化开启相机权限的弹框

        initSkipBundleData();//获取上个页面携带的数据

    }

    private void initSavePhotoDialog() {
        try {
            dialog = new Dialog(this, R.style.shuweiDialog);
            inflater = LayoutInflater.from(this).inflate(R.layout.activity_save_to_dialog, null);
            dialog.setContentView(inflater);
            dialog.setCancelable(false);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_activity_update = (TextView) inflater.findViewById(R.id.tv_activity_update);
            tv_sure = (TextView) inflater.findViewById(R.id.tv_activity_cancel);
            tv_sure.setOnClickListener(this);
            tv_activity_update.setOnClickListener(this);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void initSkipBundleData() {
        Intent intent = getIntent();
        Bundle bundle1 = intent.getBundleExtra("receive");
        currency = bundle1.getString("currency");
        logoUrl = bundle1.getString("logoUrl");
        coinFive = bundle1.getString("currencyFive");
        coinThree = bundle1.getString("currencyThree");
        tvReceiveCurrency.setText(String.format(getString(R.string.main_tx_two),currency.toUpperCase()));
        tvSetName.setText(currency.toUpperCase() + " " + getString(R.string.address));
        Glide.with(ReceiveCodeActivity.this).load(logoUrl).into(imgReceive);
    }

    @Override
    protected void onEvent() {
        getWalletList();
        if (TextUtils.isEmpty(coinFive) && TextUtils.isEmpty(coinThree)) {
            RequestAccountAddress(currency);
        } else {
            if (!TextUtils.isEmpty(coinFive)) {
                RequestAccountAddress(coinFive);
            } else if (!TextUtils.isEmpty(coinThree)) {
                RequestAccountAddress(coinThree);
            }
        }
    }


    @Override
    protected BaseView getView() {
        return null;
    }


    public void getWalletList() {
        moneyBagListProviderServices.getWalletListProvider()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<List<WalletAssetBean>>(this) {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                if (ex.getErrorCode().equals("0")) {
                    ToastUtil.show(getString(R.string.user_or_pass_error));
                } else {
                    ToastUtil.show(getString(R.string.server_connection_error));

                }
            }

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onSuccess(List<WalletAssetBean> walletAssetBeans) {
                mDataBeansList.addAll(walletAssetBeans);
                for (int i = 0; i < mDataBeansList.size(); i++) {
                    if (currency.equals(mDataBeansList.get(i).getCurrency())) {
                        tvSetName.setText(mDataBeansList.get(i).getCurrency().toUpperCase() + " " + getString(R.string.address));
                        tvReceiveCurrency.setText(String.format(getString(R.string.main_tx_two),mDataBeansList.get(i).getCurrency().toUpperCase()));
                    }
                }
            }
        });
    }

    private void RequestAccountAddress(String type) {
        accountAddressInfoProviderServices.requestAccountAddressProvider(type)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(
                new RxProgressSubscriber<String>(this) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }

                    @Override
                    public void onSuccess(String messageInfo) {
                        tvAddress.setText(messageInfo);
                        if (messageInfo != null) {
                            res = ZXingUtils.createImage(messageInfo, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo));
                            imgCode.setImageBitmap(res);
                        }
                    }
                }
        );
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_copy_address, R.id.btn_save_address_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                EventBus.getDefault().post(new PosInfo(2020));
                this.finish();
                break;
            case R.id.btn_copy_address:
                if (TextUtils.isEmpty(tvAddress.getText().toString().trim())) return;
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, tvAddress.getText().toString().trim());
                clipboard.setPrimaryClip(clipData);
                ToastUtil.show(getString(R.string.dialog_receive));
                break;
            case R.id.btn_save_address_photo:
                new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            LogUtil.i("--------granted------>", granted + "");
                            if (granted) {
                                if (res != null) saveImageToPhotos(ReceiveCodeActivity.this, res);

                            } else {
                                //Toast.makeText(ReceiveCodeActivity.this, "无权限访问", Toast.LENGTH_SHORT).show();
                                dialog.show();
                            }
                        });
                break;
        }
    }

    private void saveImageToPhotos(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "IDCW");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            return;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
    }

    /**
     * 打开设置页面
     */
    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new PosInfo(2020));
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_activity_cancel:
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                break;
            case R.id.tv_activity_update:
                openAppSetting();
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                break;
        }
    }

    @Override
    protected void checkAppVersion() {

    }
}
