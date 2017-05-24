package com.juxin.predestinate.ui.mail.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 私聊类型
 * Created by Kind on 16/2/3.
 */
public class CustomLetterMailItem extends CustomBaseMailItem {

    private BaseMessage msgData = null;

    public CustomLetterMailItem(Context context) {
        super(context);
    }

    public CustomLetterMailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLetterMailItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        super.init(-1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.mail_item_headpic:
                if(msgData != null && MailSpecialID.customerService.getSpecialID() != msgData.getLWhisperID()){
                    UIShow.showCheckOtherInfoAct(getContext(),msgData.getLWhisperID());
                }
                break;
        }
    }

    @Override
    public void showGap() {
        super.showGap();
    }

    @Override
    public void showData(BaseMessage msgData) {
        super.showData(msgData);
        this.msgData = msgData;
    }
}