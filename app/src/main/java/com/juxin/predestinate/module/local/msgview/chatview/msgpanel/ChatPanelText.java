package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.TextMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;

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

//        chat_item_text.setMovementMethod(LinkMovementMethod.getInstance());
//        CharSequence linkContent = chat_item_text.getText();
//        if (linkContent instanceof Spannable) {
//            int end = linkContent.length();
//            Spannable sp = (Spannable) linkContent;
//            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//            SpannableStringBuilder style = new SpannableStringBuilder(linkContent);
//            style.clearSpans();
//            for (int i = 0; i < urls.length; i++) {
//                Hyperlinks hyperlinks = new Hyperlinks(urls[i].getURL());
//                style.setSpan(hyperlinks, sp.getSpanStart(urls[i]), sp.getSpanEnd(urls[i]), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            hi.setText(style);
//        }
        return true;
    }
}
