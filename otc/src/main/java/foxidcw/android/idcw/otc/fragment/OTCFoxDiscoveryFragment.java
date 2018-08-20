package foxidcw.android.idcw.otc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.FragmentEvent;

import cn.bingoogolapple.bgabanner.BGABanner;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCDiscoveryListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCDiscoveryBean;
import foxidcw.android.idcw.otc.widgets.dialog.OTCAuthorizationPopWindow;

/**
 * Created by hpz on 2018/5/22.
 */

public class OTCFoxDiscoveryFragment extends BaseWalletFragment {
    private TextView mTvOtcDiscovery;
    private RecyclerView mRvOtcDiscovery;
    private com.cjwsc.idcm.adapter.CommonAdapter commonAdapter;
    private BGABanner mZoomBanner;

    @Autowired
    OTCDiscoveryListProviderServices otcDiscoveryListProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fox_discovery;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mTvOtcDiscovery = (TextView) rootView.findViewById(R.id.tv_otc_discovery);
        mRvOtcDiscovery = (RecyclerView) rootView.findViewById(R.id.rv_otc_discovery);
        mZoomBanner = (BGABanner) rootView.findViewById(R.id.banner_main_zoom);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvOtcDiscovery.setLayoutManager(linearLayoutManager);
        initDiscoveryRecycler();
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {
        requestList();//网络请求
    }

    private void requestList() {
        otcDiscoveryListProviderServices.requestDiscoveryListProvider(AppLanguageUtils.getLanguageLocalCode(mContext))
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<OTCDiscoveryBean>((BaseProgressView) mContext) {
                    @Override
                    public void onSuccess(OTCDiscoveryBean data) {
                        LogUtil.e("fragment===", new Gson().toJson(data));
                        mZoomBanner.setAutoPlayAble(data.getBannerList().size() > 1);
                        commonAdapter.setNewData(data.getModuleList());
                        mZoomBanner.setData(R.layout.item_list_discovery, data.getBannerList(), null);
                        loadData();//填充数据
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }
                });
    }

    private void loadData() {
        mZoomBanner.setAdapter(new BGABanner.Adapter<View, OTCDiscoveryBean.BannerListBean>() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, @Nullable OTCDiscoveryBean.BannerListBean model, int position) {
                ImageView imageView = itemView.findViewById(R.id.iv_discovery);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtil.loadImageViewLoding(itemView.getContext(), model.getImageUrl(), imageView, R.drawable.holder, R.drawable.holder);
            }
        });

        mZoomBanner.setDelegate(new BGABanner.Delegate<View, OTCDiscoveryBean.BannerListBean>() {//banner点击事件
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable OTCDiscoveryBean.BannerListBean model, int position) {
                if (model.isIsHaveUrl()) {
                    ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                            .withString("url", model.getUrl())
                            .withInt("skipTag", 3)
                            .navigation(mContext);
                }
            }
        });
    }

    private void initDiscoveryRecycler() {
        commonAdapter = new CommonAdapter<OTCDiscoveryBean.ModuleListBean>(R.layout.item_card_discovery) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCDiscoveryBean.ModuleListBean item) {//第一个列表标题数据
                ((TextView) helper.getView(R.id.tv_otc_discovery_title)).setText(item.getModuleName());
                ((RecyclerView) helper.getView(R.id.rv_otc_card_discovery)).setLayoutManager(new GridLayoutManager(getActivity(), 4));//内嵌列表数解析
                ((RecyclerView) helper.getView(R.id.rv_otc_card_discovery)).setAdapter(new CommonAdapter<OTCDiscoveryBean.ModuleListBean.DappListBean>(R.layout.item_discovery, item.getDappList()) {
                    //设置数据
                    @Override
                    public void commonconvert(BaseViewHolder helper, OTCDiscoveryBean.ModuleListBean.DappListBean item) {
                        GlideUtil.loadImageViewLoding(mContext, item.getLogoUrl(), (ImageView) helper.getView(R.id.iv_game_logo), R.drawable.otc_holder, R.drawable.otc_holder);
                        ((TextView) helper.getView(R.id.tv_game_name)).setText(item.getDappName());
                        helper.getConvertView().setOnClickListener(v -> {
                            String mdAESstring = null;
                            if (!TextUtils.isEmpty(LoginUtils.getLoginBean(mContext).getGuid())) {
                                try {
                                    mdAESstring = new AESUtil().getBase64(LoginUtils.getLoginBean(mContext).getGuid());
                                    LogUtil.e("class==", mdAESstring);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            requestIfRead(String.valueOf(item.getId()), item.getUrl(), item.getLogoUrl(), item.getDappName(), mdAESstring);//判断Dapp是否已读
                        });
                    }
                });
            }
        };
        mRvOtcDiscovery.setAdapter(commonAdapter);
    }

    private void requestIfRead(String id, String url, String logoUrl, String name, String mdAESstring) {
        otcDiscoveryListProviderServices.requestDiscoveryIfReadProvider(id)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<Boolean>((BaseProgressView) mContext) {
                    @Override
                    public void onSuccess(Boolean data) {
                        LogUtil.e("fragment===1", new Gson().toJson(data));
                        if (data) {
                            if (TextUtils.isEmpty(url)) return;
                            ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                                    .withString("url", url + "?openid=" + mdAESstring)//+"?openid="+id
                                    .withInt("skipTag", 3)
                                    .navigation(mContext);
                            LogUtil.e("fragment===2", url + "?openid=" + mdAESstring);
                        } else {
                            OTCAuthorizationPopWindow otcAuthorizationPopWindow = new OTCAuthorizationPopWindow(mContext);
                            otcAuthorizationPopWindow.showPopupWindow();
                            GlideUtil.loadImageViewLoding(mContext, logoUrl, otcAuthorizationPopWindow.setImageRes(), R.drawable.otc_holder, R.drawable.otc_holder);
                            otcAuthorizationPopWindow.getTvGameName().setText(name + " " + getString(R.string.str_otc_apply_get_name));
                            otcAuthorizationPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                                if (TextUtils.isEmpty(url)) return;
                                requestSetRead(id);//设置已读
                                if (otcAuthorizationPopWindow != null && otcAuthorizationPopWindow.isShowing()) {
                                    otcAuthorizationPopWindow.dismiss();
                                }
                                ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                                        .withString("url", url + "?openid=" + mdAESstring)//+"?openid="+mdAESstring
                                        .withInt("skipTag", 3)
                                        .navigation(mContext);
                            });
                        }
                        LogUtil.e("class==", url + "?openid=" + mdAESstring);
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.str_server_get_permission_error));
                    }
                });
    }

    private void requestSetRead(String id) {
        otcDiscoveryListProviderServices.requestDiscoverySetReadProvider(id)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<Boolean>((BaseProgressView) mContext) {
                    @Override
                    public void onSuccess(Boolean data) {
                        LogUtil.e("fragment===2", new Gson().toJson(data));
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.str_server_get_permission_error));
                    }
                });
    }
}
