package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cjwsc.idcm.Utils.LogUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.GetAboutUsListProviderServices;
import com.idcg.idcw.model.bean.AboutUsBean;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.StringUtils;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 14:48
 **/
@Route(path = ArouterConstants.CONTACT, name = "联系我们的页面")
public class ContactActivity extends BaseWalletActivity {

    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_about_content)
    TextView tvAboutContent;
    @BindView(R.id.rv_about_us)
    RecyclerView rvAboutUs;
    private CommonAdapter commonAdapter;

    @Autowired
    GetAboutUsListProviderServices getAboutUsListProviderServices;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        tvSetName.setText(R.string.constract);
        tvVersion.setText(String.valueOf(getString(R.string.tv_version) + " " + StringUtils.getVersionName(this)));
        tvAboutContent.setText(String.valueOf("©" + "2017-2018 IDCW. All Rights Reserved."));

        initAboutUsRecycler();
    }

    private void initAboutUsRecycler() {
        commonAdapter = new CommonAdapter<AboutUsBean>(R.layout.item_about_us) {
            @Override
            public void commonconvert(BaseViewHolder helper, AboutUsBean item) {
                GlideUtil.loadImageView(mContext, item.getIcon(), (ImageView) helper.getView(R.id.img_about_us));
                ((TextView) helper.getView(R.id.tv_about_us)).setText(item.getTitle());
                ((TextView) helper.getView(R.id.tv_about_us_content)).setText(item.getContent());

                helper.getConvertView().setOnClickListener(v -> {

                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAboutUs.setLayoutManager(linearLayoutManager);
        rvAboutUs.setAdapter(commonAdapter);
    }

    @Override
    protected void onEvent() {
        requestAboutUsList();
    }

    private void requestAboutUsList() {
        getAboutUsListProviderServices.requestAboutUsListProvider(AppLanguageUtils.getLanguageLocalCode(mCtx))
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<List<AboutUsBean>>((BaseProgressView) mCtx) {
                    @Override
                    public void onSuccess(List<AboutUsBean> data) {
                        LogUtil.e("AboutFragment===", new Gson().toJson(data));
                        commonAdapter.setNewData(data);
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(foxidcw.android.idcw.otc.R.string.server_connection_error));
                    }
                });
    }

    @OnClick(R.id.mr_back_layout)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    protected void checkAppVersion() {

    }
}
