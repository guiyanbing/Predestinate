package com.juxin.predestinate.module.local.msgview.chatview;

import android.content.Context;

import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * Created by Kind on 2017/3/30.
 */

public class ChatBasePanel extends BaseViewPanel {

    private ChatAdapter.ChatInstance chatInstance = null;

    public ChatBasePanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context);
        this.chatInstance = chatInstance;
    }

    public ChatAdapter.ChatInstance getChatInstance() {
        return chatInstance;
    }

    public void setChatInstance(ChatAdapter.ChatInstance chatInstance) {
        this.chatInstance = chatInstance;
    }
}
