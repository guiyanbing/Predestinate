package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;

/**
 * Created by Kind on 2017/5/8.
 */

public class ChatPanelCommon extends ChatPanel{

    private EmojiTextView text = null;


    public ChatPanelCommon(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_common, sender);

    }

    @Override
    public void initView() {
        text = (EmojiTextView) findViewById(R.id.chat_item_text);
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof CommonMessage)) {
            return false;
        }

        CommonMessage msg = (CommonMessage) msgData;

        text.setText(Html.fromHtml(msg.getContent()));

        text.setTextColor(isSender() ? Color.WHITE : getContext().getResources().getColor(R.color.color_666666));

//        text.setMovementMethod(LinkMovementMethod.getInstance());
//        CharSequence linkContent = text.getText();
//        if (linkContent instanceof Spannable) {
//            int end = linkContent.length();
//            Spannable sp = (Spannable) linkContent;
//            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//            SpannableStringBuilder style = new SpannableStringBuilder(linkContent);
//            style.clearSpans();
//            for (int i = 0; i < urls.length; i++) {
//                style.setSpan(new Hyperlinks(urls[i].getURL()), sp.getSpanStart(urls[i]), sp.getSpanEnd(urls[i]), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            text.setText(style);
//        }

        return true;
    }
}
