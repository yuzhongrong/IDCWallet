package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.model.bean.NoticeOptionBean;
import com.idcg.idcw.model.bean.NoticeResBean;
import com.idcg.idcw.model.logic.NoticeLogic;
import com.idcg.idcw.model.params.NoticeListReqParam;
import com.idcg.idcw.presenter.impl.NoticePresenterImpl;
import com.idcg.idcw.presenter.interfaces.NoticeContract;

import com.idcg.idcw.utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.CommonAnimUtils;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 18:07
 **/
@Route(path = ArouterConstants.NOTICE_LIST, name = "通知列表")
public class NoticeActivity extends BaseWalletActivity<NoticeLogic, NoticePresenterImpl> implements
        NoticeContract.View {

    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    private CommonAdapter commonAdapter;
    @BindView(R.id.noticeslist)
    SwipeMenuRecyclerView mRecyclerView;

    @BindView(R.id.mr_back_layout)
    LinearLayout img_back;
    @BindView(R.id.tv_set_Name)
    TextView tv_set_Name;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    private int pageIndex = 1;
    private int pageSize = 10;

    private int mCurrentAdapterPosition = -1;
    private boolean isloadmore = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        tv_set_Name.setText(getString(R.string.tv_notice));
        LinearLayoutManager horizeLinLayout = new LinearLayoutManager(this);
        horizeLinLayout.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(horizeLinLayout);

        // recycleview item 创建右侧删除菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(NoticeActivity.this); // 各种文字和图标属性设置。
                deleteItem.setText(getString(R.string.str_item_del));
                deleteItem.setTextColor(getResources().getColor(R.color.white));
                deleteItem.setBackground(R.color.common_red);
                deleteItem.setHeight(MATCH_PARENT);
                deleteItem.setWidth((int) getResources().getDimension(R.dimen.d100));
                rightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。

            }
        };
        // 安装菜单。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        //监听菜单点击
        // 菜单点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(menuBridge ->  {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                //获取点击这个item的bean
                NoticeResBean.MsgDataBean itembean = (NoticeResBean.MsgDataBean) commonAdapter.getData().get(adapterPosition);
                //构造删除对象
                NoticeOptionBean optionBean = new NoticeOptionBean();
                optionBean.setMsgId(Arrays.asList(itembean.getMsId()));//设置id
                optionBean.setType(1);//删除
                String result = JSON.toJSONString(optionBean);
                if (!TextUtils.isEmpty(result)) {
                    //这个方法要的是一串json
                    mCurrentAdapterPosition = adapterPosition;
                    mPresenter.getNoticeDel(result);
            }
        });

        mRecyclerView.setAdapter(commonAdapter = new CommonAdapter<NoticeResBean.MsgDataBean>(R.layout.item_notice_layout) {
            @Override
            public void commonconvert(BaseViewHolder helper, NoticeResBean.MsgDataBean item) {
                Glide.with(NoticeActivity.this).load(item.getLogo())
                        .into((ImageView) helper.getView(R.id.icon));
                ((TextView) helper.getView(R.id.title)).setText(item.getMsgTitle());
                ((TextView) helper.getView(R.id.subtitle)).setText(item.getSecondaryTitle());
                try {
                    String realy = TimeUtils.getOtherTime(item.getStartTime());//转换成时间戳
                    ((TextView) helper.getView(R.id.time)).setText(realy);
                } catch (Exception e) {
                }
                helper.getConvertView().setOnClickListener(v -> {
                    if (TextUtils.isEmpty(item.getMsgTitle()) || TextUtils.isEmpty(item.getContentUrl()))
                        return;
                    ARouter.getInstance().build(ArouterConstants.WEB_VIEW)
                            .withString("title", item.getMsgTitle())
                            .withString("url", item.getContentUrl())// + "?id=" + item.getId()
                            .navigation();
                });
            }
        });
        commonAdapter.setEmptyView(R.layout.recyclerview_empty_notice_layout, mRecyclerView);
        smartrefreshlayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex += 1;
                isloadmore = true;
                loadData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                isloadmore = false;
                loadData();
            }
        });
        isloadmore = false;
        loadData();
    }

    private void loadData() {
        //showDialog();
        NoticeListReqParam reqParam = new NoticeListReqParam();
        reqParam.setLang(AppLanguageUtils.getLanguageLocalCode(mCtx));
        reqParam.setPageIndex(String.valueOf(pageIndex));
        reqParam.setPageSize(String.valueOf(pageSize));
        reqParam.setClient("android");
        mPresenter.getNoticeList(reqParam, smartrefreshlayout != null && !smartrefreshlayout.isRefreshing()
                && !smartrefreshlayout.isLoading());
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        ToastUtil.show(getString(R.string.server_connection_error));
        onUpdateSmartRefresh(smartrefreshlayout);
        smartrefreshlayout.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
    }

    private void onUpdateSmartRefresh(SmartRefreshLayout smartRefreshLayout) {
        if (smartRefreshLayout != null) {
            if (smartRefreshLayout.isRefreshing()) {
                smartRefreshLayout.finishRefresh();
            } else if (smartRefreshLayout.isLoading()) {
                smartRefreshLayout.finishLoadmore();
            }
        }
    }

    @Override
    public void updateNoticeList(NoticeResBean result) {
        dismissDialog();
        if (result != null && result.getMsgData() != null && result.getMsgData().size() > 0) {
            if (smartrefreshlayout == null) return;
            smartrefreshlayout.resetNoMoreData();
            if (isloadmore) {//加载更多
                //llNotice.setVisibility(View.GONE);
                smartrefreshlayout.finishLoadmore();
                commonAdapter.addData(result.getMsgData());
            } else {//重新加载数据
                //llNotice.setVisibility(View.GONE);
                smartrefreshlayout.finishRefresh();
                commonAdapter.setNewData(result.getMsgData());
                CommonAnimUtils.playCommonAllViewAnimation(mRecyclerView, CommonLayoutAnimationHelper.getAnimationSetFromRight(), false);
            }
        } else {
            onUpdateSmartRefresh(smartrefreshlayout);
            smartrefreshlayout.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        }
    }

    @Override
    public void updateNoticeDel(Boolean result) {
        //dismissDialog();
        if (mCurrentAdapterPosition != -1) {
            commonAdapter.getData().remove(mCurrentAdapterPosition);
            commonAdapter.notifyItemRemoved(mCurrentAdapterPosition);
            mCurrentAdapterPosition = -1;
        }
    }

    @OnClick(R.id.mr_back_layout)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.mr_back_layout) {
            finish();
        }
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
