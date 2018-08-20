package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.api.NetWorkApi;
import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.concurrent.TimeUnit;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hpz on 2018/4/21.
 */

@Route(path = OTCConstant.APPLYACCEPTREAD, name = "承兑商申请阅读规则")
public class OTCAcceptanceReadActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private TextView mTvBalanceRules;
    private TextView mTvMainContent;
    private TextView mBtnAcceptApplyRead;
    private static final int MAX_COUNT_TIME = 5;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_acceptance_read;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        EventBus.getDefault().register(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mTvMainContent = (TextView) findViewById(R.id.tv_apply_main_content);
        mTvBalanceRules = (TextView) findViewById(R.id.tv_balance_rules);
        mBtnAcceptApplyRead = (TextView) findViewById(R.id.btn_accept_apply_read);
        mMrBackLayout.setOnClickListener(this);
        mTvBalanceRules.setOnClickListener(this);
        mBtnAcceptApplyRead.setOnClickListener(this);
        mTvSetName.setText(getString(R.string.str_otc_apply_assurer));

        //mTvMainContent.setText(String.format(getString(R.string.str_otc_accept_content), "0.3%", "1%"));
        initBtnCountDown();//进来下次再说按钮默认倒计时5s后才能点击
    }

    private void initBtnCountDown() {
        addSubscribe(Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                .take(MAX_COUNT_TIME)
                .map(aLong -> MAX_COUNT_TIME - (aLong + 1))
                .doOnSubscribe(disposable -> mBtnAcceptApplyRead.setEnabled(false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    //倒计时有几秒显示几秒
                    mBtnAcceptApplyRead.setText(String.valueOf(getString(R.string.str_otc_accept_already_read) + "(" + aLong + "s" + ")"));
                    //再去做0秒的时候的处理
                    if (aLong == 0) {
                        mBtnAcceptApplyRead.postDelayed(() -> {
                            if (mBtnAcceptApplyRead != null) {
                                mBtnAcceptApplyRead.setEnabled(true);
                                mBtnAcceptApplyRead.setBackground(getResources().getDrawable(R.drawable.sw_ripple_btn_bg));
                                mBtnAcceptApplyRead.setText(getString(R.string.str_otc_accept_already_read));
                                mBtnAcceptApplyRead.setTextColor(getResources().getColor(R.color.white));
                            }
                        }, 1000);
                    }
                }));
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            this.finish();
        } else if (i == R.id.tv_balance_rules) {//点击保证金规则跳转到h5页面，根据app内设置的语言
            String url = NetWorkApi.TRANSACTION_ACCEPT_RULE_URL;
            switch (AppLanguageUtils.getLanguageLocalCode(mCtx)) {
                case "0":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=en")
                            .navigation(mCtx);
                    break;
                case "1":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=zh-CN")
                            .navigation(mCtx);
                    break;
                case "2":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=ja")
                            .navigation(mCtx);
                    break;
                case "3":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=nl")
                            .navigation(mCtx);
                    break;
                case "4":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=ko")
                            .navigation(mCtx);
                    break;
                case "5":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=fr")
                            .navigation(mCtx);
                    break;
                case "6":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=vi")
                            .navigation(mCtx);
                    break;
                case "7":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=es")
                            .navigation(mCtx);
                    break;
                case "8":
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", url + "?lang=hk")
                            .navigation(mCtx);
                    break;
            }

        } else if (i == R.id.btn_accept_apply_read) {
            navigation(OTCConstant.ADDCURRENCY);
        } else {
        }
    }

    @Subscriber
    public void refreshBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 166) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
