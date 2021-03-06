package com.juxin.predestinate.ui.pay.wepayother.h5;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.view.BasePanel;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.invoke.WebAppInterface;
import com.juxin.predestinate.module.util.PerformanceHelper;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 专用的支付panel
 * Created by ZRP on 2016/12/8.
 * Fix by IQQ on 2017/05/27.
 */
public class WebPanelPayH5 extends BasePanel {
    private static final int FRAME_WEB = 0;     //web页
    private static final int FRAME_ERROR = 1;   //加载失败页
    private static final int FRAME_LOADING = 2; //loading页
    private String url;
    private boolean isLoadingInnerControl = true;    //是否由内部控制loading的展示逻辑
    private CustomFrameLayout customFrameLayout;
    private WebView webView;
    private Context context;
    private H5PayStart h5PayStart;

    public WebPanelPayH5(Context context, String url, boolean isLoadingInnerControl, H5PayStart h5PayStart) {
        super(context);
        this.context = context;
        this.url = url;
        this.isLoadingInnerControl = isLoadingInnerControl;
        setContentView(R.layout.common_web_panel);
        this.h5PayStart = h5PayStart;
        initView();
    }


    private void toWeixin(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ((Activity) context).startActivityForResult(intent, PayWebActivity.payResutl);
        } else {
            PToast.showShort("微信未安装");
        }
        if(null != h5PayStart)
            h5PayStart.OnPayStart();
    }

    public String getHostName(String urlString) {
        int index = urlString.indexOf("://");
        String header = "";
        if (index != -1) {
            header = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index);
        }
        return header + urlString;
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        customFrameLayout = (CustomFrameLayout) findViewById(R.id.customFrameLayout);
        customFrameLayout.setList(new int[]{R.id.webView, R.id.common_net_error, R.id.common_loading});
        customFrameLayout.showOfIndex(FRAME_LOADING);
        customFrameLayout.setShowOfIndexChangeListener(new CustomFrameLayout.OnShowOfIndexChangeListener() {
            @Override
            public void onChange(CustomFrameLayout view, int id) {
                if (id != FRAME_LOADING)
                    view.stopLoading(R.id.loading_gif);
            }
        });

        ImageLoader.loadFitCenter(getContext(), R.drawable.f1_loading_h5, (ImageView) findViewById(R.id.loading_gif), 0, 0);

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
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不从缓存加载，确保每次进入的时候都是从服务器请求的最新页面
        if (PerformanceHelper.isHighPerformance(getContext())) {
            /*为高内存手机 增加清晰度*/
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (webListener != null) webListener.onTitle(s);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith("weixin://")) {
                    toWeixin(url);
                } else {
                    String ref = getHostName(webView.getUrl());
                    Map extraHeaders = new HashMap();
                    extraHeaders.put("Referer", ref);
                    webView.loadUrl(url, extraHeaders);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                super.onReceivedError(webView, errorCode, description, failingUrl);
                customFrameLayout.showOfIndex(FRAME_ERROR);
                if (webListener != null) webListener.onLoadFinish(WebLoadStatus.ERROR);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                PLogger.d("-----onPageFinished---->" + url);
                if (isLoadingInnerControl) hideLoading();
                if (webListener != null) webListener.onLoadFinish(WebLoadStatus.FINISH);
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
                super.onPageStarted(webView, url, bitmap);
//                customFrameLayout.showOfIndex(FRAME_LOADING);
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
     * 清除webView引用并置空
     */
    public void clearReference() {
        if (webView != null) {
            webView.removeJavascriptInterface("Android");
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    /**
     * 隐藏loading并展示WebView
     */
    public void hideLoading() {
        customFrameLayout.showOfIndex(FRAME_WEB);
    }

    private WebListener webListener;

    /**
     * 设置网页标题监听
     */
    public void setWebListener(WebListener webListener) {
        this.webListener = webListener;
    }

    /**
     * 网页标题监听
     */
    public interface WebListener {
        /**
         * 解析网页得到title
         *
         * @param title html title
         */
        void onTitle(String title);

        /**
         * 页面加载完成，由多方控制，如onPageFinished，cmd-hide_loading
         */
        void onLoadFinish(WebLoadStatus loadStatus);
    }

    /**
     * 网络加载状态
     */
    public enum WebLoadStatus {
        ERROR, FINISH
    }
}
