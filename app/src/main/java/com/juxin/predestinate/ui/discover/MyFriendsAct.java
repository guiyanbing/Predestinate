package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友
 * Created by zhang on 2017/5/4.
 */

public class MyFriendsAct extends BaseActivity implements RequestComplete, ExListView.IXListViewListener {

    private CustomStatusListView customStatusListView;
    private ExListView exListView;

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
        customStatusListView = (CustomStatusListView) findViewById(R.id.myfriend_list);
        View mViewTop = LayoutInflater.from(this).inflate(R.layout.layout_margintop, null);
        exListView = customStatusListView.getExListView();
        exListView.setXListViewListener(this);
        exListView.setPullRefreshEnable(true);
        exListView.setPullLoadEnable(true);
        exListView.addHeaderView(mViewTop);
        adapter = new MyFriendsAdapter(this, data);
        exListView.setAdapter(adapter);

        customStatusListView.showLoading();
    }

    @Override
    public void onRefresh() {
        exListView.setPullRefreshEnable(true);
        exListView.setPullLoadEnable(true);
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
                    if (data.size() < 10) {
                        exListView.setPullLoadEnable(false);
                    }
                    adapter.notifyDataSetChanged();
                    customStatusListView.showExListView();
                } else {
                    if (page == 1) {
                        customStatusListView.showNoData(getString(R.string.my_friend_nodata), "去添加", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                back();
                            }
                        });
                    } else {
                        exListView.setPullLoadEnable(false);
                    }
                }

                if (page == 1) {
                    exListView.stopRefresh();
                } else {
                    exListView.stopLoadMore();
                }
            }
        } else {
            customStatusListView.showNoData("请求出错", "重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customStatusListView.showLoading();
                    onRefresh();
                }
            });
        }
    }

}
