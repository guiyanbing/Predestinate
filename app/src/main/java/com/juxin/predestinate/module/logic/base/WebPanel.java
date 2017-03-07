package com.juxin.predestinate.module.logic.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.juxin.library.utils.NetworkUtils;
import com.juxin.library.view.BasePanel;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.library.view.DownloadProgressView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.invoke.WebAppInterface;

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
        });
        webView.addJavascriptInterface(new WebAppInterface(getContext(), webView), "aiai");
        webView.loadUrl(url);
    }

    /**
     * CookieManager会将这个Cookie存入该应用程序/data/data/databases/目录下的webviewCookiesChromium.db数据库的cookies表中
     * 需要在当前用户退出登录的时候进行清除
     */
    private void synCookies() {
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().setCookie(url, Constant.cookieString);
        CookieSyncManager.getInstance().sync();
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
