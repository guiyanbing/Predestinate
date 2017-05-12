package com.juxin.predestinate.ui.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.baseui.WebPanel;
import com.juxin.predestinate.module.logic.invoke.Invoker;
import com.juxin.predestinate.module.util.PerformanceHelper;

/**
 * 网页容器fragment
 * Created by ZRP on 2017/4/21.
 */
@SuppressLint("ValidFragment")
public class WebFragment extends BaseFragment {

    private String title, url;
    private WebPanel webPanel;

    public WebFragment(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_main_web_fragment);

        setTitle(title);
        initView();

        return getContentView();
    }

    private void initView() {
        LinearLayout web_container = (LinearLayout) findViewById(R.id.web_container);

        String webUrl = url + "?resolution=" + (PerformanceHelper.isHighPerformance(App.context) ? "2" : "1");
        webPanel = new WebPanel(getActivity(), webUrl, true);
        web_container.removeAllViews();
        web_container.addView(webPanel.getContentView());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Invoker.getInstance().setWebView(webPanel == null ? null : webPanel.getWebView());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webPanel != null) webPanel.clearReference();
    }
}
