package com.juxin.predestinate.module.logic.baseui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.juxin.library.utils.NetworkUtils;
import com.juxin.library.view.BasePanel;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.library.view.DownloadProgressView;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.login.LoginMgr;
import com.juxin.predestinate.module.logic.invoke.WebAppInterface;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 通用的网络请求处理panel
 * Created by ZRP on 2016/12/8.
 */
public class WebPanel extends BasePanel {

    private Context context;
    private String url;

    private DownloadProgressView progressView;
    private CustomFrameLayout customFrameLayout;
    private TextView errorTxt;
    private View btn_refresh;
    private WebView webView;

    public WebPanel(Context context, String url) {
        super(context);
        this.context = context;
        this.url = url;
        setContentView(R.layout.common_web_panel);

        initView();
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressView.setVisibility(View.GONE);
                // 判断有无网络
                if (!NetworkUtils.isConnected(context)) {
                    customFrameLayout.show(R.id.common_net_error);
                    btn_refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            webView.loadUrl(url);
                        }
                    });
                } else {
                    // 判断网络请求网址是否有效
                    if (!URLUtil.isValidUrl(url)) {
                        customFrameLayout.show(R.id.common_net_error);
                        errorTxt.setText("无效网址");
                    } else {
                        customFrameLayout.show(R.id.webView);
                    }
                }
            } else {
                progressView.setVisibility(View.VISIBLE);
                progressView.setProgress(newProgress);
            }
        }

        // 获取到url打开页面的标题
        public void onReceivedTitle(WebView view, String title) {
            if (titleListener != null) titleListener.onTitle(title);
        }

        //js交互提示
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        progressView = (DownloadProgressView) findViewById(R.id.progressView);
        customFrameLayout = (CustomFrameLayout) findViewById(R.id.customFrameLayout);
        btn_refresh = findViewById(R.id.error_btn);
        errorTxt = (TextView) findViewById(R.id.error_txt);
        webView = (WebView) findViewById(R.id.webView);
        customFrameLayout.setList(new int[]{R.id.webView, R.id.common_net_error});

        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);// 设置允许访问文件数据
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.supportMultipleWindows();// 多窗口

        synCookies();//格式化写入cookie，需写在setJavaScriptEnabled之后
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient() {// 让webView内的链接在当前页打开，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                MMLog.autoDebug("-----onPageFinished---->" + url);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }
        });
        webView.addJavascriptInterface(new WebAppInterface(getContext(), webView), "Android");
        webView.loadUrl(url);
    }

    /**
     * CookieManager会将这个Cookie存入该应用程序/data/data/databases/目录下的webviewCookiesChromium.db数据库的cookies表中
     * 需要在当前用户退出登录的时候进行清除
     */
    private void synCookies() {
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().setCookie(url, LoginMgr.cookie);
        CookieSyncManager.getInstance().sync();
    }

    private TitleListener titleListener;

    /**
     * 设置网页标题监听
     */
    public void setTitleListener(TitleListener titleListener) {
        this.titleListener = titleListener;
    }

    /**
     * 网页标题监听
     */
    public interface TitleListener {
        void onTitle(String title);
    }
}
