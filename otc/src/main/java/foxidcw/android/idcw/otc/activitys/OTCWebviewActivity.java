package foxidcw.android.idcw.otc.activitys;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.base.BaseView;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by hpz on 2018/5/11.
 */

@Route(path = OTCConstant.RULEWEBVIEW, name = "保证金规则webview")
public class OTCWebviewActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private ProgressBar mProgressBar1;
    private WebView mWebView;
    @Autowired(name = "url")
    String url = "http://192.168.1.35:81/static/h5/otc/tradingRules/index.html?lang=zh-CN";
    @Autowired(name = "skipTag")
    int skipTag = 1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_otc_web_view;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mMrBackLayout.setOnClickListener(this);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mWebView = (WebView) findViewById(R.id.webview);
        if (skipTag == 2) {
            mTvSetName.setText(getString(R.string.str_otc_transaction_rule));
        } else if (skipTag == 3) {
            mTvSetName.setText("");
        } else {
            mTvSetName.setText(getString(R.string.str_otc_deposit_rules_title));
        }

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
                                      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                              view.loadUrl(request.getUrl().toString());
                                          } else {
                                              view.loadUrl(request.toString());
                                          }
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
                if (newProgress == 100) {
                    mProgressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mProgressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar1.setProgress(newProgress);//设置进度值
                }
            }
        });
        if (TextUtils.isEmpty(url)) return;
        String product = Build.PRODUCT;
        String[] products = {"starqltesq", "star2qltesq", "starqlteue", "star2qlteue", "starqltezc", "star2qltezc"};
        //if (Arrays.<String>asList(products).contains(product)) {
        if (product.startsWith("star")) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.loadUrl(url);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            this.finish();
        } else {
        }
    }
}
