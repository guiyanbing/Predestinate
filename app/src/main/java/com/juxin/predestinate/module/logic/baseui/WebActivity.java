package com.juxin.predestinate.module.logic.baseui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;

/**
 * 通用网页
 * Created by ZRP on 2016/12/9.
 */
public class WebActivity extends BaseActivity {

    private WebPanel webPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isCanBack(false);
        int type = getIntent().getIntExtra("type", 1);
        String url = getIntent().getStringExtra("url");
        if (type == 2) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_web_activity);
        View base_title = findViewById(R.id.base_title);
        base_title.setVisibility(type == 2 ? View.GONE : View.VISIBLE);
        setBackView();

        LinearLayout web_container = (LinearLayout) findViewById(R.id.web_container);
        webPanel = new WebPanel(this, url, true);
        webPanel.setWebListener(new WebPanel.WebListener() {
            @Override
            public void onTitle(String title) {
                setTitle(title);
            }

            @Override
            public void onLoadFinish(WebPanel.WebLoadStatus loadStatus) {
            }
        });
        web_container.addView(webPanel.getContentView());
    }

    @Override
    public void onBackPressed() {
        if (webPanel != null && webPanel.getWebView() != null && webPanel.getWebView().canGoBack()) {
            webPanel.getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 关闭loading，显示webView
     */
    public void hideLoading() {
        if (webPanel != null) webPanel.hideLoading();
    }
}
