package com.juxin.predestinate.module.logic.baseui;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_web_activity);
        String url = getIntent().getStringExtra("url");
        setBackView();

        LinearLayout web_container = (LinearLayout) findViewById(R.id.web_container);
        webPanel = new WebPanel(this, url);
        webPanel.setTitleListener(new WebPanel.TitleListener() {
            @Override
            public void onTitle(String title) {
                setTitle(title);
            }
        });
        web_container.addView(webPanel.getContentView());
    }
}
