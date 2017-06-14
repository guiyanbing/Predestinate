package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.SysNoticeMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;

/**
 * 系统消息通知
 * Created by Kind on 2017/6/14.
 */
public class ChatPanelSysNotice extends ChatPanel {

    public ChatPanelSysNotice(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_sysnotice, sender);
    }

    @Override
    public void initView() {

    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof SysNoticeMessage)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {

        return true;
    }



}
