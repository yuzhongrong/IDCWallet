package com.idcg.idcw.activitys;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.LoginWalletProviderServices;
import com.idcg.idcw.iprovider.ReqRegisterIproviderServices;
import com.idcg.idcw.model.logic.VerfityNameLogic;
import com.idcg.idcw.model.params.CreateSetPassReqParam;
import com.idcg.idcw.presenter.impl.VerfityNameImpl;
import com.idcg.idcw.presenter.interfaces.CreateWalletContract;
import com.idcg.idcw.utils.SoftHideKeyBoardUtil;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.ui.view.countdownview.Utils;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.Subscriber;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.StringUtils;

/**
 * Created by hpz on 2018/3/30.
 */

@Route(path = ArouterConstants.SETWALLETNAME, name = "设置用户的钱包名")
public class SetWalletNameActivity extends BaseWalletActivity<VerfityNameLogic, VerfityNameImpl> implements TextWatcher,
        CreateWalletContract.View {
    @BindView(R.id.ed_set_wallet_name)
    EditText edSetWalletName;
    @BindView(R.id.btn_set_wallet_next)
    TextView btnSetWalletNext;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.view_set_wallet)
    View viewSetWallet;
    @BindView(R.id.ll_wallet_name_layout)
    LinearLayout llWalletNameLayout;
    @BindView(R.id.ll_wallet_name_head)
    LinearLayout llWalletNameHead;
    @BindView(R.id.ll_warp_wallet_layout)
    LinearLayout llWarpWalletLayout;
    @BindView(R.id.ed_get_wallet_invite)
    EditText edGetWalletInvite;

    private Bundle bundle = new Bundle();

    @Autowired
    LoginWalletProviderServices loginWalletProviderServices;

    @Autowired
    ReqRegisterIproviderServices newRegisterIproviderServices;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_wallet_name;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        edSetWalletName.addTextChangedListener(this);
        btnSetWalletNext.setEnabled(false);//初始化按钮不能点击
        edSetWalletName.setFilters(new InputFilter[]{filter});

        keepWalletBtnNotOver(llWalletNameLayout, llWarpWalletLayout);
    }

    /**
     * 保持按钮始终不会被覆盖
     *
     * @param root
     * @param subView
     */
    private void keepWalletBtnNotOver(final View root, final View subView) {
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();// 屏幕宽（像素，如：480px）
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();// 屏幕高（像素，如：800p） 适配Note8
        LogUtil.e(Build.MODEL + ":screenWidth=" + screenWidth + ";screenHeight=" + screenHeight);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
                if (rootInvisibleHeight > 200 && screenHeight < 2800) {
                    // 显示键盘时
                    int srollHeight = rootInvisibleHeight - (root.getHeight() - subView.getHeight()) - SoftHideKeyBoardUtil.getNavigationBarHeight(root.getContext());
                    if (srollHeight > 0) { //当键盘高度覆盖按钮时
                        root.scrollTo(0, Utils.dp2px(getApplicationContext(), 25));
                    }
                } else {
                    // 隐藏键盘时
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @Override
    protected void onEvent() {
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnSetWalletNext.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnSetWalletNext.setBackgroundResource(R.drawable.item_gray_black);
        btnSetWalletNext.setTextColor(getResources().getColor(R.color.gray_90));
        viewSetWallet.setBackground(getResources().getDrawable(R.color.tian_tip_blue));//设置线的颜色
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            String str1 = "";
            for (int i = 0; i < str.length; i++) {
                str1 += str[i];
            }
            edSetWalletName.setText(str1);
            edSetWalletName.setSelection(start);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!edSetWalletName.getText().toString().equals("") && edSetWalletName.getText().toString().length() >= 4) {
            btnSetWalletNext.setEnabled(true);
            btnSetWalletNext.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnSetWalletNext.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if (throwable.getErrorCode().equals("0")) {//用户名已经存在
            ToastUtil.show(getString(R.string.str_set_wallet_id_exit));
        } else {
            ToastUtil.show(getString(R.string.server_connection_error));
        }
    }

    @Override
    public void updateVerfityName(Object result) {//用户名可以注册的回调
        dismissDialog();
        ToastUtil.show(getString(R.string.str_wallet_id_set_success));
        ACacheUtil.get(mCtx).put(AcacheKeys.SAVEWALLETID, edSetWalletName.getText().toString().trim());//保存钱包id
        navigation(ArouterConstants.REMPHRASE);
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_set_wallet_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_set_wallet_next:
                if (!StringUtils.isUserName(edSetWalletName.getText().toString())) {
                    ToastUtil.show(getString(R.string.str_set_wallet_id_txt));//正则判断id的合法性
                    return;
                }
                //请求校验用户名是否存在的接口
                String locale = Locale.getDefault().getLanguage();
                if ("zh".equals(locale)) {
                    RequestNewVersionRegister("1", "zh-CN");
                } else {
                    RequestNewVersionRegister("0", "en");
                }
                break;
        }
    }

    private void RequestNewVersionRegister(String language, String url) {
        newRegisterIproviderServices.requestRegisterProvider(new CreateSetPassReqParam(edSetWalletName.getText().toString().trim(),
                "USD",
                "0",
                language,
                url,
                "",
                "",
                edGetWalletInvite.getText().toString().trim()
                ))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<LoginStatus>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {
                if (ex.getErrorCode().equals("106")) {
                    ToastUtil.show(getString(R.string.str_set_wallet_id_exit));
                } else if (ex.getErrorCode().equals("102")) {
                    ToastUtil.show(getString(R.string.code_error));
                } else if (ex.getErrorCode().equals("126")) {
                    ToastUtil.show(getString(R.string.str_version_not_support));
                } else {
                    ToastUtil.show(getString(R.string.str_create_error));
                }
            }

            @Override
            public void onSuccess(LoginStatus loginStatus) {//设置wallet ID成功之后保存登陆态posMain为6，这个时候会把状态保存，如果用户杀死程序就会重新回到记住短语的页面
                loginStatus.setPosMain(6);
                ACacheUtil.get(SetWalletNameActivity.this).put(AcacheKeys.loginbean, loginStatus);
                bundle.putString("walletName", "walletName");
                ARouter.getInstance()
                        .build(ArouterConstants.REMPHRASE)
                        .withBundle("wallet", bundle)
                        .navigation(mCtx);
            }
        });
    }

    @Subscriber
    public void onSetWalletInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 602) {
                finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
