package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.utils.FileUtil;
import com.juxin.library.view.CircularCoverView;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.library.view.DownloadProgressView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatMediaPlayer;
import com.juxin.predestinate.module.local.msgview.chatview.input.MsgPopView;
import com.juxin.predestinate.module.local.msgview.chatview.msgpanel.video.VideoPlayDialog;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;
import com.juxin.predestinate.module.logic.baseui.custom.TextureVideoView;
import com.juxin.predestinate.module.util.MediaNotifyUtils;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.utils.MyURLSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本、图片、短语音、小视频消息展示panel
 * Created by Kind on 2017/5/8.
 */
public class ChatPanelCommon extends ChatPanel implements ChatMediaPlayer.OnPlayListener {

    private CustomFrameLayout chat_item_customFrameLayout;

    private EmojiTextView chat_item_text = null;
    private ImageView chat_item_img;

    private View parent = null;
    private ImageView img = null;
    private TextView time = null;
    private AnimationDrawable voiceAnimation = null;
    private boolean palySound = false;
    private BaseMessage msgData = null;


    private TextureVideoView preview_surface;
    private ImageView thumb_img;
    private View preview_play;
    private DownloadProgressView progress_bar;
    private boolean isPlaying = false;


    public ChatPanelCommon(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_common, sender);
    }

    @Override
    public void initView() {
        chat_item_customFrameLayout = (CustomFrameLayout) findViewById(R.id.chat_item_customFrameLayout);
        chat_item_text = (EmojiTextView) findViewById(R.id.chat_item_text);
        chat_item_img = (ImageView) findViewById(R.id.chat_item_img);

        //语音
        if (isSender()) {
            parent = findViewById(R.id.chat_item_panel_voice_right);
            img = (ImageView) findViewById(R.id.chat_item_voice_right_img);
            time = (TextView) findViewById(R.id.chat_item_voice_right_time);
        } else {
            parent = findViewById(R.id.chat_item_panel_voice_left);
            img = (ImageView) findViewById(R.id.chat_item_voice_left_img);
            time = (TextView) findViewById(R.id.chat_item_voice_left_time);
        }

        parent.setVisibility(View.VISIBLE);

        //视频
        CircularCoverView coverView = (CircularCoverView) findViewById(R.id.chat_item_video_cover_view);
        preview_surface = (TextureVideoView) findViewById(R.id.chat_item_video_preview_surface);
        thumb_img = (ImageView) findViewById(R.id.chat_item_video_thumb_img);
        preview_play = findViewById(R.id.chat_item_video_preview_play);
        progress_bar = (DownloadProgressView) findViewById(R.id.chat_item_video_progress_bar);

        preview_surface.setMute(true);
        preview_surface.setPlayingScreenOn(false);
        progress_bar.setMax(100);

        if (isSender()) {
            coverView.setRadians(15, 6, 15, 15);
        } else {
            coverView.setRadians(6, 15, 15, 15);
        }
    }

    @Override
    public void setInit(BaseMessage msgData) {
        if (msgData == null || !(msgData instanceof CommonMessage)) return;
        CommonMessage msg = (CommonMessage) msgData;

        String videoUrl = msg.getVideoUrl();
        String localVideoUrl = msg.getLocalVideoUrl();
        String voiceUrl = msg.getVoiceUrl();
        String localVoiceUrl = msg.getLocalVoiceUrl();
        String img = msg.getImg();
        String localImg = msg.getLocalImg();
        if (!TextUtils.isEmpty(videoUrl) || !TextUtils.isEmpty(localVideoUrl)) {//视频
        } else if (!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)) {//语音
        } else if (!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)) {//图片
        } else {
        }

//        if (msgData.getfStatus() == 1 && msgData.isAutoplay()) {//自动播放
//            PLogger.d(msg.getImg());
//
//          //  ChatMediaPlayer.getInstance().togglePlayVoice(ModuleMgr.getChatListMgr().spliceStringAmr(msg.getImg()), this);
//
//            palySound = true;
//            msgData.setfStatus(0);
//            this.msgData = msgData;
//
//            if (chatItemHolder != null) {
//                chatItemHolder.statusImg.setVisibility(View.GONE);
//            }
//
//         //   ModuleMgr.getChatMgr().updateLocalReadVoiceStatus(msgData.getChannelID(), msgData.getWhisperID(), msgData.getMsgid());
//        }
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof CommonMessage)) return false;
        CommonMessage msg = (CommonMessage) msgData;

        String videoUrl = msg.getVideoUrl();
        String localVideoUrl = msg.getLocalVideoUrl();
        String voiceUrl = msg.getVoiceUrl();
        String localVoiceUrl = msg.getLocalVoiceUrl();
        String img = msg.getImg();
        String localImg = msg.getLocalImg();

        //视频
        if (!TextUtils.isEmpty(videoUrl) || !TextUtils.isEmpty(localVideoUrl)) {
            onVideoDisplayContent(msg);
            return true;
        }
        //语音
        if (!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)) {
            onVoiceDisplayContent(msg);
            return true;
        }
        //图片
        if (!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)) {
            onImgDisplayContent(msg);
            setShowParentBg(false);
            return true;
        }
        //文本
        setShowParentBg(true);
        onTextDisplayContent(msg);
        return true;
    }

    /**
     * 加载展示语音消息
     */
    private void onVoiceDisplayContent(CommonMessage msg) {
        PLogger.printObject("CommonMessage===" + msg.toString());
        chat_item_customFrameLayout.show(R.id.chat_item_panel_voice);

        time.setText("" + msg.getVoiceLen() + "''");
        time.setWidth(UIUtil.dp2px((float) (20.f + Math.sqrt(msg.getVoiceLen() - 1) * 20)));
    }

    /**
     * 加载展示文本消息
     */
    private void onTextDisplayContent(CommonMessage msg) {
        chat_item_customFrameLayout.show(R.id.chat_item_text);
        if (BaseMessage.BaseMessageType.hi.getMsgType() == msg.getType()) {
            MyURLSpan.addClickToTextViewLink(App.getActivity(), chat_item_text, msg.getMsgDesc());
        } else {
            chat_item_text.setTextContent(msg.getMsgDesc());
        }
        chat_item_text.setTextColor(isSender() ? Color.WHITE : getContext().getResources().getColor(R.color.color_666666));
    }

    /**
     * 加载展示图片消息
     */
    private void onImgDisplayContent(CommonMessage msg) {
        chat_item_customFrameLayout.show(R.id.chat_item_img);

        String url = msg.getLocalImg();
        if (TextUtils.isEmpty(url)) url = msg.getImg();
        ImageLoader.loadRound(getContext(), url, chat_item_img);
    }

    /**
     * 加载展示视频消息，此处只展示缩略图
     */
    private void onVideoDisplayContent(CommonMessage msg) {
        chat_item_customFrameLayout.show(R.id.chat_item_panel_video);
        String url = msg.getVideoThumb();
        ImageLoader.loadFitCenter(getContext(), url, thumb_img);
    }

    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof CommonMessage)) return false;
        CommonMessage msg = (CommonMessage) msgData;

        String videoUrl = msg.getVideoUrl();
        String localVideoUrl = msg.getLocalVideoUrl();
        String voiceUrl = msg.getVoiceUrl();
        String localVoiceUrl = msg.getLocalVoiceUrl();
        String img = msg.getImg();
        String localImg = msg.getLocalImg();

        if (!TextUtils.isEmpty(videoUrl) || !TextUtils.isEmpty(localVideoUrl)) {//视频
            onVideoClickContent(msg);
        } else if (!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)) {//语音
            onVoiceClickContent(msg);
        } else if (!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)) {//图片
            onImgClickContent(msg, longClick);
        } else {
            return false;//暂时无其他类型的处理panel
        }
        return true;
    }

    /**
     * 图片点击处理逻辑
     *
     * @param msg       图片消息体
     * @param longClick 是否为长按事件响应
     */
    private void onImgClickContent(CommonMessage msg, boolean longClick) {
        if (longClick) {//长按图片
            new MsgPopView(context, msg.getImg()).show(contentView);
            return;
        }
        String url = msg.getLocalImg();
        if (TextUtils.isEmpty(url)) url = msg.getImg();
        UIShow.showPhotoOfBigImg((FragmentActivity) App.getActivity(), url);
    }

    /**
     * 视频点击处理逻辑
     */
    private void onVideoClickContent(CommonMessage msg) {
        String url = msg.getLocalVideoUrl();
        if (TextUtils.isEmpty(url) || !FileUtil.isExist(url)) url = msg.getVideoUrl();

        //视频播放
        preview_play.setVisibility(View.GONE);
        if (isPlaying) {
            VideoPlayDialog playDialog = new VideoPlayDialog((FragmentActivity) App.activity, url);
            playDialog.show();
            resetPlay();
        } else {
            ChatMediaPlayer.getInstance().playVideo(true, preview_surface, url);
        }
    }

    /**
     * 语音点击处理逻辑
     */
    private void onVoiceClickContent(CommonMessage msg) {
        String url = msg.getLocalVoiceUrl();
        if (TextUtils.isEmpty(url) || !FileUtil.isExist(url)) url = msg.getVoiceUrl();
        ChatMediaPlayer.getInstance().togglePlayVoice(ModuleMgr.getChatListMgr().spliceStringAmr(url), this);

        if (msg.getfStatus() == 1) {
            palySound = true;
            msg.setfStatus(0);
            if (chatItemHolder != null) {
                chatItemHolder.statusImg.setVisibility(View.GONE);
            }
            ModuleMgr.getChatMgr().updateToReadVoice(msg.getMsgID());
        }
    }

    /**
     * 视频播放完成之后重置播放界面
     */
    private void resetPlay() {
        isPlaying = false;

        preview_surface.stopPlayback();
        thumb_img.setVisibility(View.VISIBLE);
        preview_play.setVisibility(View.VISIBLE);
    }

    /**
     * 播放视频
     */
    public void playLocalVideo(final String filePath) {
        isPlaying = true;

        preview_surface.setVideoPath(filePath);
        preview_surface.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                preview_surface.start();
                TimerUtil.beginTime(new TimerUtil.CallBack() {//短暂延时之后取消遮罩，防止屏幕闪烁
                    @Override
                    public void call() {
                        thumb_img.setVisibility(View.GONE);
                    }
                }, 200);
            }
        });
        preview_surface.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                resetPlay();
                return true;
            }
        });
    }


    @Override
    public void onStart(String filePath) {
        showAnimation();
    }

    @Override
    public void onStop(String filePath) {
        stopAnimation();

        if (palySound && !TextUtils.isEmpty(filePath)) {
            palySound = false;
            MediaNotifyUtils.playSound(getContext(), R.raw.play_voice_after);
        }

        if (msgData != null) {
            try {
                List<BaseMessage> baseMessages = getChatInstance().chatContentAdapter.getList();
                List<BaseMessage> tmpList = new ArrayList<BaseMessage>();//临时
                for (BaseMessage tmp : baseMessages) {
                    if (!tmp.isSender()) {
                        tmpList.add(tmp);
                    }
                }

                int index = tmpList.indexOf(msgData) + 1;
                if (tmpList.size() > index) {
                    BaseMessage tmp = tmpList.get(index);
                    if (tmp.getfStatus() == 1) {
                        BaseMessage temp = baseMessages.get(baseMessages.indexOf(tmp));
                        temp.setAutoplay(true);
                        getChatInstance().chatContentAdapter.notifyDataSetChanged();
                        msgData = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ResourceType")
    private void showAnimation() {
        if (isSender()) {
            img.setImageResource(R.drawable.voice_to_icon);
        } else {
            img.setImageResource(R.drawable.voice_from_icon);
        }

        voiceAnimation = (AnimationDrawable) img.getDrawable();
        voiceAnimation.start();
    }

    private void stopAnimation() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }

        if (isSender()) {
            img.setImageResource(R.drawable.y1_talk_sound_r);
        } else {
            img.setImageResource(R.drawable.y1_talk_sound_l);
        }
    }

    @Override
    public boolean onClickErrorResend(BaseMessage msgData) {
        if (msgData == null || !(msgData instanceof CommonMessage)) {
            return false;
        }
        setDialog(msgData, null);
        return true;
    }
}
