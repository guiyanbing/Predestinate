package com.juxin.predestinate.ui.web;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.baseui.WebPanel;
import com.juxin.predestinate.module.logic.config.Hosts;
import com.juxin.predestinate.module.logic.invoke.Invoker;
import com.juxin.predestinate.module.util.WebUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 风云榜fragment
 * Created by ZRP on 2017/4/30.
 */
public class RankFragment extends BaseFragment implements View.OnClickListener {

    //jcmd预定义的值
    private static final int TYPE_THIS_WEEK = 101;
    private static final int TYPE_LAST_WEEK = 102;
    private static final String TYPE_KEY = "type";

    private Map<String, Object> typeMap = new HashMap<>();

    private RadioButton this_week, last_week;
    private WebPanel webPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_main_web_fragment);

        initTitle();
        initView();

        return getContentView();
    }

    private void initTitle() {
        setTitle(getResources().getString(R.string.main_btn_rank));
        View rank_title = LayoutInflater.from(getActivity()).inflate(R.layout.f1_main_rank_title, null);
        setTitleCenterContainer(rank_title);

        this_week = (RadioButton) rank_title.findViewById(R.id.this_week);
        last_week = (RadioButton) rank_title.findViewById(R.id.last_week);

        this_week.setOnClickListener(this);
        last_week.setOnClickListener(this);

        onChecked(TYPE_THIS_WEEK);
    }

    private void initView() {
        LinearLayout web_container = (LinearLayout) findViewById(R.id.web_container);

        webPanel = new WebPanel(getActivity(), WebUtil.jointUrl(Hosts.H5_RANKING), true);
        web_container.removeAllViews();
        web_container.addView(webPanel.getContentView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.this_week://通过jcmd打开本周排行榜
                onChecked(TYPE_THIS_WEEK);
                break;
            case R.id.last_week://通过jcmd打开上周排行榜
                onChecked(TYPE_LAST_WEEK);
                break;
        }
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

    /**
     * 设置选中
     *
     * @param type #TYPE_THIS_WEEK/TYPE_LAST_WEEK
     */
    private void onChecked(int type) {
        if (type == TYPE_THIS_WEEK) {
            this_week.setChecked(true);
            last_week.setChecked(false);
        } else if (type == TYPE_LAST_WEEK) {
            this_week.setChecked(false);
            last_week.setChecked(true);
        }
        typeMap.put(TYPE_KEY, type);
        Invoker.getInstance().doInJS(Invoker.JSCMD_ranking_btn_click, typeMap);
    }
}
