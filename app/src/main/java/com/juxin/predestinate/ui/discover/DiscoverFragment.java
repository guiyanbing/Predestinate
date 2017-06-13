package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.local.statistics.StatisticsDiscovery;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * 发现
 * Created by zhang on 2017/4/20.
 */

public class DiscoverFragment extends BaseFragment implements RequestComplete, View.OnClickListener, PObserver, ExListView.IXListViewListener {

    private static final int Look_All = 0; //查看全部
    private static final int Look_Near = 1; //只看附近的人

    private static final int Group_sayHai_Msg = 100; //群打招呼

    private ExListView exListView;
    private CustomStatusListView customStatusListView;

    private int page = 0;

    private List<UserInfoLightweight> infos = new ArrayList<>();
    private DiscoverAdapter adapter;

    private Button groupSayhiBtn;

    private boolean isNearPage = false;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.discover_fragment);

        initView();
        onRefresh(); //默认加载全部
        MsgMgr.getInstance().attach(this);
        return getContentView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    private void initView() {
        groupSayhiBtn = (Button) findViewById(R.id.discover_group_sayhi_btn);
        groupSayhiBtn.setOnClickListener(this);
        setGroupSayhiBtn(false);

        customStatusListView = (CustomStatusListView) findViewById(R.id.discover_content);
        View mViewTop = LayoutInflater.from(getContext()).inflate(R.layout.layout_margintop, null);
        exListView = customStatusListView.getExListView();
        exListView.setXListViewListener(this);
        exListView.setPullRefreshEnable(true);
        exListView.setPullLoadEnable(true);
        exListView.addHeaderView(mViewTop);
        adapter = new DiscoverAdapter(getActivity(), infos);
        adapter.setNear(false);
        exListView.setAdapter(adapter);
        exListView.setHeaderStr(null, null);
        exListView.setHeaderHintType(2);
        customStatusListView.showLoading();
    }


    @Override
    public void onRefresh() {
        exListView.setPullRefreshEnable(true);
        if (!isNearPage) {
            exListView.setPullLoadEnable(true);
            page = 1;
            ModuleMgr.getCommonMgr().getMainPage(page, 1, this);
        } else {
            exListView.setPullLoadEnable(false);
            getNearData();
        }

    }

    @Override
    public void onLoadMore() {
        page++;
        ModuleMgr.getCommonMgr().getMainPage(page, 0, this);
    }

    public void showDiscoverSelectDialog() {
        final DiscoverSelectDialog dialog = new DiscoverSelectDialog();
        dialog.setNear(isNearPage);
        dialog.setOnItemClick(new DiscoverSelectDialog.OnDialogItemClick() {
            @Override
            public void onDialogItemCilck(int position) {
                switch (position) {
                    case Look_All: //查看全部
                        isNearPage = false;
                        adapter.setNear(false);
                        setGroupSayhiBtn(false);
                        onRefresh();
                        //统计
                        StatisticsDiscovery.onClickLookAll();
                        dialog.dismiss();
                        break;
                    case Look_Near: //只看附近的人
                        isNearPage = true;
                        adapter.setNear(true);
                        onRefresh();
                        //统计
                        StatisticsDiscovery.onClickLookNear();
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.showDialog(getActivity());
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        exListView.stopRefresh();
        exListView.stopLoadMore();
        if (response.getUrlParam() == UrlParam.getMainPage) {
            setMainData(response);
        } else if (response.getUrlParam() == UrlParam.getNearUsers2) {
            setNearData(response);
        }
    }

    private void setMainData(HttpResponse response) {
        if (response.isOk()) {
            if (!response.isCache()) {
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());

                if (lightweightList.getUserInfos().size() != 0) {
                    //统计
                    if (page == 1) {
                        StatisticsDiscovery.onRecommendRefresh(lightweightList.getLightweightLists(), isNearPage);
                    }

                    //ref 如果是 true 并且请求的如果非第一页
                    //那么返回来的就是第一页 应该把之前的数据都清掉，把返回的数据作为第一页
                    if (page == 1 || lightweightList.isRef()) {
                        if (infos.size() != 0) {
                            infos.clear();
                        }
                        page = 1;
                    }

                    infos.addAll(lightweightList.getUserInfos());
                    if (infos.size() < 10) {
                        exListView.setPullLoadEnable(false);
                    }
                    adapter.notifyDataSetChanged();
                    customStatusListView.showExListView();
                } else {
                    if (page == 1) {
                        customStatusListView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customStatusListView.showLoading();
                                onRefresh();
                            }
                        });
                    } else {
                        exListView.setPullLoadEnable(false);
                    }
                }
            } else {
                if (page == 1) {
                    UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                    lightweightList.parseJson(response.getResponseString());
                    if (lightweightList != null && lightweightList.getUserInfos().size() != 0) {
                        if (infos.size() != 0) {
                            infos.clear();
                        }
                        infos.addAll(lightweightList.getUserInfos());
                        if (infos.size() < 10) {
                            exListView.setPullLoadEnable(false);
                        }
                        adapter.notifyDataSetChanged();
                        customStatusListView.showExListView();
                    }
                }
            }
        } else {
            if (infos.size() != 0) {
                if (infos.size() < 10) {
                    exListView.setPullLoadEnable(false);
                }
                adapter.notifyDataSetChanged();
                customStatusListView.showExListView();
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

    private void setNearData(HttpResponse response) {
        if (response.isOk()) {
            if (!response.isCache()) {
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());
                if (lightweightList.getUserInfos().size() != 0) {
                    if (infos.size() != 0) {
                        infos.clear();
                        //统计
                        StatisticsDiscovery.onRecommendRefresh(lightweightList.getLightweightLists(), isNearPage);
                    }
                    infos.addAll(lightweightList.getUserInfos());
                    if (infos.size() < 10) {
                        exListView.setPullLoadEnable(false);
                    }
                    adapter.notifyDataSetChanged();
                    customStatusListView.showExListView();
                    if (!ModuleMgr.getCenterMgr().isRobot(infos.get(0).getKf_id())) {
                        setGroupSayhiBtn(false);
                    } else {
                        setGroupSayhiBtn(true);
                    }
                } else {
                    customStatusListView.showNoData("暂无数据", "重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customStatusListView.showLoading();
                            getNearData();
                        }
                    });
                    groupSayhiBtn.setVisibility(GONE);
                }
            } else {
                UserInfoLightweightList lightweightList = new UserInfoLightweightList();
                lightweightList.parseJson(response.getResponseString());
                if (lightweightList.getUserInfos().size() != 0) {
                    if (infos.size() != 0) {
                        infos.clear();
                    }
                    infos.addAll(lightweightList.getUserInfos());
                    if (infos.size() < 10) {
                        exListView.setPullLoadEnable(false);
                    }
                    adapter.notifyDataSetChanged();
                    customStatusListView.showExListView();
                    if (!ModuleMgr.getCenterMgr().isRobot(infos.get(0).getKf_id())) {
                        setGroupSayhiBtn(false);
                    } else {
                        setGroupSayhiBtn(true);
                    }
                }
            }
        } else {
            if (infos.size() != 0) {
                adapter.notifyDataSetChanged();
                customStatusListView.showExListView();
                if (!ModuleMgr.getCenterMgr().isRobot(infos.get(0).getKf_id())) {
                    setGroupSayhiBtn(false);
                } else {
                    setGroupSayhiBtn(true);
                }
            } else {
                customStatusListView.showNoData("请求出错", "重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customStatusListView.showLoading();
                        getNearData();
                    }
                });
                setGroupSayhiBtn(false);
            }
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

    Map<Integer, Long> onclickData = null;

    private void doGroupSayHi() {
        if (isHasNoSayHi()) {
            if (ModuleMgr.getCenterMgr().isCanGroupSayHi(getActivity())) {
                LoadingDialog.show(getActivity());
                handler.sendEmptyMessage(Group_sayHai_Msg);
            }
        } else {
            PToast.showShort(getString(R.string.say_hi_group_refresh));
        }

        //统计
        onclickData = new HashMap<>();
        if (infos.size() != 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (!infos.get(i).isSayHello()) {
                    onclickData.put(i, infos.get(i).getUid());
                }
            }
        }
        StatisticsDiscovery.onNearGroupSayHello(onclickData);

    }

    /**
     *
     */
    private void doSayHi() {
        Iterator<UserInfoLightweight> userInfos = infos.iterator();
        while (userInfos.hasNext()) {
            final UserInfoLightweight infoLightweight = userInfos.next();
            if (!infoLightweight.isSayHello()) {
                ModuleMgr.getChatMgr().sendSayHelloMsg(String.valueOf(infoLightweight.getUid()), getString(R.string.say_hello_txt),
                        infoLightweight.getKf_id(),
                        ModuleMgr.getCenterMgr().isRobot(infoLightweight.getKf_id()) ?
                                Constant.SAY_HELLO_TYPE_NEAR : Constant.SAY_HELLO_TYPE_SIMPLE, null);
                notifyAdapter(infoLightweight.getUid());
                handler.sendEmptyMessage(Group_sayHai_Msg);
                break;
            }
        }

        if (!isHasNoSayHi()) {
            LoadingDialog.closeLoadingDialog();
        }
    }

    /**
     * 刷新打招呼的状态
     *
     * @param uid
     */
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

    /**
     * 当前列表内有没有还未打招呼的人
     *
     * @return true有 false没有
     */
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
            case MsgType.MT_Say_Hello_Notice: //一键打招呼完成后 当前列表内有一键打招呼的人的数据的时候 更新列表
                List<UserInfoLightweight> data = (List<UserInfoLightweight>) value;
                for (int i = 0; i < data.size(); i++) {
                    notifyAdapter(data.get(i).getUid());
                }
                break;
            case MsgType.MT_Say_HI_Notice:
                long uid = (long) value;
                notifyAdapter(uid);
                break;
            default:
                break;
        }
    }

    /**
     * 是否显示群打招呼
     *
     * @param isCanShow 状态更新是否显示（该参数控制在数据未加载的时候 不显示群打招呼） 默认传false
     */
    private void setGroupSayhiBtn(boolean isCanShow) {
        //我是VIP的时候不显示
        if (ModuleMgr.getCenterMgr().getMyInfo().isVip() || !ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
            groupSayhiBtn.setVisibility(View.GONE);
        } else {  //非VIP附近的人显示
            if (!isNearPage) {
                groupSayhiBtn.setVisibility(View.GONE);
            } else {
                if (isCanShow) {
                    groupSayhiBtn.setVisibility(View.VISIBLE);
                } else {
                    groupSayhiBtn.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }

    /**
     * 刷新首页
     */
    public void refreshList() {
        onRefresh();
        if (adapter != null && adapter.getList() != null && adapter.getList().size() != 0) {
            exListView.setSelection(0);
        }
    }


}
