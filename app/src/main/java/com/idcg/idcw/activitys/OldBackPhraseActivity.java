package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import com.idcg.idcw.model.bean.PhraseListBean;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.bean.VerfifyListBean;
import com.idcg.idcw.model.logic.BackPhraseLogic;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.presenter.impl.BackPhrasePresenterImpl;
import com.idcg.idcw.presenter.interfaces.BackPhraseContract;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.Subscriber;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * Created by hpz on 2018/3/30.
 */

@Route(path = ArouterConstants.OLDBACKPHRASE, name = "旧用户验证12个助记词")
public class OldBackPhraseActivity extends BaseWalletActivity<BackPhraseLogic, BackPhrasePresenterImpl>
        implements BackPhraseContract.View {
    @BindView(R.id.rv_phrase)
    RecyclerView rvPhrase;
    @BindView(R.id.btn_now_back)
    TextView btnNowBack;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;

    private List<PhraseDataReqAndResParam.VerinfyWordBean> verinfyList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.RandomWordBean> phraseAllList = new ArrayList<>();
    private BaseQuickAdapter<PhraseDataReqAndResParam.RandomWordBean,BaseViewHolder> phraseCommonAdapter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_old_back_phrase;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initRecycler();

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
                helper.setText(R.id.text_confirm, item.getPhrase());
            }
        };
        rvPhrase.setAdapter(phraseCommonAdapter);
    }

    @Override
    protected void onEvent() {
        showDialog();
        mPresenter.requestPhraseData(String.valueOf(0));//随机获取12个短语
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
    }

    @Override
    public void updateRequestPhraseData(PhraseDataReqAndResParam phraseDataReqAndResBean) {
        dismissDialog();
        if (phraseAllList!=null){
            phraseAllList.clear();
        }
        phraseAllList.addAll(phraseDataReqAndResBean.getRandomWord());
        verinfyList.addAll(phraseDataReqAndResBean.getVerinfyWord());
        phraseCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateRequestSavePhrase(Object obj) {

    }

    @OnClick({R.id.mr_back_layout, R.id.btn_now_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_now_back:
                /**
                 * phraseTag标志带到back备份短语验证页面，说明是从旧用户入口进来备份的
                 */
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataList", new PhraseListBean(phraseAllList));
                bundle.putSerializable("verifyList", new VerfifyListBean(verinfyList));
                bundle.putString("phraseTag", "phraseTag");
                ARouter.getInstance()
                        .build(ArouterConstants.BACKPHRASE)
                        .withBundle("number", bundle)
                        .navigation();
                break;
        }
    }

    @Subscriber
    public void onOldBackPhraseInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 601) {
                this.finish();
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
