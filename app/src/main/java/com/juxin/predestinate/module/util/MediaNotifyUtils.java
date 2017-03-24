package com.juxin.predestinate.module.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;

import com.juxin.predestinate.module.logic.application.App;

/**
 * 软件内通知：声音及震动工具
 */
public class MediaNotifyUtils {

    /**
     * 震动
     */
    public static void vibrator() {
        Vibrator vibrator = (Vibrator) App.context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400};
        vibrator.vibrate(pattern, -1);
    }

    /**
     * 播放系统默认消息提示音
     */
    public static void playSound() {
        MediaPlayer mMediaPlayer = MediaPlayer.create(App.context,
                RingtoneManager.getActualDefaultRingtoneUri(App.context, RingtoneManager.TYPE_NOTIFICATION));
        mMediaPlayer.setLooping(false);
        mMediaPlayer.start();
    }
}
