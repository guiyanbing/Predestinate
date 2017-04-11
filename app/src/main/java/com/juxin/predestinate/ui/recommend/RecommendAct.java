package com.juxin.predestinate.ui.recommend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.recommend.RecommendPeople;
import com.juxin.predestinate.bean.recommend.RecommendPeopleList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.CommonUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 推荐的人
 * Created YAO on 2017/3/30.
 */

public class RecommendAct extends BaseActivity implements RequestComplete, XRecyclerView.LoadingListener {
    CustomRecyclerView cv_common;
    XRecyclerView recyclerView;
    RecommendAdapter adapter;
    List<RecommendPeople> data = new ArrayList<>();
    HashMap<String, Object> post_param;
    private int page = 1;//页码，要请求的数据页数
    private boolean b_resetPage;//重置页码标记

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommend_act);
        post_param= new HashMap<>();
        setTitle(getResources().getString(R.string.title_recommend));
        setTitleRight(getResources().getString(R.string.title_right_filter), R.color.title_right_commit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIShow.showRecommendFilterAct(RecommendAct.this);
            }
        });
        initView();
    }

    //获取数据
    public void getHttpData() {
        cv_common.showLoading();
        b_resetPage = true;
        ModuleMgr.getCommonMgr().sysRecommend(this, 1, post_param);
    }

    private void initView() {
        cv_common = (CustomRecyclerView) findViewById(R.id.cv_common);
        recyclerView = cv_common.getXRecyclerView();
//        List<String> list = new ArrayList<>();
//        list.add("小明");
//        list.add("晓云");
//        list.add("佐助");
        adapter = new RecommendAdapter(this, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_recommend_item_space));
        getHttpData();
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
        if (response.getUrlParam() == UrlParam.sysRecommend) {
            if (response.isOk()) {
                RecommendPeopleList recommendPeopleList = (RecommendPeopleList) response.getBaseData();
                List<RecommendPeople> list = recommendPeopleList.getRecommendPeopleList();
                if (list.size() <= 0) {
                    if (page == 1) {
                        cv_common.showNoData();
                    }
                } else {
                    if (b_resetPage) {
                        page = 2;
                        if (list.size() > 0)
                            data.clear();
                    } else {
                        page++;
                    }
                    data.addAll(list);
                    adapter.setList(data);
                    cv_common.showXrecyclerView();
                }
                if (list.size() < 20) {
                    recyclerView.setLoadingMoreEnabled(false);
//                    exListView.addFooterView(divider_footer);
                } else {
                    recyclerView.setLoadingMoreEnabled(true);
                }
            } else {
                if (adapter.getList() != null && adapter.getList().size() > 0) {
                    MMToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
                } else {
                    cv_common.showNetError("点击刷新", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cv_common.showLoading();
                            ModuleMgr.getCommonMgr().sysRecommend(RecommendAct.this, 1, post_param);
                        }
                    });
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            post_param.clear();
            Bundle bundle = data.getExtras();
            if (data.getIntExtra("province", -1) != -1){
                post_param.put("province",data.getIntExtra("province", -1));
                post_param.put("city",data.getIntExtra("city", -1));
            }else if(data.getIntExtra("age_min", -1) != -1){
                post_param.put("age_min",data.getIntExtra("age_min", -1));
                post_param.put("age_max",data.getIntExtra("age_max", -1));
            }else if(bundle.getIntArray("tags")!=null){
                post_param.put("tags",bundle.getIntArray("tags"));
            }
            getHttpData();
        }
    }

    @Override
    public void onRefresh() {
        b_resetPage = true;
        ModuleMgr.getCommonMgr().sysRecommend(this, 1, post_param);
    }

    @Override
    public void onLoadMore() {
        b_resetPage = false;
        ModuleMgr.getCommonMgr().sysRecommend(this, page, post_param);
    }
}
