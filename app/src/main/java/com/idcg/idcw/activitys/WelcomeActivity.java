package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import java.util.concurrent.TimeUnit;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.utils.IDCWPayJumpUtils;
import foxidcw.android.idcw.foxcommon.Constants.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by hpz on 2018/1/4.
 */
public class WelcomeActivity extends BaseWalletActivity {
    private Disposable disposable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onInitView(Bundle bundle) {
        Intent intent = getIntent();
        // 检测是否需要缓存
        IDCWPayJumpUtils.cacheThirdPartyDataToSpIfHasScheme(intent);
        // 获取跳转类型
        int reqType = IDCWPayJumpUtils.checkNeedJumpWithLogin(this);
        /**
         * 如果不是瑞士会  并且有appid  才进行跳转
         */
//        if (IDCWPayJumpUtils.isNeedBackToThirdParty()) {
//            IDCWPayJumpUtils.clearCacheParam();
//            finish();
//            return;
//        }
//        AppManager.getInstance().finishActivityClass(IDCWWithDrawActivity.class);
//        AppManager.getInstance().finishActivityClass(IDCWPayActivity.class);
        //如果跳转的类型是不是-1
        if (reqType != IDCWPayJumpUtils.JUMP_STATE_NONE) {
            IDCWPayJumpUtils.IDCWPayJumpWithType(this, reqType, MainActivity.isOpen);
            finish();
            return;
        }
        LoginStatus loginStatus = LoginUtils.getLoginBean(WelcomeActivity.this);
        // 防止app多次启动
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        disposable = Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (loginStatus != null) {
                        ACacheUtil.get(this).getAsBoolean(loginStatus.getDevice_id() + AcacheKeys.SAVEISVAILDPIN, false);
                        if (!TextUtils.isEmpty(loginStatus.getTicket()) && ACacheUtil.get(this).getAsBoolean(loginStatus.getDevice_id() + AcacheKeys.SAVEISVAILDPIN, false)) {
                            /**
                             * 如果已保存登录态并且已设置pin就跳转到pin验证页面
                             **/
                            ARouter.getInstance().build(Constants.PIN_START).navigation(mCtx);
                            this.finish();//每个判断走了之后同时干掉当前页面
                        } else if (!TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 2) {
                            /**
                             * 如果恢复资产之后在重置pin页面退出之后下次进来自动回到pin重置页面
                             **/
                            ARouter.getInstance().build(ArouterConstants.SETWALLETPIN).navigation(mCtx);
                            this.finish();//每个判断走了之后同时干掉当前页面
                        } else if (!TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 3 && ACacheUtil.get(WelcomeActivity.this).getAsString("old") != null
                                && ACacheUtil.get(WelcomeActivity.this).getAsString("old").equals("old")) {
                            /**
                             * 如果从旧版本入口登录的，到了备份短语那一步如果干掉页面，下次进来就直接回到pin设置
                             **/
                            ARouter.getInstance().build(ArouterConstants.SETWALLETPIN).navigation(mCtx);
                            this.finish();//每个判断走了之后同时干掉当前页面
                        } else if (!TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6 && ACacheUtil.get(WelcomeActivity.this).getAsString("namePhrase") != null
                                && ACacheUtil.get(WelcomeActivity.this).getAsString("namePhrase").equals("namePhrase")) {
                            /**
                             * 正常都创建钱包流程，到了备份短语那一步，如果干掉页面，下次进来直接进入到设置pin页面
                             **/
                            Bundle bundleString = new Bundle();
                            bundleString.putString("finish", "finish");
                            ARouter.getInstance()
                                    .build(ArouterConstants.SETWALLETPIN)
                                    .withBundle("backFinish", bundleString)
                                    .navigation();
                            this.finish();//每个判断走了之后同时干掉当前页面
                        } else if (!TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6) {
                            /**
                             * 如果用户创建了钱包名，突然杀死程序，重新进来会回到记住助记词页面
                             **/
                            ARouter.getInstance().build(ArouterConstants.REMPHRASE).navigation(mCtx);
                            this.finish();//每个判断走了之后同时干掉当前页面
                        }
                    } else {
                        ARouter.getInstance().build(ArouterConstants.MAINGUIDE).navigation(mCtx);
                        finish();
                    }
                });
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void onEvent() {
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
