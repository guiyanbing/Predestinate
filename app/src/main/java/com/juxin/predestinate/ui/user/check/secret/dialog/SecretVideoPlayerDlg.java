package com.juxin.predestinate.ui.user.check.secret.dialog;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.view.DownloadProgressView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.TextureVideoView;
import com.juxin.predestinate.module.util.TimerUtil;

/**
 * 私密视频播放页
 * Created by Su on 2017/5/17.
 */

public class SecretVideoPlayerDlg extends BaseActivity implements View.OnClickListener {
    private TextureVideoView tvv_player;
    private ImageView iv_pic, iv_start;
    private DownloadProgressView progress_bar;
    private String mp4Path = "";
    private String picPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_secret_video_player);

        initView();
    }


    private void initView() {
        iv_pic = (ImageView) findViewById(R.id.iv_photo_video_pic);
        iv_start = (ImageView) findViewById(R.id.iv_photo_video_start);
        tvv_player = (TextureVideoView) findViewById(R.id.tvv_photo_video_player);
        progress_bar = (DownloadProgressView) findViewById(R.id.progress_bar_photo_view);
        iv_pic.setVisibility(View.VISIBLE);

        iv_start.setOnClickListener(this);
        findViewById(R.id.iv_photo_view_back).setOnClickListener(this);

        ImageLoader.loadFitCenter(this, picPath, iv_pic);
    }


    /**
     * 下载小视频
     */
    private void downloadVideo(String url) {
//        ModuleMgr.getHttpMgr().reqVideo(url, new NetInterface.OnTransmissionListener() {
//            @Override
//            public void onProcess(String s, int i, long l) {
//                progress_bar.setProgress(i);
//            }
//
//            @Override
//            public void onStart(String s, String s1) {
//                progress_bar.setVisibility(View.VISIBLE);
//                progress_bar.setProgress(0);
//            }
//
//            @Override
//            public void onStop(String s, String s1, int i) {
//                progress_bar.setVisibility(View.GONE);
//                playLocalVideo(s1);
//            }
//
//        });
    }

    /**
     * 播放视频
     */
    public void playLocalVideo(final String filePath) {
        tvv_player.setVideoPath(filePath);
        //tvv_player.start();
        tvv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                tvv_player.start();
                iv_start.setVisibility(View.GONE);
                iv_pic.setVisibility(View.GONE);
                TimerUtil.beginTime(new TimerUtil.CallBack() {//短暂延时之后取消遮罩，防止屏幕闪烁
                    @Override
                    public void call() {
                        iv_pic.setVisibility(View.GONE);
                    }
                }, 200);
            }
        });
        tvv_player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                iv_start.setVisibility(View.VISIBLE);
                resetPlay();
                return true;
            }
        });

        tvv_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvv_player.isPlaying()) {
                    tvv_player.pause();
                } else {
                    tvv_player.start();
                }
            }
        });
    }

    private void resetPlay() {
        tvv_player.stopPlayback();
        iv_pic.setVisibility(View.VISIBLE);
        iv_start.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photo_video_start:
                onViewStart();
                break;
            case R.id.iv_photo_view_back:
                finish();
                break;
        }
    }

    private void onViewStart() {
        iv_start.setVisibility(View.GONE);
        iv_pic.setVisibility(View.GONE);
        downloadVideo(mp4Path);
    }
}
