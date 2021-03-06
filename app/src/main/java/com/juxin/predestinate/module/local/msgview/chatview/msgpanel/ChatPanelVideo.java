package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.VideoMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

/**
 * 音视频结束后状态展示panel
 * Created by Kind on 2017/5/12.
 */
public class ChatPanelVideo extends ChatPanel {

    private ImageView chat_item_type_img;
    private TextView chat_item_video_text;

    private UserInfoLightweight infoLightweight;

    public ChatPanelVideo(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_audio, sender);
    }

    @Override
    public void initView() {
        CustomFrameLayout fl_video_panel = (CustomFrameLayout) findViewById(R.id.fl_video_panel);
        if (isSender()) {
            chat_item_type_img = (ImageView) findViewById(R.id.iv_type_right);
            chat_item_video_text = (TextView) findViewById(R.id.tv_text_right);
            fl_video_panel.showOfIndex(1);
        } else {
            chat_item_type_img = (ImageView) findViewById(R.id.iv_type_left);
            chat_item_video_text = (TextView) findViewById(R.id.tv_text_left);
            fl_video_panel.showOfIndex(0);
        }
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof VideoMessage))
            return false;

        this.infoLightweight = infoLightweight;
        VideoMessage videoMessage = (VideoMessage) msgData;
        chat_item_type_img.setImageResource(videoMessage.isVideoMediaTp()
                ? (isSender() ? R.drawable.f1_chat_video_right : R.drawable.f1_chat_video_left)
                : (isSender() ? R.drawable.f1_chat_voice_right : R.drawable.f1_chat_voice_left));
        chat_item_video_text.setText(VideoMessage.getVideoChatContent(
                videoMessage.getEmLastStatus(), videoMessage.getVideoVcTalkTime(), videoMessage.isSender()));

        PLogger.d("tp=" + videoMessage.getVideoTp() + ":code=" + videoMessage.getVideoVcEscCode());
        return true;
    }

    @Override
    public boolean onClickContent(final BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof VideoMessage) || isSender())
            return false;

        // 如果是别人发起的音视频，点击之后更新小红点已读状态
        ModuleMgr.getChatMgr().updateMsgFStatus(msgData.getMsgID(), null);

        //暂时注释掉（不让其回拨）
        //        String channel_uid = "";
        //        if (infoLightweight != null) {
        //            channel_uid = String.valueOf(infoLightweight.getChannel_uid());
        //        }

        // 发起音视频
        //        VideoAudioChatHelper.getInstance().inviteVAChat((Activity) App.getActivity(), msgData.getLWhisperID(),
        //                ((VideoMessage) msgData).isVideoMediaTp() ? VideoAudioChatHelper.TYPE_VIDEO_CHAT : VideoAudioChatHelper.TYPE_AUDIO_CHAT, channel_uid);
        return true;
    }
}
