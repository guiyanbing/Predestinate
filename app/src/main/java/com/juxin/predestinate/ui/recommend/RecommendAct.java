package com.juxin.predestinate.ui.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐的人
 * Created YAO on 2017/3/30.
 */

public class RecommendAct extends BaseActivity {

    CustomRecyclerView customRecyclerView;
    XRecyclerView recyclerView;
    RecommendAdapter recommendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommend_act);
        setTitle(getResources().getString(R.string.title_recommend));
        setTitleRight(getResources().getString(R.string.title_right_filter), R.color.title_right_commit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIShow.showRecommendFilterAct(RecommendAct.this);
            }
        });
        initView();
    }

    private void initView() {
        customRecyclerView = (CustomRecyclerView) findViewById(R.id.cv_common);
        recyclerView = customRecyclerView.getXRecyclerView();
        List<String> list = new ArrayList<>();
        list.add("小明");
        list.add("晓云");
        list.add("佐助");
        recommendAdapter = new RecommendAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recommendAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_recommend_item_space));
        customRecyclerView.showXrecyclerView();
    }
}
