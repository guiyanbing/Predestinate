package com.juxin.predestinate.ui.mail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.SysNoticeMessage;
import com.juxin.predestinate.module.local.chat.utils.SortList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的钻石页面
 * Created by zm on 2017/4/19
 */
public class SysMessAct extends BaseActivity implements ExListView.IXListViewListener, ChatMsgInterface.ChatMsgListener {

    //控件
    private CustomStatusListView cslList;
    private ExListView listDatas;
    //数据
    private SysMessAdapter mSysMessAdapter;
    private List<SysNoticeMessage> sysMess;
    //变量
    private int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_sys_mess_act);
        initView();
    }

    private void initView() {
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.sys_mess_act));
        cslList = (CustomStatusListView) findViewById(R.id.sys_mess_list);
        listDatas = cslList.getExListView();
        mSysMessAdapter = new SysMessAdapter(this, null);
        listDatas.setAdapter(mSysMessAdapter);
        View listview_footer = LayoutInflater.from(this).inflate(R.layout.common_footer_distance, null);
        listDatas.addFooterView(listview_footer);
        listDatas.setXListViewListener(this);
        initData();
    }

    private void initData() {
        loadData();
        mSysMessAdapter.setList(sysMess);
        cslList.showLoading();
    }

    private void loadData() {
        // TODO: 2017/6/14 注意正确的rx调用方式
        ModuleMgr.getChatMgr().attachChatListener("9998", this);
        ModuleMgr.getChatMgr().getRecentlyChat("", "9998", 0)
                .observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BaseMessage>>() {
                    @Override
                    public void onCompleted() {
                        cslList.showExListView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        cslList.showExListView();
                    }

                    @Override
                    public void onNext(final List<BaseMessage> baseMessages) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SortList.sortTimeListView(baseMessages);//排序
                                List<SysNoticeMessage> listTemp = new ArrayList<>();

                                if (baseMessages.size() < 20) {
                                    listDatas.setPullRefreshEnable(false);
                                }
                                if (baseMessages.size() > 0) {
                                    for (BaseMessage baseMessage : baseMessages) {
                                        if (baseMessage instanceof SysNoticeMessage && baseMessage != null) {
                                            listTemp.add((SysNoticeMessage) baseMessage);
                                        }
                                    }
                                }

                                mSysMessAdapter.setList(listTemp);
                                listDatas.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        cslList.showExListView();
                                        moveToBottom();
                                    }
                                },800);
                            }
                        });
                    }
                });
    }

    /**
     * 移动到聊天列表的最后一个。
     */
    public void moveToBottom() {
        if (mSysMessAdapter.getCount() > 0)
            listDatas.setSelection(ListView.FOCUS_DOWN);
    }

    @Override
    public void onRefresh() {
        // TODO: 2017/6/14 注意正确的rx调用方式
        // 这里是加载更多信息的。
        ModuleMgr.getChatMgr().getHistoryChat("", "9998", ++page)
                .observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BaseMessage>>() {
                    @Override
                    public void onCompleted() {
                        listDatas.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listDatas.stopRefresh();
                    }

                    @Override
                    public void onNext(final List<BaseMessage> baseMessages) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SortList.sortTimeListView(baseMessages);// 排序
                                PLogger.printObject(baseMessages);
                                listDatas.stopRefresh();
                                if (baseMessages.size() > 0) {
                                    if (baseMessages.size() < 20) {
                                        listDatas.setPullRefreshEnable(false);
                                    }

                                    List<SysNoticeMessage> listTemp = new ArrayList<>();

                                    for (BaseMessage baseMessage : baseMessages) {
                                        if (baseMessage instanceof SysNoticeMessage && baseMessage != null) {
                                            listTemp.add((SysNoticeMessage) baseMessage);
                                        }
                                    }

                                    if (mSysMessAdapter.getList() != null) {
                                        listTemp.addAll(mSysMessAdapter.getList());
                                    }

                                    mSysMessAdapter.setList(listTemp);
                                    listDatas.setSelection(baseMessages.size());
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onChatUpdate(boolean ret, BaseMessage message) {
        if (message == null)
            return;
        if (!(message instanceof SysNoticeMessage))
            return;
        if (mSysMessAdapter.getList() == null)
            return;
        if (message.getTime() == 0) {
            message.setTime(ModuleMgr.getAppMgr().getTime());
        }
        mSysMessAdapter.getList().add((SysNoticeMessage) message);
        mSysMessAdapter.notifyDataSetChanged();
        moveToBottom();
    }

    @Override
    protected void onDestroy() {
        ModuleMgr.getChatMgr().detachChatListener(this);
        super.onDestroy();
    }
}