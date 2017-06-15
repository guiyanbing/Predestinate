package com.juxin.predestinate.ui.mail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.SysNoticeMessage;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钻石页面
 * Created by zm on 2017/4/19
 */
public class SysMessAct extends BaseActivity implements ExListView.IXListViewListener {

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
        testData();
        mSysMessAdapter.setList(sysMess);
        cslList.showExListView();
    }

    private void loadData() {
        // TODO: 2017/6/14 注意正确的rx调用方式
        /*Observable<List<BaseMessage>> observable = ModuleMgr.getChatMgr().getRecentlyChat(channelId, whisperId, 0);
        observable.compose(RxUtil.<List<BaseMessage>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
                .subscribe(new Action1<List<BaseMessage>>() {
                    @Override
                    public void call(List<BaseMessage> baseMessages) {
                        SortList.sortListView(baseMessages);//排序
                        List<BaseMessage> listTemp = new ArrayList<>();

                        if (baseMessages.size() < 20) {
                            listDatas.setPullRefreshEnable(false);
                        }
                        if (baseMessages.size() > 0) {
                            for (BaseMessage baseMessage : baseMessages) {
                                if (isShowMsg(baseMessage)) {
                                    listTemp.add();
                                }
                            }
                        }

                        mSysMessAdapter.setList(listTemp);
                        moveToBottom();
                    }
                });*/
    }

    /**
     * 移动到聊天列表的最后一个。
     */
    public void moveToBottom() {
        if (mSysMessAdapter.getCount() > 0)
            listDatas.setSelection(mSysMessAdapter.getCount() - 1);
    }

    @Override
    public void onRefresh() {
        // TODO: 2017/6/14 注意正确的rx调用方式
        // 这里是加载更多信息的。
       /* Observable<List<BaseMessage>> observable = ModuleMgr.getChatMgr().getHistoryChat(channelId, whisperId, ++page);
        observable.compose(RxUtil.<List<BaseMessage>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
                .subscribe(new Action1<List<BaseMessage>>() {
                    @Override
                    public void call(List<BaseMessage> baseMessages) {
                        SortList.sortListView(baseMessages);// 排序
                        PLogger.printObject(baseMessages);
                        listDatas.stopRefresh();
                        if (baseMessages.size() > 0) {
                            if (baseMessages.size() < 20) {
                                listDatas.setPullRefreshEnable(false);
                            }

                            List<BaseMessage> listTemp = new ArrayList<BaseMessage>();

                            for (BaseMessage baseMessage : baseMessages) {
                                if (isShowMsg(baseMessage)) {
                                    listTemp.add(baseMessage);
                                }
                            }

                            if (listDatas != null) {
                                listTemp.addAll(listDatas);
                            }

                            mSysMessAdapter.setList(listTemp);
                            listDatas.setSelection(baseMessages.size());
                        }
                    }
                });*/
    }

    @Override
    public void onLoadMore() {

    }

    private void testData() {
        if (sysMess == null)
            sysMess = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SysNoticeMessage diamond = new SysNoticeMessage();
            if (i % 2 == 1) {
                diamond.setInfo("活动");
                diamond.setBtn_text("点击查看");
                diamond.setPic("OO");
                diamond.setMsgDesc("系统活动");
            } else {
                diamond.setInfo(this.getString(R.string.sys_mess_content));
                diamond.setBtn_text(this.getString(R.string.sys_mess_btn_text));
                diamond.setMsgDesc(this.getString(R.string.updata_mess));
            }
            sysMess.add(diamond);
        }
    }
}