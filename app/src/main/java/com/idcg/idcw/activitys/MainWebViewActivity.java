package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.CodeBean;
import com.idcg.idcw.model.bean.ShareStatus;
import com.idcg.idcw.widget.dialog.CommonShareDialog;
import com.idcg.idcw.widget.dialog.MyInvitationCodeDiaLog;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.google.gson.Gson;

import org.simple.eventbus.Subscriber;

import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by hpz on 2018/7/3.
 */

@Route(path = ArouterConstants.SHARE_WEB_VIEW, name = "分享页面h5")
public class MainWebViewActivity extends BaseWalletActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    @BindView(R.id.webview)
    WebView mWebView;
    //private String url = NetWorkApi.SHARE_COMMON_URL;

    @Autowired(name = "shareUrl")
    String url = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_web;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        LoginStatus loginStatus = LoginUtils.getLoginBean(mCtx);
        if (getResources().getConfiguration().locale.getLanguage().equals("zh")||getResources().getConfiguration().locale.getLanguage().equals("cn")){
            url = url +"?userid="+ loginStatus.getId() + "&clientType=android&lang=zh-cn";
        }else  if(getResources().getConfiguration().locale.getLanguage().equals("fi")){
            url = url +"?userid="+ loginStatus.getId() + "&clientType=android&lang=hk";
        }else {
            url = url +"?userid="+ loginStatus.getId() + "&clientType=android&lang="+getResources().getConfiguration().locale.getLanguage();
        }

        LogUtil.d("userid:", LoginUtils.getLoginBean(mCtx).getId() + "");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(new JS(), "Android");//Android需要大写
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
                if (progressBar1 != null) {
                    if (newProgress == 100) {
                        progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                    } else {
                        progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        progressBar1.setProgress(newProgress);//设置进度值
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtil.d("onReceivedTitle:", title);
                if (tvSetName!=null){
                    tvSetName.setText(title);
                }
            }
        });

        if (TextUtils.isEmpty(url)) return;
        mWebView.loadUrl(url);
        LogUtil.e("url===>", url);
    }

    public class JS {
        @JavascriptInterface
        public void copyCode(String str) {//复制邀请码
            if (TextUtils.isEmpty(str)) return;
            ClipboardManager clipboard = (ClipboardManager) mCtx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, str);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
            }
            ToastUtil.show(getString(R.string.dialog_receive));
        }

        @JavascriptInterface
        public void showMyQrCode(String codeResponse) {//我的二维码
            if (TextUtils.isEmpty(codeResponse)) return;
            Gson gson = new Gson();
            CodeBean codeBean = gson.fromJson(codeResponse, CodeBean.class);
            MyInvitationCodeDiaLog myInvitationCodeDiaLog = new MyInvitationCodeDiaLog(mCtx, codeBean.getUrl(), codeBean.getTitle(), codeBean.getSubtitle());
            myInvitationCodeDiaLog.show();
            LogUtil.e("url===>", new Gson().toJson(codeResponse));
        }

        @JavascriptInterface
        public void showSharePanel(String response) {//分享邀请好友
            if (TextUtils.isEmpty(response)) return;
            Gson gson = new Gson();
            ShareStatus shareStatus = gson.fromJson(response, ShareStatus.class);

            URL url;
            try {//图片url正确的
                url = new URL(shareStatus.getImg());
                InputStream in = url.openStream();
                //ToastUtil.show("连接可用");
                CommonShareDialog commonShareDialog = new CommonShareDialog(
                mCtx, shareStatus.getTitle(),
                        shareStatus.getDesc(),
                        shareStatus.getUrl(),
                        shareStatus.getImg());
                commonShareDialog.show();
            } catch (Exception e1) {//图片url错误的
                //ToastUtil.show("连接打不开!");
                url = null;
                CommonShareDialog commonShareDialog = new CommonShareDialog(
                        mCtx, shareStatus.getTitle(),
                        shareStatus.getDesc(),
                        shareStatus.getUrl(),
                        "");
                commonShareDialog.show();
            }


            LogUtil.e("url===>",
                    shareStatus.getTitle() + "--"
                            + shareStatus.getDesc() + "--"
                            + shareStatus.getUrl() + "--"
                            + shareStatus.getImg());
        }

    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    //使用Webview的时候，返回键没有重写的时候会直接关闭程序，这时候其实我们要其执行的知识回退到上一步的操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.mr_back_layout)
    public void onViewClicked() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    //6321
    @Subscriber
    public void getShareCode(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 6321) {
            ToastUtil.show(getString(R.string.str_share_success_text));
        }else if (posInfo.getPos() == 6322){
            ToastUtil.show(getString(R.string.str_share_already));
        }
    }
}
