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
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.juxin.predestinate.module.logic.application.App.getActivity;

/**
 * 我的好友
 * Created by zhang on 2017/5/4.
 */

public class MyFriendsAct extends BaseActivity implements XRecyclerView.LoadingListener, RequestComplete {

    private CustomRecyclerView customRecyclerView;
    private XRecyclerView xRecyclerView;

    private MyFriendsAdapter adapter;
    private List<UserInfoLightweight> data = new ArrayList<>();

    private int page = 1;

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
        setTitle(getResources().getString(R.string.my_friend_title));
        setTitleRight(getResources().getString(R.string.my_friend_righ_title), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIShow.showMyDefriends(MyFriendsAct.this);
            }
        });
    }

    private void initView() {
        customRecyclerView = (CustomRecyclerView) findViewById(R.id.myfriend_list);
        xRecyclerView = customRecyclerView.getXRecyclerView();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);

        adapter = new MyFriendsAdapter(this);
        adapter.setList(data);
        xRecyclerView.setAdapter(adapter);

        customRecyclerView.showLoading();
    }

    @Override
    public void onRefresh() {
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        page = 1;
        ModuleMgr.getCommonMgr().getMyFriends(page, this);
    }

    @Override
    public void onLoadMore() {
        page++;
        ModuleMgr.getCommonMgr().getMyFriends(page, this);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            if (!response.isCache()) {
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJsonFriends(response.getResponseString());

                if (lightweightList != null && lightweightList.getUserInfos().size() != 0) {
                    if (page == 1) {
                        if (data.size() != 0) {
                            data.clear();
                        }
                    }
                    data.addAll(lightweightList.getUserInfos());
                    customRecyclerView.showXrecyclerView();
                    adapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        customRecyclerView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customRecyclerView.showLoading();
                                onRefresh();
                            }
                        });
                    } else {
                        xRecyclerView.setLoadingMoreEnabled(false);
                    }
                }

                if (page == 1) {
                    xRecyclerView.refreshComplete();
                } else {
                    xRecyclerView.loadMoreComplete();
                }
            }
        } else {
            customRecyclerView.showNoData("请求出错", "重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customRecyclerView.showLoading();
                    onRefresh();
                }
            });
        }
    }

}
