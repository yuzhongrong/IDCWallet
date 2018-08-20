package com.idcg.idcw.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.EventBean;
import com.idcg.idcw.model.bean.PhraseListBean;
import com.idcg.idcw.model.bean.VerfifyListBean;
import com.idcg.idcw.model.logic.BackPhraseLogic;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.presenter.impl.BackPhrasePresenterImpl;
import com.idcg.idcw.presenter.interfaces.BackPhraseContract;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * Created by hpz on 2018/3/30.
 */

@Route(path = ArouterConstants.BACKPHRASE, name = "验证12个助记词")
public class BackPhraseActivity extends BaseWalletActivity<BackPhraseLogic, BackPhrasePresenterImpl>
        implements View.OnClickListener, BackPhraseContract.View {
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.rv_check)
    RecyclerView rvCheck;
    @BindView(R.id.rv_phrase)
    RecyclerView rvPhrase;
    @BindView(R.id.btn_phrase_verify)
    TextView btnPhraseVerify;

    private BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean,BaseViewHolder> checkCommonAdapter;
    private List<PhraseDataReqAndResParam.RandomWordBean> phraseList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.RandomWordBean> phraseCommitList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.RandomWordBean> phraseAllList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.VerinfyWordBean> verifyList = new ArrayList<>();
    private BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean,BaseViewHolder> phraseCommonAdapter;
    private TextView tvPhrase;
    private Dialog dialog;
    private TextView tv_sure;
    private String phraseTag;
    private Bundle bundle = new Bundle();
    private String namePhrase;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_new_phrase;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        btnPhraseVerify.setEnabled(false);
        getPhraseBundleData();//获取上个页面带过来的12个助记词
        initSelectErrorDialog();//选择顺序错误的弹框提示
        initRecycler();
    }

    private void getPhraseBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("number");
        if (bundle != null) {
            PhraseListBean bean = (PhraseListBean) bundle.getSerializable("dataList");
            phraseAllList.addAll(bean.getPhrase());
            Collections.shuffle(phraseAllList);
            phraseTag = bundle.getString("phraseTag");//从旧用户入口进来的备份标志
        }

        if (bundle != null) {
            VerfifyListBean beanVerify = (VerfifyListBean) bundle.getSerializable("verifyList");
            verifyList.addAll(beanVerify.getPhrase());
        }

        for (int i = 0; i < phraseAllList.size(); i++) {//初始化默认设置12个空白的助记词位置
            PhraseDataReqAndResParam.RandomWordBean confirmBean = new PhraseDataReqAndResParam.RandomWordBean();
            confirmBean.setPhrase("");
            confirmBean.setSerial_number(-1);
            phraseList.add(confirmBean);
        }
    }

    private void initSelectErrorDialog() {
        dialog = new Dialog(this, R.style.shuweiDialog);
        View inflater = LayoutInflater.from(this).inflate(R.layout.re_select_dialog, null);
        dialog.setContentView(inflater);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setAttributes(lp);
        tv_sure = (TextView) inflater.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(this);
    }


    private int getFirstEmptyData() {
        int returnPosition = -1;
        for (int i = 0, size = phraseList.size(); i < size; i++) {
            PhraseDataReqAndResParam.RandomWordBean data = phraseList.get(i);
            if (TextUtils.isEmpty(data.getPhrase()) && data.getSerial_number() == -1) {
                returnPosition = i;
                break;
            }
        }
        return returnPosition;
    }


    private void initRecycler() {
        rvPhrase.setLayoutManager(new GridLayoutManager(this, 3));
        phraseCommonAdapter = new BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean, BaseViewHolder>(R.layout.item_confirm_new_phrase, phraseAllList) {
            @Override
            protected void convert(BaseViewHolder helper, PhraseDataReqAndResParam.RandomWordBean item) {
                helper.setText(R.id.text_confirm, item.getPhrase());
                tvPhrase = helper.getView(R.id.text_confirm);
                if (item.isSelect()) {
                    tvPhrase.setBackground(getResources().getDrawable(R.drawable.phrase_item_gray_dw));
                } else {
                    tvPhrase.setBackground(getResources().getDrawable(R.drawable.phrase_item_blue_dw));
                }
                if (TextUtils.isEmpty(phraseList.get(helper.getAdapterPosition()).getPhrase())) {
                    btnPhraseVerify.setEnabled(false);
                    btnPhraseVerify.setBackground(getResources().getDrawable(R.drawable.item_gray_black));
                    btnPhraseVerify.setTextColor(getResources().getColor(R.color.gray_90));
                }
            }
        };
        rvPhrase.setAdapter(phraseCommonAdapter);
        rvPhrase.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == -1) return;
                PhraseDataReqAndResParam.RandomWordBean bean = phraseCommonAdapter.getItem(position);
                if (bean.isSelect()) {
                    return;
                }
                bean.setSelect(true);
                phraseCommonAdapter.notifyDataSetChanged();
                int emptyPosition = getFirstEmptyData();
                PhraseDataReqAndResParam.RandomWordBean emptyData = phraseList.get(emptyPosition);
                emptyData.setSerial_number(position + 1);
                emptyData.setPhrase(bean.getPhrase());
                checkCommonAdapter.notifyDataSetChanged();
                LogUtil.d("position==select:",position+"");
            }
        });

        rvCheck.setLayoutManager(new GridLayoutManager(this, 3));
        checkCommonAdapter = new BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean, BaseViewHolder>(R.layout.item_check_phrase, phraseList) {
            @Override
            protected void convert(BaseViewHolder helper, PhraseDataReqAndResParam.RandomWordBean item) {
                helper.setText(R.id.text_confirm, item.getPhrase());
                if (!TextUtils.isEmpty(phraseList.get(11).getPhrase())) {
                    btnPhraseVerify.setBackground(getResources().getDrawable(R.drawable.sw_tipper_blue_bg));
                    btnPhraseVerify.setEnabled(true);
                    btnPhraseVerify.setTextColor(getResources().getColor(R.color.white));
                }
            }
        };
        rvCheck.setAdapter(checkCommonAdapter);
        rvCheck.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (TextUtils.isEmpty(phraseList.get(position).getPhrase())) return;
                PhraseDataReqAndResParam.RandomWordBean data = phraseList.get(position);
                PhraseDataReqAndResParam.RandomWordBean wordBean = phraseAllList.get(data.getSerial_number() - 1);
                wordBean.setSelect(false);
                data.setPhrase("");
                data.setSerial_number(-1);
                checkCommonAdapter.notifyDataSetChanged();
                phraseCommonAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_phrase_verify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_phrase_verify:
                for (int i = 0; i < phraseAllList.size(); i++) {
                    PhraseDataReqAndResParam.RandomWordBean phraseInfo = new PhraseDataReqAndResParam.RandomWordBean();
                    phraseInfo.setSerial_number(i + 1);
                    phraseInfo.setPhrase(phraseList.get(i).getPhrase());
                    phraseCommitList.add(phraseInfo);
                }
                LogUtil.e("btn_phrase_verify=", new Gson().toJson(phraseCommitList));
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("number");
                if (intent != null && bundle != null) {
                    PhraseListBean phraseListBean = (PhraseListBean) bundle.getSerializable("dataList");
                    for (int i = 0; i < phraseListBean.getPhrase().size(); i++) {
                        if (phraseListBean.getPhrase().get(i).getPhrase() != phraseList.get(i).getPhrase()) {
                            dialog.show();
                            return;
                        }
                    }
                }
                if (!TextUtils.isEmpty(phraseTag) && phraseTag.equals("phraseTag")) {//旧用户进来的备份短语标志phraseTag
                    RequestSaveUserPhrase();
                    return;
                }
                //顺序正确的请求
                //ToastUtil.show(getString(R.string.str_verify_success));
//                Bundle bundlePhrase = new Bundle();
//                bundlePhrase.putSerializable("phraseAllList", new PhraseListBean(phraseCommitList));
//                bundlePhrase.putSerializable("phraseVerifyList", new VerfifyListBean(verifyList));
//                ARouter.getInstance()
//                        .build(ArouterConstants.SETWALLETPIN)
//                        .withBundle("phrase", bundlePhrase)
//                        .navigation();
                RequestSaveUserPhrase();
                break;
        }
    }

    private void RequestSaveUserPhrase() {
        PhraseDataReqAndResParam reqAndResParam = new PhraseDataReqAndResParam();
        reqAndResParam.setRandomWord(phraseCommitList);
        reqAndResParam.setVerinfyWord(verifyList);
        showDialog();
        mPresenter.requestSavePhrase(reqAndResParam);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                phraseCommitList.clear();
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if (throwable.getErrorCode().equals("0")) {
            dialog.show();
        } else {
            ToastUtil.show(getString(R.string.server_connection_error));
        }
    }

    @Override
    public void updateRequestPhraseData(PhraseDataReqAndResParam phraseDataReqAndResBean) {

    }

    @Override
    public void updateRequestSavePhrase(Object obj) {
        dismissDialog();
        if (!TextUtils.isEmpty(phraseTag) && phraseTag.equals("phraseTag")) {//从旧用户入口进来的，成功回调之后保存的登录态mainPos为3并且保存的标志是old
            phraseCommitList.clear();
            ACacheUtil.get(BackPhraseActivity.this).put("old", "old");
            navigation(ArouterConstants.SETWALLETPIN);
            return;
        }
        ACacheUtil.get(BackPhraseActivity.this).put("namePhrase", "namePhrase");
        phraseCommitList.clear();
        bundle.putString("phraseNew", "phraseNew");
        ARouter.getInstance()
                .build(ArouterConstants.SETWALLETPIN)
                .withBundle("phraseNewPin", bundle)
                .navigation();
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Subscriber
    public void onBackFinishInfo(EventBean posInfo) {
        try {
            if (posInfo == null) return;
            namePhrase = posInfo.getMsg();
            if (posInfo.getMsg().equals("finish")) {
                mrBackLayout.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(namePhrase) && namePhrase.equals("finish")) {
                moveTaskToBack(true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
