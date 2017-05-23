package com.juxin.predestinate.ui.mail.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;

/**
 * 目前，关注, 好友在用
 * Created by Kind on 16/2/3.
 */
public class CustomOtherMailItem extends CustomBaseMailItem {

    public CustomOtherMailItem(Context context) {
        super(context);
    }

    public CustomOtherMailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomOtherMailItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        super.init(R.layout.p1_mail_item_act);
        item_headpic = (ImageView) getContentView().findViewById(R.id.mail_item_headpic);
        item_unreadnum = (TextView) getContentView().findViewById(R.id.mail_item_unreadnum);
        item_nickname = (TextView) getContentView().findViewById(R.id.mail_item_nickname);
        item_last_msg = (EmojiTextView) getContentView().findViewById(R.id.mail_item_last_msg);
        item_headpic.setOnClickListener(this);
    }

    @Override
    public void showGap() {
        super.showGap();
    }

    @Override
    public void showData(BaseMessage msgData) {
        if (!TextUtils.isEmpty(msgData.getAvatar())) {
            ImageLoader.loadAvatar(getContext(), msgData.getAvatar(), item_headpic);
        } else {
            item_headpic.setImageResource(msgData.getLocalAvatar());
        }

        String nickname = msgData.getName();
        if (!TextUtils.isEmpty(nickname)) {
            if (nickname.length() > 10) {
                item_nickname.setText(nickname.substring(0, 9) + "...");
            } else {
                item_nickname.setText(nickname);
            }
        } else {
            item_nickname.setText(msgData.getWhisperID());
        }

        item_last_msg.setText(msgData.getAboutme());
//        String conStr;
//        MailMsgID mailMsgID = MailMsgID.getMailMsgID(msgData.getLWhisperID());
//        if (mailMsgID != null) {
//            switch (mailMsgID) {
//                case WhoAttentionMe_Msg://最近来访
//                    if (msgData.getNum() > 0) {
//                        conStr = "你有" + msgData.getNum() + "个新的来访";
//                    } else {
//                        conStr = "";
//                    }
//                    break;
//                default:
//                    conStr = msgData.getAboutme();
//                    break;
//            }
//        } else {
//            conStr = BaseMessage.getContent(msgData);
//        }
//
//        item_last_msg.setText(conStr);
        setUnreadnum(msgData);
    }
}