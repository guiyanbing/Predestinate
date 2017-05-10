package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.utils.FileUtil;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * Created by Kind on 2017/3/30.
 */

public class ChatRecordPanel extends BaseViewPanel implements ChatMediaRecord.OnRecordListener{

    private ChatAdapter.ChatInstance chatInstance = null;
    private ImageView micImage = null;
    private TextView recordingHint = null;

    // 录音动画
    private Drawable[] micImages = null;
    public ChatRecordPanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context);

        this.chatInstance = chatInstance;

        setContentView(R.layout.p1_chat_record);
        initView();
    }

    public void initView() {
        // 居中显示
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        getContentView().setLayoutParams(lp);

        micImage = (ImageView) findViewById(R.id.chat_mic_image);
        recordingHint = (TextView) findViewById(R.id.chat_recording_hint);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{
                getDrawable(R.drawable.y1_talk_sound_ly_1),
                getDrawable(R.drawable.y1_talk_sound_ly_2),
                getDrawable(R.drawable.y1_talk_sound_ly_3),
                getDrawable(R.drawable.y1_talk_sound_ly_4)
        };

        setVisibility(View.INVISIBLE);
    }

    /**
     * 根据Touch动作，开始录音、停止、取消等动作。
     *
     * @param action Touch动作。
     * @param posY   手指位置。
     */
    public void onTouch(int action, float posY) {
        onTouch(action, posY, null, null);
    }

    private boolean isPosY = true;//是否手指跑到屏幕上
    private boolean isSend = false;//是否可以发送,这个字段主要是给录音超过60秒,自动发送后重复发送用的

    /**
     * 根据Touch动作，开始录音、停止、取消等动作。
     *
     * @param action    Touch动作。
     * @param posY      手指位置。
     * @param whisperId 指定一个私聊Id，主要是群私聊用。
     */
    public void onTouch(int action, float posY, String channelId, String whisperId) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                if (!ModuleMgr.getCommonMgr().headRemindOnChat()) {
//                    return;
//                }
                if (ChatMediaRecord.getInstance().startRecordVoice(this)) {
                    setVisibility(View.VISIBLE);
                    recordingHint.setText(getContext().getString(R.string.chat_move_up_to_cancel));
                    isPosY = true;
                    isSend = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                MMLog.e("ACTION_MOVE", "posY--->" + posY);
                if (posY < 0) {
                    micImage.setImageResource(R.drawable.v3_voice_up_cancel);
                    recordingHint.setText(getContext().getString(R.string.chat_release_to_cancel));
                    isPosY = false;
                } else {
                    recordingHint.setText(getContext().getString(R.string.chat_move_up_to_cancel));
                    isPosY = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isSend) {
                    return;
                }
                isSend = false;
                setVisibility(View.INVISIBLE);
                ChatMediaRecord.getInstance().stopRecordVoice();

                if (posY < 0) {// 取消发送
                    FileUtil.deleteFile(ChatMediaRecord.getInstance().getVoiceFileName());
                } else {
                    int length = ChatMediaRecord.getInstance().getVoiceDuration();
                    String voiceFileUri = ChatMediaRecord.getInstance().getVoiceFileName();
                    if (length > 1000) {
                        ModuleMgr.getChatMgr().sendVoiceMsg(channelId, whisperId, voiceFileUri, (length + 500) / 1000);
                   //     if (chatInstance != null) {
                   //         ModuleMgr.getChatMgr().sendVoiceMsg(channelId, whisperId, voiceFileUri, (length + 500) / 1000, null);
                    //    } else {
                  //          ModuleMgr.getChatMgr().sendVoiceMsg(channelId, whisperId, voiceFileUri, (length + 500) / 1000, null);
                    //    }
                    } else {
                        MMToast.showShort("录音不能小于1秒!");
                        FileUtil.deleteFile(voiceFileUri);//清除无效文件
                    }
                }
                break;
            default:
                setVisibility(View.INVISIBLE);
                ChatMediaRecord.getInstance().stopRecordVoice();
                FileUtil.deleteFile(ChatMediaRecord.getInstance().getVoiceFileName());
                break;
        }
    }

    /**
     * 根据资源id获取一个Drawable。
     *
     * @param id 资源id。
     * @return Drawable实例。
     */
    private Drawable getDrawable(int id) {
        try {
            return getContext().getResources().getDrawable(id);
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }
        return null;
    }

    @Override
    public void onDB(int db) {
        int index = db / 25;

        if (index >= 4) {
            index = 3;
        }

        try {
            if (isPosY) {
                micImage.setImageDrawable(micImages[index]);
            }
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }
    }

    @Override
    public void onProgress(long time) {
    }

    @Override
    public void onProgressMax() {
        onTouch(MotionEvent.ACTION_UP, 0, chatInstance.chatAdapter.getChannelId(), chatInstance.chatAdapter.getWhisperId());
    }

    @Override
    public void onStatus(int status) {
    }
}
