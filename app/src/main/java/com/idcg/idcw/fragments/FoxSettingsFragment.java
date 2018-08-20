package com.idcg.idcw.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.idcg.idcw.iprovider.GetWalletBalanceProviderServices;
import com.idcg.idcw.model.bean.ShareConfigBean;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LogUtil;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.ExitAppProviderServices;
import com.idcg.idcw.widget.dialog.BindPhonePopWindow;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.iprovider.CheckVersionProviderServices;
import foxidcw.android.idcw.common.model.bean.CheckAppVersionBean;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.otc.activitys.pay.OTCPayMethodManagerActivity;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCurrentStepBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceListBean;

/**
 * Created by admin-2 on 2018/3/14.
 */

public class FoxSettingsFragment extends BaseWalletFragment implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ll_local_curr)
    RelativeLayout llLocalCurr;
    @BindView(R.id.ll_language)
    RelativeLayout llLanguage;
    @BindView(R.id.ll_contract)
    RelativeLayout llContract;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_user_call_back)
    RelativeLayout llUserCallBack;
    @BindView(R.id.ll_power_indoction)
    RelativeLayout llPowerIndoction;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.btn_check_new_version)
    TextView btnCheckNewVersion;
    @BindView(R.id.ll_pay_manager)
    RelativeLayout llPayManager;
    @BindView(R.id.ll_assurer_manager)
    RelativeLayout llAssurerManager;
    Unbinder unbinder;
    @BindView(R.id.tv_accept_status)
    TextView tvAcceptStatus;
    @BindView(R.id.ll_share)
    RelativeLayout llShare;
    @BindView(R.id.tv_share)
    TextView tv_share;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.img_share)
    ImageView img_share;


    private Dialog dialogOut;
    private View inflater;
    private TextView tv_login_out;
    private TextView tv_out_cancel;
    private Dialog dialog;
    private TextView tv_activity_title, tv_activity_update, tv_activity_cancel;
    private String updateUrl;
    private String version;

    private static final int PAY_METHOD_MANAGER_REQUEST = 105;

    // 是否设置支付方式
    @BindView(R.id.payment_method_state_tv)
    TextView mPaymentMethodStateTv;

    @Autowired
    CheckVersionProviderServices mCheckVersionServices;

    @Autowired
    OTCMoneyBagListProviderServices mWalletListServices;

    @Autowired
    GetWalletBalanceProviderServices getWalletBalanceProviderServices;

    @Autowired
    ExitAppProviderServices mLogOutServices;
    private double local;
    private double server;
    private int currentStep;
    private int Status;
    private boolean isShowUpdate;
    private String shareUrl;

    public static FoxSettingsFragment newInstance() {
        Bundle args = new Bundle();
        FoxSettingsFragment fragment = new FoxSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fox_setting;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onInitView(Bundle bundle) {
        EventBus.getDefault().register(this);
        ARouter.getInstance().inject(this);
        tvSetName.setText(getString(R.string.str_me));
        try {
            //tvVersion.setText(String.valueOf(getString(R.string.tv_version) + " " + Utils.getVersionName(getActivity())));
            tvVersion.setText(getString(R.string.tv_version) + " "+"3.2.2");//因为3.2.1.1版本的原因，导致这里要写死版本号
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgBack.setVisibility(View.GONE);
        initExitDialog();
        initCheckVersionDialog();
    }

    private void initCheckVersionDialog() {
        try {
            dialog = new Dialog(getActivity(), R.style.shuweiDialog);
            inflater = LayoutInflater.from(getActivity()).inflate(R.layout.activity_check_version_dialog, null);
            dialog.setContentView(inflater);
            dialog.setCancelable(false);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_activity_title = inflater.findViewById(R.id.tv_activity_title);
            tv_activity_update = inflater.findViewById(R.id.tv_activity_update);
            tv_activity_cancel = inflater.findViewById(R.id.tv_activity_cancel);
            tv_activity_update.setOnClickListener(this);
            tv_activity_cancel.setOnClickListener(this);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void initExitDialog() {
        try {
            dialogOut = new Dialog(getActivity(), R.style.shuweiDialog);
            inflater = LayoutInflater.from(getActivity()).inflate(R.layout.act_login_out_dialog, null);
            dialogOut.setContentView(inflater);
            dialogOut.setCancelable(false);
            Window dialogWindow = dialogOut.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_login_out = (TextView) inflater.findViewById(R.id.out_sure);
            tv_out_cancel = (TextView) inflater.findViewById(R.id.out_cancel);
            tv_login_out.setOnClickListener(this);
            tv_out_cancel.setOnClickListener(this);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onEvent() {
        requestAcceptStatus(true);//获取承兑商的状态
        requestShareConfigStatus();//获取邀请链的配置信息
    }

    private void requestShareConfigStatus() {
        getWalletBalanceProviderServices.requestShareConfigProvider(AppLanguageUtils.getLanguageLocalCode(mContext),"0")
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<ShareConfigBean>(this) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }

                    @Override
                    public void onSuccess(ShareConfigBean shareConfigBean) {
                        LogUtil.e("ShareConfigBean==",new Gson().toJson(shareConfigBean));
                        if (shareConfigBean.isIs_show()){
                            llShare.setVisibility(View.VISIBLE);
                        }else {
                            llShare.setVisibility(View.GONE);
                        }
                        shareUrl = shareConfigBean.getLink();
                        if (!TextUtils.isEmpty(shareConfigBean.getLable_text())){
                            tv_share.setText(shareConfigBean.getLable_text());
                        }

                        if (!TextUtils.isEmpty(shareConfigBean.getIcon())){
                            //GlideUtil.loadImageView(mContext, shareConfigBean.getIcon(), img_share);
                            GlideUtil.loadImageViewLoding(mContext, shareConfigBean.getIcon(), img_share, R.mipmap.icon_setting_invite, R.mipmap.icon_setting_invite);
                        }

                        if (!TextUtils.isEmpty(shareConfigBean.getLabel_text2())){
                            tv_status.setText(shareConfigBean.getLabel_text2());
                        }else {
                            tv_status.setText("");
                        }
                    }

                });
    }

    private void requestAcceptCurrentStepStatus(boolean isShowProgress) {
        mWalletListServices.requestGetCurrentStepProvider()
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<OTCCurrentStepBean>(this, isShowProgress) {
                    @Override
                    public void onSuccess(OTCCurrentStepBean bean) {
                        currentStep = bean.getCurrentStep();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void requestAcceptStatus(boolean isShowProgress) {
        mWalletListServices.requestDepositBalanceList()
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<OTCDepositBalanceListBean>(this, isShowProgress) {
                    @Override
                    public void onSuccess(OTCDepositBalanceListBean bean) {
                        currentStep = bean.getCurrentStep();
                        Status = bean.getStatus();
                        switch (bean.getStatus()) {
                            case 1:
                                tvAcceptStatus.setText(getString(R.string.str_otc_go_open));
                                break;
                            case 2:
                                tvAcceptStatus.setText(getString(R.string.str_otc_wait_check));
                                break;
                            case 3:
                                tvAcceptStatus.setText("");
                                break;
                            case 4:
                                tvAcceptStatus.setText(getString(R.string.str_otc_wait_parse));
                                break;
                        }
                        mPaymentMethodStateTv.setText(bean.isHasPayment() ? "" : getResources().getString(R.string.str_otc_go_setting));
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (ex.getErrorCode().equals("627")) {
                            tvAcceptStatus.setText(getString(R.string.str_otc_go_open));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }
                });
    }

    @OnClick({
            R.id.ll_account_security,
            R.id.ll_local_curr,
            R.id.ll_language,
            R.id.ll_contract,
            R.id.ll_share,
            R.id.ll_user_call_back,
            R.id.ll_power_indoction,
            R.id.ll_version,
            R.id.btn_check_new_version,
            R.id.ll_pay_manager,
            R.id.ll_assurer_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_account_security:
                navigation(ArouterConstants.ACCOUNT_AND_SECURITY);
                break;
            case R.id.ll_share:
                if (TextUtils.isEmpty(shareUrl))return;
                ARouter.getInstance().build(ArouterConstants.SHARE_WEB_VIEW)
                        .withString("shareUrl", shareUrl)
                        .navigation(mContext);
                //navigation(ArouterConstants.SHARE_WEB_VIEW);
                break;
            case R.id.ll_local_curr:
                navigation(ArouterConstants.LOCAL_CURRENCY);
                break;
            case R.id.ll_language:
                navigation(ArouterConstants.LANGUAGE_AC);
                break;
            case R.id.ll_contract:
                navigation(ArouterConstants.CONTACT);
                break;
            case R.id.ll_user_call_back:
                navigation(ArouterConstants.USER_BACK);
                break;
            case R.id.ll_power_indoction:
                navigation(ArouterConstants.INTRODUCE);
                break;
            case R.id.ll_version:
                break;
            case R.id.btn_check_new_version:
                showDialog();
                addSubscribe(mCheckVersionServices.CheckVersionProvider().subscribeWith(new RxSubscriber<CheckAppVersionBean>() {
                    @Override
                    public void onSuccess(CheckAppVersionBean checkAppVersionBean) {
                        dismissDialog();
                        version = checkAppVersionBean.getLatestVersion();
                        isShowUpdate = checkAppVersionBean.isIsShowUpdate();
                        if (StringUtils.getVersionName(getActivity()).contains(".")) {
                            String str = StringUtils.getVersionName(getActivity()).replace(".", "");
                            StringBuilder stringBuilder = new StringBuilder(str);
                            String localStr = stringBuilder.insert(1, ".").toString();
                            local = Double.parseDouble(localStr);
                        }

                        if (checkAppVersionBean.getLatestVersion().contains(".")) {
                            String str = checkAppVersionBean.getLatestVersion().replace(".", "");
                            StringBuilder stringBuilder = new StringBuilder(str);
                            String serverStr = stringBuilder.insert(1, ".").toString();
                            server = Double.parseDouble(serverStr);
                        }

                        if (local < server && checkAppVersionBean.isIsShowUpdate()) {
                            dialog.show();
                            tv_activity_title.setText(String.valueOf(getString(R.string.check_version_title) + version));
                            updateUrl = checkAppVersionBean.getUpdateUrl();
                            if (checkAppVersionBean.isIs_enabled()) {
                                tv_activity_cancel.setVisibility(View.GONE);
                            } else {
                                tv_activity_cancel.setVisibility(View.VISIBLE);
                            }

                        }
                        if (local >= server||!checkAppVersionBean.isIsShowUpdate()) {
                            dialog.show();
                            tv_activity_title.setText(getString(R.string.already_new_version));
                            tv_activity_update.setText(getString(R.string.tv_sure));
                            tv_activity_cancel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }
                }));
                break;
            case R.id.ll_pay_manager:
                Intent intent = new Intent(mContext, OTCPayMethodManagerActivity.class);
                startActivityForResult(intent, PAY_METHOD_MANAGER_REQUEST);
                break;
            case R.id.ll_assurer_manager:
                BindPhonePopWindow phonePopWindow = new BindPhonePopWindow(mContext);
                if (ACacheUtil.get(mContext).getAsBoolean(AcacheKeys.ISBINDPHONE, false)) {
                    if (Status == 1) {
                        if (currentStep == 1) {
                            navigation(OTCConstant.APPLYACCEPT);
                        } else if (currentStep == 2) {
                            navigation(OTCConstant.APPLYSELLCURR);
                        } else if (currentStep == 3) {
                            navigation(OTCConstant.RECHARGEDEPOSIT);
                        }

                    } else if (Status == 2) {
                        navigation(OTCConstant.DEPOSITBALANCE);
                    } else if (Status == 3) {
                        navigation(OTCConstant.DEPOSITBALANCE);
                    } else if (Status == 4) {
                        navigation(OTCConstant.DEPOSITBALANCE);
                    }
                } else {
                    phonePopWindow.showPopupWindow();
                    phonePopWindow.getSkipBindPhone().setOnClickListener(v -> {
                        navigation(ArouterConstants.ACTIVITY_PHONE);
                        phonePopWindow.dismiss();
                    });
                }
                break;
        }
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out_sure:
                showDialog();
                addSubscribe(mLogOutServices.requestLoginOutProvider().subscribeWith(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dismissDialog();
                        dialogOut.dismiss();
                        LoginStatus loginStatus = LoginUtils.getLoginBean(mContext);
                        ACacheUtil.get(mContext).remove(loginStatus.getId() + AcacheKeys.cache1);
                        ACacheUtil.get(mContext).remove(AcacheKeys.loginbean);
                        AppManager.getInstance().finishActivity();
                        navigation(ArouterConstants.GUIDE);
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }
                }));
                break;
            case R.id.out_cancel:
                dialogOut.dismiss();
                break;
            case R.id.tv_activity_update:
                if (local >= server||!isShowUpdate) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else if (!StringUtils.getVersionName(getActivity()).equals(version) && !TextUtils.isEmpty(updateUrl)) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(updateUrl);
                    intent.setData(content_url);
                    if (dialog != null && dialog.isShowing()) dialog.dismiss();
                    startActivity(intent);
                }
                break;
            case R.id.tv_activity_cancel:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.dialog = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Subscriber
    public void refreshBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 166) {
            requestAcceptStatus(true);
        }
    }

    @Subscriber
    public void refreshStatusList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 603) {
            requestAcceptStatus(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAY_METHOD_MANAGER_REQUEST) {
            int size = 0;
            if (data != null) {
                size = data.getIntExtra("size", 0);
            }
            mPaymentMethodStateTv.setText(size > 0 ? "" : getResources().getString(R.string.str_otc_go_setting));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
