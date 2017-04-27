package com.juxin.predestinate.ui.pay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.mumu.bean.utils.NetUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.WebPanel;

/**
 * Created by Kind on 2017/4/26.
 */

public class PayViewPanel extends WebPanel{

    private ProgressBar progress_bar;
    protected CustomFrameLayout customFrameLayout;
    protected TextView errorTxt;
    protected View refresh;// 刷新按钮
    protected WebView webView;

    protected boolean isNeedEnc = true;// 网址是否需要加密

    protected WebChromeClient chromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progress_bar.setVisibility(View.GONE);
//                if (progressListener != null) {
//                    progressListener.progressOver();
//                }
                // 判断有无网络
                if (!NetUtil.isAvaliable()) {
                    customFrameLayout.show(R.id.common_net_error);
                    refresh.setVisibility(View.VISIBLE);
                    refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  webView.loadUrl(getWebviewUrl());
                        }
                    });
                } else {
                    // 判断网络请求网址是否有效
//                    if (!URLUtil.isValidUrl(getWebviewUrl())) {
//                        customFrameLayout.show(R.id.common_net_error);
//                        errorTxt.setText("无效网址");
//                    } else {
//                        customFrameLayout.show(R.id.common_web);
//                    }
                }
            } else {
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setProgress(newProgress);
            }
        }

        // 获取到url打开页面的标题
        public void onReceivedTitle(WebView view, String title) {
           // if (titleListener != null) titleListener.onTitle(title);
        }

        //js交互提示
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    };

    /**
     * @param context
     * @param url       网址url
     * @param isNeedEnc 网址是否需要加密，默认进行加密
     * @param param     提交数据参数/网页交互参数，为一个json串。交互格式为一个普通json串，提交格式如下：
     *                  - post方式提交格式：{"post":{"s":"d","w":"e"}}
     *                  - get方式提交格式：{"get":{"s":"d","w":"e"}}
     */
    public PayViewPanel(Context context, String url, boolean isNeedEnc, String param) {
        super(context, null, false);
        this.isNeedEnc = isNeedEnc;
        setContentView(R.layout.common_web_panel);
      //  initView(onEncUrl(isNeedEnc, url, param));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(boolean isGetRequest) {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
      //  customFrameLayout = (CustomFrameLayout) findViewById(R.id.web_fram);
        customFrameLayout.setList(new int[]{R.id.webView, R.id.common_net_error});
        refresh = findViewById(R.id.error_btn);
        errorTxt = (TextView) findViewById(R.id.error_txt);
        webView = (WebView) findViewById(R.id.webView);


        WebSettings settings = webView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.setVerticalScrollbarOverlay(true);


        synCookies();//格式化写入cookie，需写在setJavaScriptEnabled之后
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient() {// 让webView内的链接在当前页打开，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                final PayTask task = new PayTask((Activity)getContext());
                final String ex = task.fetchOrderInfoFromH5PayUrl(url);
                if (!TextUtils.isEmpty(ex)) {
                    System.out.println("paytask:::::" + url);
                    new Thread(new Runnable() {
                        public void run() {
                            System.out.println("payTask:::" + ex);
                            final H5PayResultModel result = task.h5Pay(ex, true);
                            if (!TextUtils.isEmpty(result.getReturnUrl())) {
                                ((Activity)getContext()).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        view.loadUrl(result.getReturnUrl());
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });

     //   webView.loadUrl(getWebviewUrl());

//        if (isGetRequest) {
//            webView.loadUrl(url);
//        } else {//如果是post请求单独提交数据
//            webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
//        }
    }

    /**
     * CookieManager会将这个Cookie存入该应用程序/data/data/databases/目录下的webviewCookiesChromium.db数据库的cookies表中
     * 需要在当前用户退出登录的时候进行清除
     */
    private void synCookies() {
        CookieSyncManager.createInstance(getContext());
    //    CookieManager.getInstance().setCookie(getWebviewUrl(), ModuleMgr.getLoginMgr().getCookieVerCode());
        CookieSyncManager.getInstance().sync();
    }

    public void onDestroy() {
        if (webView != null) {
            webView.removeAllViews();
            try {
                webView.destroy();
            } catch (Throwable t) {
            }
            webView = null;
        }
    }

    /**
     * 回调网页中的脚本接口。
     *
     * @param method 调用网页方法的方法名
     * @param notify 传给网页的通知内容。
     */
    public void onNotifyListener(String method, String notify) {
        if (webView != null) {
            String url = "javascript:" + method + "('" + notify + "')";
            webView.loadUrl(url);
        }
    }

    public void webRefresh() {
        if (webView != null) {
            webView.reload();
        }
    }
}
