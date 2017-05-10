package com.juxin.predestinate.ui.wode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.juxin.library.controls.smarttablayout.PagerItem;
import com.juxin.library.controls.smarttablayout.SmartTabLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.ui.wode.adapter.ViewGroupPagerAdapter;
import com.juxin.predestinate.ui.wode.util.AttentionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注
 * Created by zm on 2017/5/4
 */
public class MyAttentionAct extends BaseActivity{

    private SmartTabLayout stlTitles;
    private ViewPager vpViewChange;

    private List<PagerItem> listViews;//pagerItem集合
    private List<BaseViewPanel> panls = new ArrayList<>(); // Tab页面列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_my_attention_act);
        initData();
        initView();
    }

    private void initData() {
        AttentionUtil.initUserDetails();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.my_attention));
        stlTitles = (SmartTabLayout) findViewById(R.id.my_attention_stl_titles);
        vpViewChange = (ViewPager) findViewById(R.id.my_attention_vPager);
        initViewsList();
        initViewPager();
        ((LinearLayout)stlTitles.getTabStrip()).setGravity(Gravity.CENTER_HORIZONTAL);//标题居中
        stlTitles.setCustomTabView(R.layout.f1_custom_table_view, R.id.tv_left_tab);//设置自定义标题
        stlTitles.setViewPager(vpViewChange);//设置viewpager
    }
    //添加两个panel
    private void initViewsList() {
        panls.add(new AttentionMePanel(this));
        panls.add(new MyAttentionPanel(this));
        listViews = new ArrayList<>();
        listViews.add(new PagerItem("关注我的", panls.get(0).getContentView()));
        listViews.add(new PagerItem("我关注的", panls.get(1).getContentView()));
    }

    private void initViewPager() {
        vpViewChange.setAdapter(new ViewGroupPagerAdapter(listViews));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (panls.size()==2&&panls.get(0)!=null&&panls.get(1)!=null){
            ((AttentionMePanel)panls.get(0)).reFreshUI();
            ((MyAttentionPanel)panls.get(1)).reFreshUI();
        }
    }
}