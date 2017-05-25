package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.VideoMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.util.TimeUtil;

/**
 * Created by Kind on 2017/5/12.
 */

public class ChatPanelVideo extends ChatPanel {
    private TextView chat_item_video_text;

    public ChatPanelVideo(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_audio, sender);
    }

    @Override
    public void initView() {
        chat_item_video_text = (TextView) findViewById(R.id.chat_item_video_text);
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof VideoMessage)) {
            return false;
        }
        VideoMessage videoMessage = (VideoMessage) msgData;
        chat_item_video_text.setText(VideoMessage.getVideoChatContent(videoMessage.getEmLastStatus(), videoMessage.getTime(), videoMessage.isSender()));
        return true;
    }




}
