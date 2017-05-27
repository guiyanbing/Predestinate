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

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单列表
 * Created by zhang on 2017/5/4.
 */

public class MyDefriendAct extends BaseActivity implements RequestComplete, ExListView.IXListViewListener {

    private CustomStatusListView customStatusListView;
    private ExListView exListView;

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
        customStatusListView = (CustomStatusListView) findViewById(R.id.myfriend_list);
        View mViewTop = LayoutInflater.from(this).inflate(R.layout.layout_margintop, null);
        exListView = customStatusListView.getExListView();
        exListView.setXListViewListener(this);
        exListView.setPullRefreshEnable(true);
        exListView.setPullLoadEnable(false);
        exListView.addHeaderView(mViewTop);
        adapter = new MyDefriendAdapter(this, data);
        exListView.setAdapter(adapter);

        customStatusListView.showLoading();
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
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());
                if (lightweightList != null && lightweightList.getUserInfos().size() != 0) {
                    if (data.size() != 0) {
                        data.clear();
                    }
                    data.addAll(lightweightList.getUserInfos());
                    if (data.size() < 10) {
                        exListView.setPullLoadEnable(false);
                    }
                    adapter.notifyDataSetChanged();
                    customStatusListView.showExListView();
                } else {
                    customStatusListView.showNoData(getString(R.string.my_defriend_nodata), "关闭", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            back();
                        }
                    });
                }
                exListView.stopRefresh();
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