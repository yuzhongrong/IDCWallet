package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.model.bean.VersionListBean;
import com.idcg.idcw.model.logic.RequestVersionListLogic;
import com.idcg.idcw.presenter.impl.RequestVersionListImpl;
import com.idcg.idcw.presenter.interfaces.IntroduceContract;

import com.idcg.idcw.utils.TimeUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.CommonAnimUtils;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;

@Route(path = ArouterConstants.INTRODUCE, name = "版本列表")
public class IntroduceActivity extends BaseWalletActivity<RequestVersionListLogic, RequestVersionListImpl>
        implements IntroduceContract.View {

    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    private CommonAdapter commonAdapter;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.rv_introduce)
    RecyclerView mRecyclerView;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    private int pageIndex = 1;
    private int tag;
    private boolean isloadmore;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_introduce;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        tvSetName.setText(getString(R.string.power_induction));
        LinearLayoutManager horizeLinLayout = new LinearLayoutManager(this);
        horizeLinLayout.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(horizeLinLayout);

        mRecyclerView.setAdapter(commonAdapter = new CommonAdapter<VersionListBean.MobileVersionListBean>(R.layout.item_introduce) {
            @Override
            public void commonconvert(BaseViewHolder helper, VersionListBean.MobileVersionListBean item) {
                helper.setText(R.id.update_title, item.getVersion_name());
                try {
                    String realy = TimeUtils.getOtherTime(item.getCreateTime());//转换成时间戳
                    String finalTime = realy.replaceAll("/", "-");
                    finalTime = finalTime.substring(0, finalTime.indexOf(" "));
                    helper.setText(R.id.update_time, finalTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                helper.getConvertView().setOnClickListener(v -> {
                    if (TextUtils.isEmpty(item.getVersion_introduction_url()) || TextUtils.isEmpty(item.getVersion_name()))
                        return;
                    ARouter.getInstance().build(ArouterConstants.WEB_VIEW)
                            .withString("title", item.getVersion_name())
                            .withString("url", item.getVersion_introduction_url())
                            .navigation();
                });
            }
        });
        smartrefreshlayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                tag = 1;
                pageIndex += 1;
                requestVersionList(pageIndex, true);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                tag = 1;
                pageIndex = 1;
                requestVersionList(pageIndex, false);
            }
        });
    }

    private void requestVersionList(int pageIndex, boolean b) {
        isloadmore = b;
        if (tag == 1) {
            dismissDialog();
        } else if (tag == 0) {
            showDialog();
        }
        mPresenter.RequestVersionList(AppLanguageUtils.getLanguageLocalCode(mCtx), String.valueOf(pageIndex));
    }

    @Override
    protected void onEvent() {
        tag = 0;
        requestVersionList(pageIndex, false);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
        onUpdateSmartRefresh(smartrefreshlayout);
    }

    private void onUpdateSmartRefresh(SmartRefreshLayout smartrefreshlayout) {
        if (smartrefreshlayout != null) {
            if (smartrefreshlayout.isRefreshing()) {
                smartrefreshlayout.finishRefresh();
            } else if (smartrefreshlayout.isLoading()) {
                smartrefreshlayout.finishLoadmore();
            }
        }
    }

    @Override
    public void updateRequestVersionList(VersionListBean result) {
        dismissDialog();
        if (result != null && result.getMobileVersionList() != null && result.getMobileVersionList().size() > 0) {
            smartrefreshlayout.resetNoMoreData();
            if (isloadmore) {//加载更多
                smartrefreshlayout.finishLoadmore();
                commonAdapter.addData(result.getMobileVersionList());
            } else {//重新加载数据
                smartrefreshlayout.finishRefresh();
                commonAdapter.setNewData(result.getMobileVersionList());
                CommonAnimUtils.playCommonAllViewAnimation(mRecyclerView, CommonLayoutAnimationHelper.getAnimationSetFromRight(), false);
            }
        } else {
            if (!isloadmore) {
                llNotice.setVisibility(View.VISIBLE);
            }
            onUpdateSmartRefresh(smartrefreshlayout);
            smartrefreshlayout.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        }
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }

    @OnClick(R.id.mr_back_layout)
    public void onViewClicked() {
        finish();
    }
}
