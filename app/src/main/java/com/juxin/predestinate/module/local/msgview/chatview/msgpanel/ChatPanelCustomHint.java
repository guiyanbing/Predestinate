package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;

/**
 * Created by Kind on 2017/5/23.
 */

public class ChatPanelCustomHint extends ChatPanel {

    public ChatPanelCustomHint(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_common, sender);

    }

    @Override
    public void initView() {

    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        return false;
    }
}
