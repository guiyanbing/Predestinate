package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 发现
 * Created by zhang on 2017/4/20.
 */

public class DiscoverFragment extends BaseFragment implements XRecyclerView.LoadingListener, RequestComplete, View.OnClickListener, PObserver {

    private static final int Look_All = 0; //查看全部
    private static final int Look_Near = 1; //只看附近的人

    private static final int Group_sayHai_Msg = 100; //只看附近的人

    private XRecyclerView xRecyclerView;
    private CustomRecyclerView customRecyclerView;

    private int page = 0;

    private List<UserInfoLightweight> infos = new ArrayList<>();
    private DiscoverAdapter adapter;

    private Button groupSayhiBtn;


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
        MsgMgr.getInstance().attach(this);
    }


    private void initView() {
        groupSayhiBtn = (Button) findViewById(R.id.discover_group_sayhi_btn);
        groupSayhiBtn.setOnClickListener(this);

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
                    groupSayhiBtn.setVisibility(View.VISIBLE);
                } else {
                    if (page == 1) {
                        customRecyclerView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customRecyclerView.showLoading();
                                onRefresh();
                            }
                        });
                        groupSayhiBtn.setVisibility(View.GONE);
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
            groupSayhiBtn.setVisibility(View.GONE);
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
                    groupSayhiBtn.setVisibility(View.VISIBLE);
                } else {
                    customRecyclerView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customRecyclerView.showLoading();
                            getNearData();
                        }
                    });
                    groupSayhiBtn.setVisibility(View.GONE);
                }
                xRecyclerView.refreshComplete();
            }
        } else {
            customRecyclerView.showNoData("请求出错", "重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customRecyclerView.showLoading();
                    getNearData();
                }
            });
            groupSayhiBtn.setVisibility(View.GONE);
        }
    }

    private void getNearData() {
        ModuleMgr.getCommonMgr().getNearUsers2(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discover_group_sayhi_btn:
                doGroupSayHi();
                break;
        }
    }

    private void doGroupSayHi() {
        if (ModuleMgr.getCenterMgr().isCanGroupSayHi(getActivity())) {
            if (isHasNoSayHi()) {
                handler.sendEmptyMessage(Group_sayHai_Msg);
            } else {
                PToast.showShort(getString(R.string.say_hi_group_refresh));
            }
        }
    }

    private void doSayHi() {
        Iterator<UserInfoLightweight> userInfos = infos.iterator();
        while (userInfos.hasNext()) {
            final UserInfoLightweight infoLightweight = userInfos.next();
            if (!infoLightweight.isSayHello()) {
                ModuleMgr.getChatMgr().sendSayHelloMsg(String.valueOf(infoLightweight.getUid()), getString(R.string.say_hello_txt),
                        infoLightweight.getKf_id(),
                        ModuleMgr.getCenterMgr().isRobot(infoLightweight.getKf_id()) ?
                                Constant.SAY_HELLO_TYPE_NEAR : Constant.SAY_HELLO_TYPE_SIMPLE, new IMProxy.SendCallBack() {
                            @Override
                            public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                                notifyAdapter(infoLightweight.getUid());
                                handler.sendEmptyMessage(Group_sayHai_Msg);
                            }

                            @Override
                            public void onSendFailed(NetData data) {
                                handler.sendEmptyMessage(Group_sayHai_Msg);
                            }
                        });
                break;
            }
        }
    }

    private void notifyAdapter(long uid) {
        for (UserInfoLightweight info : infos) {
            if (info.getUid() == uid) {
                info.setSayHello(true);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Group_sayHai_Msg:
                    doSayHi();
                    break;
            }
        }
    };

    private boolean isHasNoSayHi() {
        if (infos.size() != 0) {
            for (UserInfoLightweight info : infos) {
                if (!info.isSayHello()) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_Say_Hello_Notice:
                List<UserInfoLightweight> data = (List<UserInfoLightweight>) value;
                for (int i = 0; i < data.size(); i++) {
                    notifyAdapter(data.get(i).getUid());
                }
                break;
            default:
                break;
        }
    }
}
