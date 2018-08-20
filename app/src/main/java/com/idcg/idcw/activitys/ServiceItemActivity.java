package com.idcg.idcw.activitys;

import android.os.Bundle;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.base.BaseView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2018/3/27.
 */

@Route(path = ArouterConstants.serviceItem,name = "服务条款页面")
public class ServiceItemActivity extends BaseWalletActivity {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_item;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onEvent() {
        readHtmlFormAssets();
    }

    private void readHtmlFormAssets() {
        try {
            String locale = Locale.getDefault().getLanguage();
            WebSettings webSettings = webView.getSettings();
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            if ("zh".equals(locale)) {
                webView.loadUrl("file:///android_asset/service.html");
            }else {
                webView.loadUrl("file:///android_asset/serviceEn.html");
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick({R.id.mr_back_layout, R.id.tv_set_Name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.tv_set_Name:
                break;
        }
    }
}
