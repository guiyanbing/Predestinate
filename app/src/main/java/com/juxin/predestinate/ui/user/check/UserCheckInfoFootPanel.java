package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.detail.UserPhoto;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.ui.user.util.AlbumHorizontalPanel;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * 查看他人资料底部panel
 */
public class UserCheckInfoFootPanel extends BaseViewPanel {
    private final int channel;
    private UserDetail userDetail;  // 个人资料
    private UserProfile userProfile;// TA人资料

    private LinearLayout albumLayout, videoLayout, giftLayout;
    private TextView tv_album;
    private AlbumHorizontalPanel albumPanel, videoPanel, giftPanel;

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
            return;
        }

        if (userProfile == null) {
            PToast.showShort(getContext().getString(R.string.user_other_info_req_fail));
            return;
        }
        userPhotos = userDetail.getUserPhotos();
        albumNum = userProfile.getUserPhotos().size();
    }

    private void initView() {
        tv_album = (TextView) findViewById(R.id.album_num);
        albumLayout = (LinearLayout) findViewById(R.id.album_item);
        videoLayout = (LinearLayout) findViewById(R.id.video_item);
        giftLayout = (LinearLayout) findViewById(R.id.gift_item);

        // 照片列表
        albumPanel = new AlbumHorizontalPanel(getContext(), channel, AlbumHorizontalPanel.EX_HORIZONTAL_ALBUM, (Serializable) userPhotos);
        albumLayout.addView(albumPanel.getContentView());

        tv_album.setText(String.valueOf(albumNum));
    }

    // 添加右滑退出忽略view
    public void setSlideIgnoreView(BaseActivity activity) {
        activity.addIgnoredView(albumLayout);
        activity.addIgnoredView(videoLayout);
        activity.addIgnoredView(giftLayout);
    }

    private final NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
            }
        }
    };
}
