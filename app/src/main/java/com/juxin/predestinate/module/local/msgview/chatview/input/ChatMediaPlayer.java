package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.TextureVideoView;

/**
 * Created by Kind on 2017/5/9.
 */
public class ChatMediaPlayer implements Handler.Callback, SensorEventListener {

    private boolean playing = false;            //视频或音频是否正在播放

    private MediaPlayer mediaPlayer = null;     //媒体播放
    private String oriFilePath = null;          //播放文件地址：本地文件地址

    private static final int MEDIA_RECORDER_EVENT_Start = 0;
    private static final int MEDIA_RECORDER_EVENT_Stop = 1;
    private static final int frequency = 200;
    private Handler handler = null;

    private OnPlayListener onPlayListener = null;   //播放监听

    private static ChatMediaPlayer ourInstance = new ChatMediaPlayer();

    public static ChatMediaPlayer getInstance() {
        return ourInstance;
    }

    private ChatMediaPlayer() {
        Looper looper;

        if ((looper = Looper.myLooper()) != null) {
            handler = new Handler(looper, this);
        } else if ((looper = Looper.getMainLooper()) != null) {
            handler = new Handler(looper, this);
        } else {
            handler = null;
        }
    }

    /**
     * 切换播放状态，如果当前播放的和将要播放的是同一个文件，则停止播放。
     *
     * @param filePath       音频文件。
     * @param onPlayListener 播放状态监听。
     */
    public synchronized void togglePlayVoice(final String filePath, final OnPlayListener onPlayListener) {
        togglePlayVoice(filePath, false, onPlayListener);
    }

    /**
     * 切换播放状态，如果当前播放的和将要播放的是同一个文件，则停止播放。
     *
     * @param filePath       音频文件。
     * @param mute           静音播放。
     * @param onPlayListener 播放状态监听。
     */
    public synchronized void togglePlayVoice(final String filePath, boolean mute, final OnPlayListener onPlayListener) {
        if (oriFilePath != null && oriFilePath.equals(filePath)) {
            oriFilePath = null;
            stopPlayVoice();
            return;
        }

        playVoice(filePath, mute, onPlayListener);
    }

    /**
     * 播放一个音频。
     *
     * @param filePath       音频文件。
     * @param onPlayListener 播放状态监听。
     */
    public synchronized void playVoice(final String filePath, final OnPlayListener onPlayListener) {
        playVoice(filePath, false, onPlayListener);
    }

    /**
     * 播放一个音频。
     *
     * @param filePath       音频文件。
     * @param mute           静音播放。
     * @param onPlayListener 播放状态监听。
     */
    public synchronized void playVoice(final String filePath, final boolean mute, final OnPlayListener onPlayListener) {
        stopPlayVoice();
        this.oriFilePath = filePath;
        this.onPlayListener = onPlayListener;

        PLogger.d(filePath);

        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        if (!FileUtil.isURL(filePath)) {
            playVoice(filePath, mute);
            return;
        }

        ModuleMgr.getHttpMgr().download(filePath, new DownloadListener() {
            @Override
            public void onStart(String url, String filePath) {}

            @Override
            public void onProcess(String url, int process, long size) {}

            @Override
            public void onSuccess(String url, String filePath) {
                if (oriFilePath != null && oriFilePath.equals(filePath)) {
                    ChatMediaPlayer.getInstance().playVoice(filePath, mute);
                }
            }

            @Override
            public void onFail(String url, Throwable throwable) {

            }
        });

//        ModuleMgr.getChatMgr().reqVoice(filePath, null, new HttpMgr.IReqComplete() {
//            @Override
//            public void onReqComplete(HttpResult result) {
//                if (result.isOk()) {
//                    if (oriFilePath != null && oriFilePath.equals(filePath)) {
//                        ChatMediaPlayer.getInstance().playVoice(result.getResultStr(), mute);
//                    }
//                }
//            }
//        });
    }

    /**
     * 只接受本地地址。
     *
     * @param filePath 本地文件地址。
     * @param mute     静音播放。
     */
    private void playVoice(final String filePath, boolean mute) {
        PLogger.d("mute: " + mute + "; file: " + filePath);

        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        if (isPlaying()) {
            return;
        }

        try {
            AudioManager audioManager = (AudioManager) App.context.getSystemService(Context.AUDIO_SERVICE);
            mediaPlayer = new MediaPlayer();


            if (ispeaker()) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(true);
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            } else {
                audioManager.setSpeakerphoneOn(false);// 关闭扬声器
                // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
                audioManager.setMode(AudioManager.MODE_IN_CALL);
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            }

            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (ChatMediaPlayer.this.onPlayListener != null) {
                        ChatMediaPlayer.this.onPlayListener.onStart(oriFilePath);
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayVoice();
                }

            });

            mediaPlayer.start();

            readySensor();

            if (mute) {
                mediaPlayer.setVolume(0, 0);
            }

            playing = true;
        } catch (Exception e) {
            PLogger.printThrowable(e);

            try {
                mediaPlayer.release();
            } catch (Exception e1) {

            }

            mediaPlayer = null;
        }
    }

    /**
     * 停止播放音频。
     */
    public void stopPlayVoice() {
        playing = false;

        stopSensor();

        if (onPlayListener != null) {
            onPlayListener.onStop(oriFilePath);
            onPlayListener = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        oriFilePath = null;
    }

    /**
     * 是否正在播放音频。
     *
     * @return 播放状态。
     */
    public boolean isPlaying() {
        return playing;
    }

    public OnPlayListener getOnPlayListener() {
        return onPlayListener;
    }

    /**
     * 监听音频播放。
     */
    public interface OnPlayListener {
        /**
         * 开始播放。
         *
         * @param filePath 文件路径。
         */
        void onStart(String filePath);

        /**
         * 停止播放。
         *
         * @param filePath 文件路径。
         */
        void onStop(String filePath);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MEDIA_RECORDER_EVENT_Start:
                handler.sendEmptyMessageDelayed(MEDIA_RECORDER_EVENT_Start, frequency);
                break;

            case MEDIA_RECORDER_EVENT_Stop:
                handler.removeMessages(MEDIA_RECORDER_EVENT_Start);
                break;
        }

        return false;
    }

    public static boolean ispeaker() {
        return false;
    }

    SensorManager _sensorManager = null; // 传感器管理器
    Sensor mProximiny = null; // 传感器实例
    float f_proximiny; // 当前传感器距离


    private void readySensor() {
        if (_sensorManager == null) {
            _sensorManager = (SensorManager) App.getActivity().getSystemService(Context.SENSOR_SERVICE);
            mProximiny = _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        _sensorManager.registerListener(this, mProximiny, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopSensor() {
        if (_sensorManager != null) {
            _sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            f_proximiny = event.values[0];
            PLogger.d("" + f_proximiny + " | " + mProximiny.getMaximumRange());

            AudioManager audioManager = (AudioManager) App.context.getSystemService(Context.AUDIO_SERVICE);

            if (f_proximiny == mProximiny.getMaximumRange()) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(true);
            } else {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /* -------------------------------视频播放---------------------------------- */

    /**
     * 播放一个音频。
     *
     * @param mute             是否为静音播放
     * @param textureVideoView 播放控件
     * @param filePath         视频文件
     */
    public synchronized void playVideo(final boolean mute, final TextureVideoView textureVideoView, final String filePath) {
        PLogger.d("filePath:" + filePath);
        if (TextUtils.isEmpty(filePath)) return;

        if (!FileUtil.isURL(filePath)) {
            playLocalVideo(mute, textureVideoView, filePath);
            return;
        }

//        ModuleMgr.getChatMgr().reqVideo(filePath, new DownloadListener() {
//            @Override
//            public void onStart(String url, String filePath) {
//            }
//
//            @Override
//            public void onProcess(String url, int process, long size) {
//            }
//
//            @Override
//            public void onSuccess(String url, String filePath) {
//                playLocalVideo(mute, textureVideoView, filePath);
//            }
//
//            @Override
//            public void onFail(String url, Throwable throwable) {
//            }
//        });
    }

    /**
     * 播放视频
     */
    public void playLocalVideo(boolean mute, final TextureVideoView textureVideoView, String filePath) {
        PLogger.d("filePath:" + filePath);
        if (TextUtils.isEmpty(filePath)) return;

        textureVideoView.setVideoPath(filePath);
        textureVideoView.setMute(mute);
        textureVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                textureVideoView.start();
            }
        });
        textureVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                PToast.showShort("视频播放异常");
                return true;
            }
        });
    }
}
