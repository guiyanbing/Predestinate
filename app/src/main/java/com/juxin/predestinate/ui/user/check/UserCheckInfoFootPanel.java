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
import com.juxin.predestinate.bean.center.user.detail.UserPhoto;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import com.juxin.predestinate.ui.user.util.AlbumHorizontalPanel;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * 查看他人资料底部panel
 */
public class UserCheckInfoFootPanel extends BasePanel {

    private final int channel;
    private UserDetail userDetail;  // 个人资料
    private UserProfile userProfile;// TA人资料

    private LinearLayout albumLayout, videoLayout, chatPriceLayout;
    private TextView tv_album, tv_video_price, tv_audio_price;
    private AlbumHorizontalPanel albumPanel, videoPanel;
    private ImageView iv_auth_photo, iv_auth_phone, iv_auth_video; // 认证
    private boolean isAuthPhoto, isAuthPhone, isAuthVideo;

    private int albumNum;
    private List<UserPhoto> userPhotos;

    public UserCheckInfoFootPanel(Context context, int channel, UserProfile userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_footer);
        this.channel = channel;
        this.userProfile = userProfile;

        initData();
        initView();
    }

    private void initData() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            userPhotos = userDetail.getUserPhotos();
            albumNum = userDetail.getUserPhotos().size();
            isAuthPhone = userDetail.isVerifyCellphone();
            isAuthVideo = ModuleMgr.getCommonMgr().getVideoVerify().isVerifyVideo();
            return;
        }

        if (userProfile == null) {
            PToast.showShort(getContext().getString(R.string.user_other_info_req_fail));
            return;
        }
        userPhotos = userProfile.getUserPhotos();
        albumNum = userProfile.getUserPhotos().size();
        isAuthPhone = userProfile.isVerifyCellphone();
    }

    private void initView() {
        tv_album = (TextView) findViewById(R.id.album_num);
        chatPriceLayout = (LinearLayout) findViewById(R.id.ll_chat_price);
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
        albumPanel = new AlbumHorizontalPanel(getContext(), channel, AlbumHorizontalPanel.EX_HORIZONTAL_ALBUM, (Serializable) userPhotos);
        albumLayout.addView(albumPanel.getContentView());

        tv_album.setText(String.valueOf(albumNum));
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
        iv_auth_photo.setVisibility(isAuthPhoto ? View.VISIBLE : View.GONE);
        iv_auth_phone.setVisibility(isAuthPhone ? View.VISIBLE : View.GONE);
        iv_auth_video.setVisibility(isAuthVideo ? View.VISIBLE : View.GONE);
    }

    public void refreshView(UserDetail userDetail) {
        if (userDetail == null) return;

        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            albumNum = userDetail.getUserPhotos().size();
            tv_album.setText(String.valueOf(albumNum));
            albumPanel.refresh(userDetail);
            refreshAuth();
        }
    }

    /**
     * 刷新聊天价格
     */
    public void refreshChatPrice(VideoConfig config) {
        if (config == null) return;
        chatPriceLayout.setVisibility(View.VISIBLE);
        tv_video_price.setText(getContext().getString(R.string.user_info_chat_video, config.getVideoPrice()));
        tv_audio_price.setText(getContext().getString(R.string.user_info_chat_voice, config.getAudioPrice()));
        iv_auth_video.setVisibility(config.isVerifyVideo() ? View.VISIBLE : View.GONE);
    }

    private final NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_video:
                    UIShow.showUserSecretAct(getContext(), userProfile);
                    break;
            }
        }
    };
}
