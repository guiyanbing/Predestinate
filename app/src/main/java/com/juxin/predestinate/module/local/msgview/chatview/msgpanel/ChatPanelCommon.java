package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.view.CircularCoverView;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.library.view.DownloadProgressView;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatMediaPlayer;
import com.juxin.predestinate.module.local.msgview.chatview.msgpanel.video.VideoPlayDialog;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;
import com.juxin.predestinate.module.logic.baseui.custom.SelectableRoundedImageView;
import com.juxin.predestinate.module.logic.baseui.custom.TextureVideoView;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIUtil;
import com.shuisili.android.library.media.MediaUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/5/8.
 */

public class ChatPanelCommon extends ChatPanel implements ChatMediaPlayer.OnPlayListener{

    private CustomFrameLayout chat_item_customFrameLayout;

    private EmojiTextView chat_item_text = null;
    private SelectableRoundedImageView chat_item_img;

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
        chat_item_img = (SelectableRoundedImageView) findViewById(R.id.chat_item_img);

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

        if (msgData.getfStatus() == 1 && msgData.isAutoplay()) {//自动播放
            MMLog.autoDebug(msg.getImg());

          //  ChatMediaPlayer.getInstance().togglePlayVoice(ModuleMgr.getChatListMgr().spliceStringAmr(msg.getImg()), this);

            palySound = true;
            msgData.setfStatus(0);
            this.msgData = msgData;

            if (chatItemHolder != null) {
                chatItemHolder.statusImg.setVisibility(View.GONE);
            }

         //   ModuleMgr.getChatMgr().updateLocalReadVoiceStatus(msgData.getChannelID(), msgData.getWhisperID(), msgData.getMsgid());
        }
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof CommonMessage)) {
            return false;
        }

        CommonMessage msg = (CommonMessage) msgData;

        String videoUrl = msg.getVideoUrl();
        String localVideoUrl = msg.getLocalVideoUrl();
        String voiceUrl = msg.getVoiceUrl();
        String localVoiceUrl = msg.getLocalVoiceUrl();
        String img = msg.getImg();
        String localImg =msg.getLocalImg();
        if(!TextUtils.isEmpty(videoUrl) || !TextUtils.isEmpty(localVideoUrl)){//视频
            onVideoDisplayContent(msg);
        }else if(!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)){//语音
            onVoiceDisplayContent(msg);
        }else if(!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)){//图片
            chat_item_customFrameLayout.show(R.id.chat_item_img);


        }else{
            onTextDisplayContent(msg);
        }



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

    private void onVoiceDisplayContent(CommonMessage msg){
        chat_item_customFrameLayout.show(R.id.chat_item_panel_voice);

        time.setText("" + msg.getVoiceLen() + "''");
        time.setWidth(UIUtil.dp2px((float) (20.f + Math.sqrt(msg.getVoiceLen() - 1) * 20)));
    }

    private void onTextDisplayContent(CommonMessage msg){
        chat_item_customFrameLayout.show(R.id.chat_item_text);
        chat_item_text.setText(Html.fromHtml(msg.getContent()));
        chat_item_text.setTextColor(isSender() ? Color.WHITE : getContext().getResources().getColor(R.color.color_666666));
    }


    private void onVideoDisplayContent(CommonMessage msg){
        chat_item_customFrameLayout.show(R.id.chat_item_panel_video);
        String url = msg.getLocalVideoUrl();
        if (TextUtils.isEmpty(url)) {
            url = msg.getVideoUrl();

            ImageLoader.loadFitCenter(context, url, thumb_img);
        } else {
            thumb_img.setImageURI(Uri.parse(url));
        }
    }


    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof CommonMessage)) {
            return false;
        }

        CommonMessage msg = (CommonMessage) msgData;

        String videoUrl = msg.getVideoUrl();
        String localVideoUrl = msg.getLocalVideoUrl();
        String voiceUrl = msg.getVoiceUrl();
        String localVoiceUrl = msg.getLocalVoiceUrl();
        String img = msg.getImg();
        String localImg =msg.getLocalImg();
        if(!TextUtils.isEmpty(videoUrl) || !TextUtils.isEmpty(localVideoUrl)){//视频
            onVideoClickContent(msg);
        }else if(!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)){//语音

        }else if(!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)){//图片

        }else{

        }


        return true;
    }


    private void onVideoClickContent(CommonMessage msg){
        String url = msg.getLocalVideoUrl();
        if (TextUtils.isEmpty(url)) url = msg.getVideoUrl();
        MMLog.autoDebug("video click content url：" + url);

        //视频播放
        preview_play.setVisibility(View.GONE);
        if (isPlaying) {
            VideoPlayDialog playDialog = new VideoPlayDialog((FragmentActivity) App.activity, url);
            playDialog.show();
            resetPlay();
        } else {
            if (FileUtil.isURL(url)) {
            //    ModuleMgr.getChatMgr().reqVideo(url, this);
            } else {
                if (new File(url).exists()) {
                    playLocalVideo(url);
                } else {
                  //  ModuleMgr.getChatMgr().x(msg.getVideoUrl(), this);
                }
            }
        }
    }

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
                MMLog.autoDebug("------> mediaPlayer is error");
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
            MediaUtils.playSound(R.raw.play_voice_after);
        }

        MMLog.autoDebug(filePath);

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
}
