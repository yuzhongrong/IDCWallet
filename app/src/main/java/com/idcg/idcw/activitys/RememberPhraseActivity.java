package com.idcg.idcw.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.PhraseListBean;
import com.idcg.idcw.model.bean.VerfifyListBean;
import com.idcg.idcw.model.logic.BackPhraseLogic;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.presenter.impl.BackPhrasePresenterImpl;
import com.idcg.idcw.presenter.interfaces.BackPhraseContract;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hpz on 2018/3/30.
 */

@Route(path = ArouterConstants.REMPHRASE, name = "记住12个助记词")
public class RememberPhraseActivity extends BaseWalletActivity<BackPhraseLogic, BackPhrasePresenterImpl>
        implements BackPhraseContract.View {
    @BindView(R.id.rv_phrase)
    RecyclerView rvPhrase;
    @BindView(R.id.btn_now_back)
    TextView btnNowBack;
    @BindView(R.id.btn_later_say)
    TextView btnLaterSay;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;

    private List<PhraseDataReqAndResParam.VerinfyWordBean> verinfyList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.RandomWordBean> phraseAllList = new ArrayList<>();
    private BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean,BaseViewHolder> phraseCommonAdapter;
    private static final int MAX_COUNT_TIME = 6;
    private Bundle bundle = new Bundle();

    @Autowired(name = "mIsPhrase")
    boolean mIsPhrase = false;
    private String walletName;
    private LoginStatus loginStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rem_phrase;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initRecycler();
        initBtnCountDown();//进来下次再说按钮默认倒计时5s后才能点击

        loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("wallet");
        if (bundle != null) {
            walletName = bundle.getString("walletName");
        }

        if (mIsPhrase) {
            btnLaterSay.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(walletName) && walletName.equals(walletName)) {
            mrBackLayout.setVisibility(View.VISIBLE);
        } else if (loginStatus != null && !TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6 && mIsPhrase) {
            mrBackLayout.setVisibility(View.VISIBLE);
        } else if (loginStatus != null && !TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6) {
            mrBackLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < 12; i++) {//初始化默认设置12个空白的助记词位置
            PhraseDataReqAndResParam.RandomWordBean confirmBean = new PhraseDataReqAndResParam.RandomWordBean();
            confirmBean.setPhrase("");
            confirmBean.setSerial_number(-1);
            phraseAllList.add(confirmBean);
        }
    }

    private void initRecycler() {
        rvPhrase.setLayoutManager(new GridLayoutManager(this, 3));
        phraseCommonAdapter = new BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean, BaseViewHolder>(R.layout.item_confirm_phrase, phraseAllList) {
            @Override
            protected void convert(BaseViewHolder helper, PhraseDataReqAndResParam.RandomWordBean item) {
                if (item == null) return;
                TextView textView = helper.getView(R.id.text_confirm);
                textView.setEnabled(false);
                helper.setText(R.id.text_confirm, item.getPhrase());
            }
        };
        rvPhrase.setAdapter(phraseCommonAdapter);
    }

    private void initBtnCountDown() {
        addSubscribe(Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                .take(MAX_COUNT_TIME)
                .map(aLong -> MAX_COUNT_TIME - (aLong + 1))
                .doOnSubscribe(disposable -> btnLaterSay.setEnabled(false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    //倒计时有几秒显示几秒
                    btnLaterSay.setText(String.valueOf(getString(R.string.tv_next_say) + "(" + aLong + "s" + ")"));
                    //再去做0秒的时候的处理
                    if (aLong == 0) {
                        btnLaterSay.postDelayed(()-> {
                                if (btnLaterSay != null) {
                                    btnLaterSay.setEnabled(true);
                                    btnLaterSay.setTextColor(getResources().getColor(R.color.tipper_blue_color));
                                    btnLaterSay.setText(getString(R.string.tv_next_say));
                                }
                        }, 1000);
                    }
                }));
    }

    @Override
    protected void onEvent() {
        showDialog();
        mPresenter.requestPhraseData(String.valueOf(0));//随机获取12条助记词
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @OnClick({R.id.btn_now_back, R.id.btn_later_say, R.id.mr_back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_now_back:
                if (mIsPhrase) {
                    bundle.putSerializable("dataList", new PhraseListBean(phraseAllList));
                    bundle.putSerializable("verifyList", new VerfifyListBean(verinfyList));
                    ARouter.getInstance()
                            .build(ArouterConstants.PHRASE_CONFIRM)
                            .withBundle("number", bundle)
                            .navigation();
                    return;
                }
                bundle.putSerializable("dataList", new PhraseListBean(phraseAllList));
                bundle.putSerializable("verifyList", new VerfifyListBean(verinfyList));
                ARouter.getInstance()
                        .build(ArouterConstants.BACKPHRASE)
                        .withBundle("number", bundle)
                        .navigation();
                break;
            case R.id.btn_later_say:
                if (mIsPhrase) {
                    this.finish();
                    return;
                }
                bundle.putString("later", "later");
                ARouter.getInstance()
                        .build(ArouterConstants.SETWALLETPIN)
                        .withBundle("laterSay", bundle)
                        .navigation();
                break;
            case R.id.mr_back_layout:
                this.finish();
                break;
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
    }

    @Override
    public void updateRequestPhraseData(PhraseDataReqAndResParam phraseDataReqAndResBean) {
        dismissDialog();
        if (phraseAllList != null) {
            phraseAllList.clear();
        }
        phraseAllList.addAll(phraseDataReqAndResBean.getRandomWord());
        verinfyList.addAll(phraseDataReqAndResBean.getVerinfyWord());
        phraseCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateRequestSavePhrase(Object obj) {

    }

    @Subscriber
    public void onRemInfo(PosInfo posInfo) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(walletName) && walletName.equals(walletName)) {

            } else if (loginStatus != null && !TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6 && mIsPhrase) {

            } else if (loginStatus != null && !TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6) {
                moveTaskToBack(true);
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
