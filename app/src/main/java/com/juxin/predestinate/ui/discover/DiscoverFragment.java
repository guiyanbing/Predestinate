package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现
 * Created by zhang on 2017/4/20.
 */

public class DiscoverFragment extends BaseFragment implements XRecyclerView.LoadingListener, RequestComplete {

    private static final int Look_All = 0; //查看全部
    private static final int Look_Near = 1; //只看附近的人

    private XRecyclerView xRecyclerView;
    private CustomRecyclerView customRecyclerView;

    private int page = 0;

    private List<UserInfoLightweight> infos = new ArrayList<>();
    private DiscoverAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.discover_fragment);
        setTopView();
        initView();
        onRefresh(); //默认加载全部
        return getContentView();
    }

    private void setTopView() {
        setTitle(getString(R.string.discover_title));
        setTitleRightImg(R.drawable.f1_discover_select_ico, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscoverSelectDialog();
            }
        });
    }


    private void initView() {
        customRecyclerView = (CustomRecyclerView) findViewById(R.id.discover_content);
        xRecyclerView = customRecyclerView.getXRecyclerView();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter = new DiscoverAdapter(getActivity());
        adapter.setList(infos);
        xRecyclerView.setAdapter(adapter);
        customRecyclerView.showLoading();
    }


    @Override
    public void onRefresh() {
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        page = 1;
        ModuleMgr.getCommonMgr().getMainPage(page, 1, this);
    }

    @Override
    public void onLoadMore() {
        page++;
        ModuleMgr.getCommonMgr().getMainPage(page, 0, this);
    }

    private void showDiscoverSelectDialog() {
        final DiscoverSelectDialog dialog = new DiscoverSelectDialog();
        dialog.setOnItemClick(new DiscoverSelectDialog.OnDialogItemClick() {
            @Override
            public void onDialogItemCilck(AdapterView<?> parent, View view, int position) {
                switch (position) {
                    case Look_All: //查看全部
                        onRefresh();
                        dialog.dismiss();
                        break;
                    case Look_Near: //只看附近的人
                        getNearData();
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.showDialog(getActivity());
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.getMainPage) {
            setMainData(response);
        } else if (response.getUrlParam() == UrlParam.getNearUsers2) {
            setNearData(response);
        }
    }

    private void setMainData(HttpResponse response) {
        if (response.isOk()) {
            if (!response.isCache()) {
//                UserInfoLightweightList lightweightList = (UserInfoLightweightList) response.getBaseData();
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());

                if (lightweightList != null && lightweightList.getUserInfos().size() != 0) {
                    if (page == 1) {
                        if (infos.size() != 0) {
                            infos.clear();
                        }
                    }
                    infos.addAll(lightweightList.getUserInfos());
                    customRecyclerView.showXrecyclerView();
                    adapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        customRecyclerView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
                    onRefresh();
                }
            });
        }
    }

    private void setNearData(HttpResponse response) {
        if (response.isOk()) {
            if (!response.isCache()) {
//                UserInfoLightweightList lightweightList = (UserInfoLightweightList) response.getBaseData();
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());
                if (lightweightList != null && lightweightList.getUserInfos().size() != 0) {
                    if (infos.size() != 0) {
                        infos.clear();
                    }
                    infos.addAll(lightweightList.getUserInfos());
                    adapter.notifyDataSetChanged();
                    customRecyclerView.showXrecyclerView();
                } else {
                    customRecyclerView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getNearData();
                        }
                    });
                }
                xRecyclerView.refreshComplete();
            }
        } else {
            customRecyclerView.showNoData("请求出错", "重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getNearData();
                }
            });
        }
    }

    private void getNearData() {
        ModuleMgr.getCommonMgr().getNearUsers2(this);
    }

}
