package com.juxin.predestinate.ui.web;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.baseui.WebPanel;
import com.juxin.predestinate.module.util.PerformanceHelper;

/**
 * 网页容器fragment：暂为书城
 * Created by ZRP on 2017/4/21.
 */
public class WebFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_main_web_fragment);

        setTitle("书城");
        initView();

        return getContentView();
    }

    private void initView() {
        LinearLayout web_container = (LinearLayout) findViewById(R.id.web_container);

        String webUrl = ModuleMgr.getCommonMgr().getCommonConfig().getEntrance_url() +
                "?resolution=" + (PerformanceHelper.isHighPerformance(App.context) ? "2" : "1");
        WebPanel webPanel = new WebPanel(getActivity(), webUrl, true);
        web_container.removeAllViews();
        web_container.addView(webPanel.getContentView());
    }
}
