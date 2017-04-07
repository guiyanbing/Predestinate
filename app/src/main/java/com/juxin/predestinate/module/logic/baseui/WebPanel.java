package com.juxin.predestinate.module.logic.baseui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.juxin.library.log.PLogger;
import com.juxin.library.view.BasePanel;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.login.LoginMgr;
import com.juxin.predestinate.module.logic.invoke.WebAppInterface;
import com.juxin.predestinate.module.util.PerformanceHelper;
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

    private String url;

    private CustomFrameLayout customFrameLayout;
    private WebView webView;

    public WebPanel(Context context, String url) {
        super(context);
        this.url = url;
        setContentView(R.layout.common_web_panel);

        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        customFrameLayout = (CustomFrameLayout) findViewById(R.id.customFrameLayout);
        customFrameLayout.setList(new int[]{R.id.webView, R.id.common_net_error, R.id.common_loading});
        customFrameLayout.showOfIndex(2);
        findViewById(R.id.error_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(url);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setAllowFileAccess(false);//设置允许访问文件数据
        webSettings.setJavaScriptEnabled(true);
        if (PerformanceHelper.isHighPerformance(getContext())) {
            /*为高内存手机 增加清晰度*/
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
        }

        synCookies();//格式化写入cookie，需写在setJavaScriptEnabled之后
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (titleListener != null) titleListener.onTitle(s);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                super.onReceivedError(webView, errorCode, description, failingUrl);
                customFrameLayout.showOfIndex(1);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                PLogger.d("-----onPageFinished---->" + url);
                customFrameLayout.showOfIndex(0);
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
                super.onPageStarted(webView, url, bitmap);
                customFrameLayout.showOfIndex(2);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }
        });
        webView.addJavascriptInterface(new WebAppInterface(getContext(), webView), "Android");
        webView.requestFocus();
        webView.loadUrl(url);
    }

    /**
     * @return 获取展示的WebView实例
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     * 隐藏loading并展示WebView
     */
    public void hideLoading(){
        customFrameLayout.showOfIndex(0);
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
