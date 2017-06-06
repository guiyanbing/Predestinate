package com.juxin.predestinate.ui.mail.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.unread.BadgeView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.item.MailMsgID;

/**
 * 目前，关注, 好友在用
 * Created by Kind on 16/2/3.
 */
public class CustomOtherMailItem extends CustomBaseMailItem {

    private BaseMessage msgData;
    private BadgeView mail_item_unreadnum_two;

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
        item_unreadnum = (BadgeView) getContentView().findViewById(R.id.mail_item_unreadnum);
        mail_item_unreadnum_two =  (BadgeView) getContentView().findViewById(R.id.mail_item_unreadnum_two);
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
        this.msgData = msgData;
        if (!TextUtils.isEmpty(msgData.getAvatar())) {
            ImageLoader.loadRoundAvatar(getContext(), msgData.getAvatar(), item_headpic);
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

        item_unreadnum.setVisibility(View.GONE);
        mail_item_unreadnum_two.setVisibility(GONE);
        if (msgData.getNum() > 0) {
            if(MailMsgID.Greet_Msg.type == msgData.getLWhisperID()){
                mail_item_unreadnum_two.setVisibility(VISIBLE);
                return;
            }
            item_unreadnum.setVisibility(View.VISIBLE);
            item_unreadnum.setText(ModuleMgr.getChatListMgr().getUnreadNum(msgData.getNum()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mail_item_headpic:
                if (msgData == null) return;
                MailMsgID mailMsgID = MailMsgID.getMailMsgID(msgData.getLWhisperID());
                if (mailMsgID == null) return;
                switch (mailMsgID) {
                    case Follow_Msg://谁关注我
                        UIShow.showMyAttentionAct(getContext());
                        break;
                    case MyFriend_Msg://我的好友
                        UIShow.showMyFriendsAct(getContext());
                        break;
                    case Greet_Msg://打招呼的人
                        UIShow.showSayHelloUserAct(getContext());
                        break;
                }
                break;
            default:
                break;
        }
    }
}