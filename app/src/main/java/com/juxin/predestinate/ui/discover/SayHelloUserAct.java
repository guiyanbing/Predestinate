package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.item.MailMsgID;

import java.util.ArrayList;
import java.util.List;

/**
 * 打招呼的人
 * Created by zhang on 2017/5/22.
 */

public class SayHelloUserAct extends BaseActivity implements ExListView.IXListViewListener, AdapterView.OnItemClickListener {

    private CustomStatusListView customStatusListView;
    private ExListView exListView;

    private List<BaseMessage> data = new ArrayList<>();

    private SayHelloUserAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_say_hello_user_act);
        initView();
        initData();
    }

    private void initView() {
        setTitle(getString(R.string.say_hello_user_act_title));
        setBackView();
        customStatusListView = (CustomStatusListView) findViewById(R.id.say_hello_users_list);
        View mViewTop = LayoutInflater.from(this).inflate(R.layout.layout_margintop, null);
        exListView = customStatusListView.getExListView();
        exListView.setPullLoadEnable(true);
        exListView.setPullLoadEnable(false);
        exListView.setXListViewListener(this);
        exListView.addHeaderView(mViewTop);
        adapter = new SayHelloUserAdapter(this, data);
        exListView.setAdapter(adapter);
        exListView.setOnItemClickListener(this);
        customStatusListView.showLoading();
    }

    private void initData() {
        if (data.size() != 0) {
            data.clear();
        }
        data.addAll(ModuleMgr.getChatListMgr().getGeetList());
        if (data.size() != 0) {
            adapter.notifyDataSetChanged();
            exListView.stopRefresh();
            customStatusListView.showExListView();
        } else {
            customStatusListView.showNoData();
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BaseMessage message = (BaseMessage) adapterView.getAdapter().getItem(i);
        if (message != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(message.getLWhisperID());
            if (mailMsgID == null) {
                UIShow.showPrivateChatAct(this, message.getLWhisperID(), message.getName(), message.getKfID());
            }
        }
    }
}
