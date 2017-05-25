package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import com.juxin.predestinate.ui.user.check.self.info.UserInfoPanel;
import com.juxin.predestinate.ui.user.util.AlbumHorizontalPanel;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.io.Serializable;

/**
 * 查看他人资料底部panel
 */
public class UserCheckInfoFootPanel extends BasePanel {

    private final int channel;
    private UserDetail userDetail;  // 用户资料

    private LinearLayout albumLayout, videoLayout, chatLayout;
    private TextView tv_album, tv_video_price, tv_audio_price;
    private AlbumHorizontalPanel albumPanel, videoPanel;
    private ImageView iv_auth_photo, iv_auth_phone, iv_auth_video; // 认证

    public UserCheckInfoFootPanel(Context context, int channel, UserDetail userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_footer);
        this.channel = channel;
        this.userDetail = userProfile;

        initView();
    }

    private void initView() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        }
        if (userDetail == null) {
            PToast.showShort(getContext().getString(R.string.user_other_info_req_fail));
            return;
        }

        LinearLayout info_container = (LinearLayout) findViewById(R.id.ll_info_container);
        LinearLayout ll_secret_album = (LinearLayout) findViewById(R.id.ll_secret_album);
        LinearLayout ll_secret_video = (LinearLayout) findViewById(R.id.ll_secret_video);
        tv_album = (TextView) findViewById(R.id.album_num);
        chatLayout = (LinearLayout) findViewById(R.id.item_chat);
        tv_video_price = (TextView) findViewById(R.id.tv_video_price);
        tv_audio_price = (TextView) findViewById(R.id.tv_audio_price);
        albumLayout = (LinearLayout) findViewById(R.id.album_item);
        videoLayout = (LinearLayout) findViewById(R.id.video_item);
        iv_auth_photo = (ImageView) findViewById(R.id.iv_auth_photo);
        iv_auth_phone = (ImageView) findViewById(R.id.iv_auth_phone);
        iv_auth_video = (ImageView) findViewById(R.id.iv_auth_video);
        findViewById(R.id.ll_video).setOnClickListener(listener);
        refreshAuth();  // 认证状态

        // 照片列表
        if (userDetail.getUserPhotos().size() > 0) {
            ll_secret_album.setVisibility(View.VISIBLE);
            albumPanel = new AlbumHorizontalPanel(getContext(), channel, AlbumHorizontalPanel.EX_HORIZONTAL_ALBUM, (Serializable) userDetail.getUserPhotos());
            albumLayout.addView(albumPanel.getContentView());
            tv_album.setText(String.valueOf(userDetail.getUserPhotos().size()));
        }

        // 视频列表
        if (userDetail.getUserVideos().size() > 0) {
            ll_secret_video.setVisibility(View.VISIBLE);
        }

        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            ll_secret_album.setVisibility(View.VISIBLE);
            ll_secret_video.setVisibility(View.VISIBLE);
            return;
        }

        // TA人详细信息列表
        info_container.setVisibility(View.VISIBLE);
        UserInfoPanel infoPanel = new UserInfoPanel(getContext(), userDetail);
        info_container.addView(infoPanel.getContentView());
    }

    // 添加右滑退出忽略view
    public void setSlideIgnoreView(BaseActivity activity) {
        activity.addIgnoredView(albumLayout);
        activity.addIgnoredView(videoLayout);
    }

    /**
     * 认证状态
     */
    public void refreshAuth() {
        boolean isAuthVideo = false;
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            isAuthVideo = ModuleMgr.getCommonMgr().getVideoVerify().isVerifyVideo();
        }

        iv_auth_photo.setVisibility(userDetail.isVerifyIdcard() ? View.VISIBLE : View.GONE);
        iv_auth_phone.setVisibility(userDetail.isVerifyCellphone() ? View.VISIBLE : View.GONE);
        iv_auth_video.setVisibility(isAuthVideo ? View.VISIBLE : View.GONE);
        chatLayout.setVisibility(isAuthVideo ? View.VISIBLE : View.GONE);
    }

    public void refreshView(UserDetail userDetail) {
        if (userDetail == null) return;

        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            tv_album.setText(String.valueOf(userDetail.getUserPhotos().size()));
            albumPanel.refresh(userDetail);
            refreshAuth();
        }
    }

    /**
     * 刷新聊天价格
     */
    public void refreshChatPrice(VideoConfig config) {
        if (config == null || !config.isVerifyVideo()) return;

        chatLayout.setVisibility(config.isVerifyVideo() ? View.VISIBLE : View.GONE);
        tv_video_price.setText(getContext().getString(R.string.user_info_chat_video, config.getVideoPrice()));
        tv_audio_price.setText(getContext().getString(R.string.user_info_chat_voice, config.getAudioPrice()));
        iv_auth_video.setVisibility(config.isVerifyVideo() ? View.VISIBLE : View.GONE);
    }

    private final NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_video:
                    UIShow.showUserSecretAct(getContext(), userDetail);
                    break;
            }
        }
    };
}
