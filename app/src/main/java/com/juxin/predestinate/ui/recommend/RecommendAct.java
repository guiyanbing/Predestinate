package com.juxin.predestinate.ui.recommend;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
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
import java.util.Arrays;
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
    List<RecommendPeople> recommendData = new ArrayList<>();
    HashMap<String, Object> post_param;
    private int page = 1;//页码，要请求的数据页数
    private boolean b_resetPage;//重置页码标记
    private ArrayList<Long> uidList = new ArrayList<>();// 推荐的人uid列表
    private long[] uids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommend_act);
        post_param = new HashMap<>();
        setBackView(getResources().getString(R.string.title_recommend));
        setTitleRight(getResources().getString(R.string.title_right_filter), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIShow.showRecommendFilterAct(RecommendAct.this);
            }
        });
        initView();
    }

    //获取推荐的人数据
    public void getRecommendData() {
        cv_common.showLoading();
        b_resetPage = true;
        ModuleMgr.getCommonMgr().sysRecommend(this, 1, post_param);
    }

    /**
     * 初始化 uid 列表
     */
    private void fillUidList() {
        if (recommendData.size() > 0) {
            uidList.clear();
            for (int i = 0; i < recommendData.size(); i++) {
                if (recommendData.size() <= 10) {
                    uidList.add(recommendData.get(i).getUid());
                } else if (recommendData.size() > 10) {
                    if (i == 10) {
                        break;
                    } else {
                        uidList.add(recommendData.get(i).getUid());
                    }
                }
            }
            if (uidList.size() > 0) {
                uids = new long[uidList.size()];
                for (int i = 0; i < uidList.size(); i++) {
                    uids[i] = uidList.get(i);
                }
            }
            getUserInfo(uids);
        }
    }

    /**
     * 获取轻量级用户信息
     */
    private void getUserInfo(long[] uids) {
        PLogger.d("uids1==" + Arrays.toString(uids));
        ModuleMgr.getCenterMgr().reqUserSimpleList(uids, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                UserInfoLightweightList lightweightList = (UserInfoLightweightList) response.getBaseData();
                if (lightweightList != null) {
                    ArrayList<UserInfoLightweight> lightweightLists = lightweightList.getLightweightLists();
                    if (null == lightweightLists) {
                        cv_common.showNetError();
                        return;
                    }
                    if (lightweightLists.size() == 0) {
                        return;
                    }
                    adapter.setRecommendList(RecommendAct.this, recommendData);
                    adapter.setList(lightweightLists);
                    cv_common.showXrecyclerView();
                }
            }
        });
    }

    private void initView() {
        cv_common = (CustomRecyclerView) findViewById(R.id.cv_common);
        recyclerView = cv_common.getXRecyclerView();
        adapter = new RecommendAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_recommend_item_space));
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.p1_recommend_item_space);
        recyclerView.addItemDecoration(recyclerView.new DividerItemDecoration(dividerDrawable));
        recyclerView.setLoadingListener(this);
        getRecommendData();
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
                            recommendData.clear();
                    } else {
                        page++;
                    }
                    recommendData.addAll(list);
                    //获取用户简略信息
                    fillUidList();
                }
                if (list.size() < 10) {
                    recyclerView.setLoadingMoreEnabled(false);
//                    exListView.addFooterView(divider_footer);
                } else {
                    recyclerView.setLoadingMoreEnabled(true);
                    PLogger.d("loadingMoreEnable");
                }
            } else {
                if (adapter.getList() != null && adapter.getList().size() > 0) {
                    PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
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
            if (data.getIntExtra("province", -1) != -1) {
                post_param.put("province", data.getIntExtra("province", -1));
                post_param.put("city", data.getIntExtra("city", -1));
            }
            if (data.getIntExtra("age_min", -1) != -1) {
                post_param.put("age_min", data.getIntExtra("age_min", -1));
            }
            if (data.getIntExtra("age_max", -1) != -1) {
                post_param.put("age_max", data.getIntExtra("age_max", -1));
            }
            if (bundle.getIntArray("tags") != null) {
                post_param.put("tags", bundle.getIntArray("tags"));
            }
            getRecommendData();
        }
    }

    @Override
    public void onRefresh() {
        b_resetPage = true;
        ModuleMgr.getCommonMgr().sysRecommend(this, 1, post_param);
        PLogger.d("onRefresh");
    }

    @Override
    public void onLoadMore() {
        b_resetPage = false;
        ModuleMgr.getCommonMgr().sysRecommend(this, page, post_param);
        PLogger.d("onLoadMore");
    }
}
