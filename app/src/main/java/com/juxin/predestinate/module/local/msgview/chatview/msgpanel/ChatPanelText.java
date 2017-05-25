package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.widget.TextView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.TextMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.ui.utils.MyURLSpan;

/**
 * Created by Kind on 2017/5/12.
 */

public class ChatPanelText extends ChatPanel {
    private TextView chat_item_text;

    public ChatPanelText(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_text, sender);
    }

    @Override
    public void initView() {
        chat_item_text = (TextView) findViewById(R.id.chat_item_text);
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof TextMessage)) {
            return false;
        }

        TextMessage msg = (TextMessage) msgData;
        chat_item_text.setTextColor(isSender() ? Color.WHITE : context.getResources().getColor(R.color.text_zhuyao_black));

        chat_item_text.setText(Html.fromHtml(msg.getMsgDesc()));

        MyURLSpan.addClickToTextViewLinkEx(App.getActivity(), chat_item_text, msg.getMsgDesc());

        return true;
    }
}
