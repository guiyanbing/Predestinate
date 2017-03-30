package com.juxin.predestinate.ui.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐的人
 * Created YAO on 2017/3/30.
 */

public class RecommendAct extends BaseActivity {

    CustomRecyclerView customRecyclerView;
    XRecyclerView xRecyclerView;
    RecommendAdapter recommendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommend_act);
        setBackView("推荐的人");
        initView();
    }

    private void initView() {
//        customRecyclerView = (CustomRecyclerView) findViewById(R.id.cv_common);
//        xRecyclerView = customRecyclerView.getXRecyclerView();
//        List<String> list = new ArrayList<>();
//        list.add("小明");
//        list.add("晓云");
//        list.add("佐助");
//        recommendAdapter = new RecommendAdapter(this,list);
//        xRecyclerView.setAdapter(recommendAdapter);
//        customRecyclerView.showXrecyclerView();
    }
}
