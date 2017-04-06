package com.juxin.predestinate.ui.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.recommend.TagInfo;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐的人筛选页面
 * Created YAO on 2017/4/6.
 */

public class RecommendFilterAct extends BaseActivity {
    CustomRecyclerView cv_tag, cv_chosen;
    RecyclerView rv_chosen, rv_tag;
    TextView tv_del;
    RecommendFilterAdapter chosenAdapter, tagAdapter;
    private List<TagInfo> listTag, listChosen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommendfilter_act);
        setTitle(getResources().getString(R.string.title_recommend_filter));
        setTitleRight("提交", R.color.title_right_commit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 筛选请求
            }
        });
        initView();
    }

    private void getTag() {
        listTag = new ArrayList<>();
        listChosen = new ArrayList<>();
        String tags[] = {"选择地区", "年龄", "开放的都市少女", "爽快", "可爱", "萝莉", "御姐"};
        for (int i = 0; i < tags.length; i++) {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setTagName(tags[i]);
            tagInfo.setTagMark(i);
            listTag.add(tagInfo);
        }
    }

    private void initRecycleView() {
        rv_chosen = cv_chosen.getRecyclerView();
        rv_tag = cv_tag.getRecyclerView();
        tagAdapter = new RecommendFilterAdapter(this, listTag, RecommendFilterAdapter.FILTER_TAG, new RecommendFilterAdapter.TagItemClickListener() {
            @Override
            public void itemClick(int position, View itemView) {
                MMToast.showShort("点击了tag ");
                listChosen.add(listTag.get(position));
                chosenAdapter.notifyDataSetChanged();
            }
        });
        chosenAdapter = new RecommendFilterAdapter(this, listChosen, RecommendFilterAdapter.FILTER_TAG_CHOSEN, new RecommendFilterAdapter.TagItemClickListener() {
            @Override
            public void itemClick(int position, View itemView) {
                MMToast.showShort("点击了chosen");
            }
        });
        rv_tag.setLayoutManager(new GridLayoutManager(this, 3));
        rv_chosen.setLayoutManager(new GridLayoutManager(this, 3));
        rv_tag.setAdapter(tagAdapter);
        rv_chosen.setAdapter(chosenAdapter);
        rv_tag.setItemAnimator(new DefaultItemAnimator());
        rv_chosen.setItemAnimator(new DefaultItemAnimator());
        rv_tag.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_recommend_item_space));
        rv_chosen.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_recommend_item_space));
        cv_tag.showRecyclerView();
        cv_chosen.showRecyclerView();
    }

    ;

    private void showChosenRecyclerView() {

    }

    private void initView() {
        getTag();
        cv_chosen = (CustomRecyclerView) findViewById(R.id.cv_tag_chosen);
        cv_tag = (CustomRecyclerView) findViewById(R.id.cv_tag);
        tv_del = (TextView) findViewById(R.id.tv_del);
        initRecycleView();

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除选择标签
            }
        });


    }
}
