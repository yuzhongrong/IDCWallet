package com.idcg.idcw.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.base.BaseView;

import butterknife.BindView;
import butterknife.OnClick;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by hpz on 2018/1/15.
 */
@Route(path = ArouterConstants.WEB_VIEW, name = "加载网页代码")
public class WebViewActivity extends BaseWalletActivity {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    private String coinUrl = "";
    private String title = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        Intent intent = getIntent();
        Bundle bundle1 = intent.getExtras();
        coinUrl = bundle1.getString("url");
        title = bundle1.getString("title");
        tvSetName.setText(title);
        LogUtil.e("getBundleExtra:", coinUrl);
    }

    @Override
    protected void onEvent() {
        tvSetName.setText("");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


                                      @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();// 接受所有网站的证书
           }

        }


        );
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar1!=null){
                    if (newProgress == 100) {
                        progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                    } else {
                        progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        progressBar1.setProgress(newProgress);//设置进度值
                    }
                }
            }
        });
        if (TextUtils.isEmpty(coinUrl)) return;
        mWebView.loadUrl(coinUrl);
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



    @Override
    protected boolean isCheckVersion() {
        return false;
    }
}
