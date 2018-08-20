package com.idcg.idcw.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.GetAddAllCurrencyProviderServices;
import com.idcg.idcw.model.bean.AddDataBean;
import com.idcg.idcw.model.logic.AddCurrencyLogic;
import com.idcg.idcw.model.params.AddCurrencyIsShow;
import com.idcg.idcw.presenter.impl.AddCurrencyPresenterImpl;
import com.idcg.idcw.presenter.interfaces.AddCurrencyContract;
import com.idcg.idcw.widget.SearchEditText;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.fragments
 * 备注消息：
 * 修改时间：2018/3/18 19:43
 **/

public class AddAssetFragment extends BaseWalletFragment<AddCurrencyLogic, AddCurrencyPresenterImpl>
        implements AddCurrencyContract.View {

    @BindView(R.id.rv_add_currency)
    RecyclerView rvAddCurrency;
    @BindView(R.id.ll_shuzi_currency)
    LinearLayout llShuziCurrency;
    @BindView(R.id.query)
    SearchEditText query;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    Unbinder unbinder;

    private String mTitle;
    private CommonAdapter <AddDataBean> assetCommonAdapter;
    private List<AddDataBean> assetList = new ArrayList<>();
    private static final int INFO_AL_WHAT_UNM = 0x2432;
    List<AddDataBean> checkList = new ArrayList<>();
    List<AddDataBean> mClearList = new ArrayList<>();
    List<AddCurrencyIsShow> phraseList = new ArrayList<>();

    @Autowired
    GetAddAllCurrencyProviderServices mAddCurrencyServices;

    public static AddAssetFragment getInstance(String title) {
        AddAssetFragment self = new AddAssetFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_currency;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        smartrefreshlayout.setEnableLoadmore(false);
        smartrefreshlayout.setEnableRefresh(false);
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rvAddCurrency.setLayoutManager(linearLayoutManager);
            assetCommonAdapter = new CommonAdapter<AddDataBean>(R.layout.items_add_currency, assetList) {
                @Override
                public void commonconvert(BaseViewHolder helper, AddDataBean item) {
                    if (item == null) return;
                    ImageView imageView = helper.getView(R.id.img_currency_icon);
                    GlideUtil.loadImageViewCache(mContext,
                            String.valueOf(item.getLogo_url()), imageView);
                    helper.setText(R.id.currency_name, item.getCurrency().toUpperCase());
                    helper.setText(R.id.currency_tag, item.getCurrencyName());
                    ImageView imageView1 = helper.getView(R.id.img_gray_dian);

                    assetCommonAdapter.getData().get(helper.getAdapterPosition()).setSortIndex(helper.getAdapterPosition() + 1);
                    if (item.isIsShow()) {
                        imageView1.setImageResource(R.mipmap.icon_blue_dian);
                    } else {
                        imageView1.setImageResource(R.mipmap.icon_gray_dian);
                    }
                    LogUtil.e("checkList==:", new Gson().toJson(checkList));
                }
            };
            rvAddCurrency.setAdapter(assetCommonAdapter);
            rvAddCurrency.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    updateItemList(position);
                }
            });
        } catch (Exception ex) {

        }

        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
    }

    private void search() {
        String data = query.getText().toString().toUpperCase().trim();
        assetList.clear();
        for (int i = 0; i < mClearList.size(); i++) {
            AddDataBean workerInfo = mClearList.get(i);
            if (workerInfo.getCurrency().toUpperCase().contains(data)) {
                assetList.add(workerInfo);
            }
        }
        assetCommonAdapter.notifyDataSetChanged();
    }

    private void RequestAddCurrency() {
        phraseList.clear();
        try {
            for (AddDataBean s : assetCommonAdapter.getData()) {
                AddCurrencyIsShow currencyIsShow = new AddCurrencyIsShow();
                currencyIsShow.setSortIndex(s.getSortIndex());
                currencyIsShow.setId(s.getId());
                currencyIsShow.setIsShow(s.isIsShow());
                phraseList.add(currencyIsShow);
            }
            for (int i = 0; i < phraseList.size(); i++) {
                phraseList.get(i).setSortIndex(i + 1);
            }
            mAddCurrencyServices.requestAddCurrencyProvider(phraseList)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribeWith(new RxProgressSubscriber<Object>(this) {
                @Override
                protected void onError(ResponseThrowable ex) {
                    if (isAdded()) {
                        ToastUtil.show(getResources().getString(R.string.server_connection_error));
                    }
                }

                @Override
                public void onSuccess(Object object) {
                    EventBus.getDefault().post(new PosInfo(169));
                }
            });
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void updateItemList(int position) {
        if (assetCommonAdapter.getData().get(position).isIsShow()) {
            assetCommonAdapter.getData().get(position).setIsShow(false);
            RequestAddCurrency();
        } else {
            assetCommonAdapter.getData().get(position).setIsShow(true);
            RequestAddCurrency();
        }
        assetCommonAdapter.notifyItemChanged(position);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    protected void lazyFetchData() {
        showDialog();
        mPresenter.requestAddAllData();
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if (throwable.getErrorCode().equals("120")) {
            llShuziCurrency.setVisibility(View.VISIBLE);
        } else if (throwable.getErrorCode().equals("100")) {
            return;
        } else {
            if (isAdded()) {
                ToastUtil.show(getResources().getString(R.string.server_connection_error));
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INFO_AL_WHAT_UNM:
                    assetCommonAdapter.notifyDataSetChanged();
                    playLayoutAnimation(CommonLayoutAnimationHelper.getAnimationSetFromRight());
                    if (assetList == null) {
                        return;
                    }
                    break;
            }
        }
    };

    private void playLayoutAnimation(Animation animationSetFromRight) {
        playLayoutAnimation(animationSetFromRight, false);
    }

    /**
     * 播放RecyclerView动画
     *
     * @param animation
     * @param isReverse
     */
    public void playLayoutAnimation(Animation animation, boolean isReverse) {
        try {
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            controller.setDelay(0.1f);
            controller.setOrder(isReverse ? LayoutAnimationController.ORDER_REVERSE : LayoutAnimationController.ORDER_NORMAL);

            rvAddCurrency.setLayoutAnimation(controller);
            rvAddCurrency.getAdapter().notifyDataSetChanged();
            rvAddCurrency.scheduleLayoutAnimation();
        } catch (Exception ex) {

        }
    }

    @Override
    public void updateRequestAddData(List<AddDataBean> params) {
        dismissDialog();
        assetList.addAll(params);
        mClearList.addAll(params);
        Message message = mHandler.obtainMessage();
        message.what = INFO_AL_WHAT_UNM;
        mHandler.sendMessage(message);
    }

    @Subscriber
    public void onAddInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 100) {
                mPresenter.requestAddData();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Subscriber
    public void onSeleteAssetsInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 4567) {
                RequestAddCurrency();
            }

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void updateRequestAddCurrency(Object object) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
