package com.juxin.predestinate.ui.mail.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.VideoMessage;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;

/**
 * 私聊类型
 * Created by Kind on 16/2/3.
 */
public class CustomLetterMailItem extends CustomBaseMailItem {

    private BaseMessage msgData = null;
    public Button mail_item_right_icon;

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
        mail_item_right_icon = (Button) findViewById(R.id.mail_item_right_icon);
        mail_item_right_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.mail_item_headpic:
                if (msgData != null && MailSpecialID.customerService.getSpecialID() != msgData.getLWhisperID()) {
                    UIShow.showCheckOtherInfoAct(getContext(), msgData.getLWhisperID());
                }
                break;
            case R.id.mail_item_right_icon:
                if (msgData == null || !(msgData instanceof VideoMessage)) {
                    break;
                }
                VideoMessage videoMessage = (VideoMessage) msgData;
                if (videoMessage.isVideoMediaTp()) {
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) getContext(), msgData.getLWhisperID(), VideoAudioChatHelper.TYPE_VIDEO_CHAT);
                } else {
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) getContext(), msgData.getLWhisperID(), VideoAudioChatHelper.TYPE_AUDIO_CHAT);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showGap() {
        super.showGap();
    }

    @Override
    public void showData(BaseMessage msgData, boolean isSlideLoading) {
        super.showData(msgData, isSlideLoading);
        this.msgData = msgData;
        findViewById(R.id.gap_item).setVisibility(GONE);

        if (item_last_time != null && BaseMessage.video_MsgType == msgData.getType()) {
            item_last_time.setVisibility(GONE);
        }
        if (mail_item_right_icon == null || BaseMessage.video_MsgType != msgData.getType() || !(msgData instanceof VideoMessage)) {// 视频语音消息
            mail_item_right_icon.setVisibility(GONE);
            return;
        }

        mail_item_right_icon.setVisibility(VISIBLE);
        VideoMessage videoMessage = (VideoMessage) msgData;
        mail_item_right_icon.setBackgroundResource(videoMessage.isVideoMediaTp() ? R.drawable.f1_video_state_ico : R.drawable.f1_call_state_ico);
        mail_item_right_icon.setEnabled(true);
    }
}