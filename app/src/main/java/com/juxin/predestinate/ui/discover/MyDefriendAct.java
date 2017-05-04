package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.juxin.predestinate.module.logic.application.App.getActivity;

/**
 * 黑名单列表
 * Created by zhang on 2017/5/4.
 */

public class MyDefriendAct extends BaseActivity implements XRecyclerView.LoadingListener, RequestComplete {

    private CustomRecyclerView customRecyclerView;
    private XRecyclerView xRecyclerView;

    private MyDefriendAdapter adapter;
    private List<UserInfoLightweight> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_my_friend_act);
        initTitle();
        initView();
        onRefresh();
    }

    private void initTitle() {
        setBackView(R.id.base_title_back);
        setTitle(getResources().getString(R.string.my_defriend_title));
    }

    private void initView() {
        customRecyclerView = (CustomRecyclerView) findViewById(R.id.myfriend_list);
        xRecyclerView = customRecyclerView.getXRecyclerView();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);


        adapter = new MyDefriendAdapter(this);
        adapter.setList(data);
        xRecyclerView.setAdapter(adapter);

        customRecyclerView.showLoading();
    }

    @Override
    public void onRefresh() {
        ModuleMgr.getCommonMgr().getMyDefriends(this);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            if (!response.isCache()) {
//                UserInfoLightweightList lightweightList = (UserInfoLightweightList) response.getBaseData();
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());
                if (lightweightList != null && lightweightList.getUserInfos().size() != 0) {
                    if (data.size() != 0) {
                        data.clear();
                    }
                    data.addAll(lightweightList.getUserInfos());
                    adapter.notifyDataSetChanged();
                    customRecyclerView.showXrecyclerView();
                } else {
                    customRecyclerView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onRefresh();
                        }
                    });
                }
                xRecyclerView.refreshComplete();
            }
        } else {
            customRecyclerView.showNoData("请求出错", "重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefresh();
                }
            });
        }
    }
}