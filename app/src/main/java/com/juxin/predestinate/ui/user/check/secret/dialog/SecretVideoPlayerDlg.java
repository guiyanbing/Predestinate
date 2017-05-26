package com.juxin.predestinate.ui.user.check.secret.dialog;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.view.DownloadProgressView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserVideo;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.TextureVideoView;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.ui.user.util.CenterConstant;

/**
 * 私密视频播放页
 * Created by Su on 2017/5/17.
 */
public class SecretVideoPlayerDlg extends BaseActivity implements View.OnClickListener {
    private TextureVideoView tvv_player;
    private ImageView iv_pic, iv_start;
    private TextView tv_video_time;
    private DownloadProgressView progress_bar;
    private UserVideo userVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_secret_video_player);
        initView();
    }

    private void initView() {
        userVideo = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_VIDEO_KEY);
        if (userVideo == null) {
            PToast.showShort(getString(R.string.user_info_check_video_fail));
            return;
        }

        iv_pic = (ImageView) findViewById(R.id.iv_cover);
        iv_start = (ImageView) findViewById(R.id.btn_start);
        tvv_player = (TextureVideoView) findViewById(R.id.tvv_player);
        progress_bar = (DownloadProgressView) findViewById(R.id.progress_bar);
        tv_video_time = (TextView) findViewById(R.id.tv_video_time);

        iv_start.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        ImageLoader.loadFitCenter(this, userVideo.getPic(), iv_pic);
        tv_video_time.setText(TimeUtil.getLongToMinuteTime(userVideo.getDuration() * 1000l));
    }


    /**
     * 下载小视频
     */
    private void downloadVideo() {
        String filePath = DirType.getUploadDir() + System.currentTimeMillis() + ".mp4";
        ModuleMgr.getHttpMgr().download(userVideo.getVideo(), filePath, new DownloadListener() {
            @Override
            public void onStart(String url, String filePath) {
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setProgress(0);
            }

            @Override
            public void onProcess(String url, int process, long size) {
                progress_bar.setProgress(process);
            }

            @Override
            public void onSuccess(String url, String filePath) {
                progress_bar.setVisibility(View.GONE);
                playLocalVideo(filePath);
            }

            @Override
            public void onFail(String url, Throwable throwable) {
                progress_bar.setVisibility(View.GONE);
                resetPlay();
                PToast.showShort(getString(R.string.user_info_check_video_fail));
            }
        });
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
            case R.id.btn_start:
                onViewStart();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void onViewStart() {
        iv_start.setVisibility(View.GONE);
        iv_pic.setVisibility(View.GONE);
        downloadVideo();
    }
}
