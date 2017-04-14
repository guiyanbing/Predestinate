package com.juxin.predestinate.module.local.chat;

import com.juxin.library.observe.ModuleBase;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/4/13.
 */

public class ChatListMgr implements ModuleBase {

    private List<BaseMessage> msgList = new ArrayList<>(); //私聊列表


    @Override
    public void init() {

    }

    @Override
    public void release() {

    }


    public List<BaseMessage> getMsgList() {
        List<BaseMessage> tempList = new ArrayList<>();
        synchronized (msgList) {
            tempList.addAll(msgList);
            return tempList;
        }
    }
}
