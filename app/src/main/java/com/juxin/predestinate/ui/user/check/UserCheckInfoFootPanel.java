package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.ui.user.check.edit.AlbumHorizontalPanel;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 查看他人资料底部panel
 */
public class UserCheckInfoFootPanel extends BaseViewPanel {
    private final int channel;
    private UserDetail userDetail;

    private LinearLayout albumLayout, videoLayout;
    private AlbumHorizontalPanel albumPanel, videoPanel;

    public UserCheckInfoFootPanel(Context context, int channel, UserDetail userDetail) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_footer);
        this.channel = channel;
        this.userDetail = userDetail;
        initView();
    }

    private void initView() {
        albumLayout = (LinearLayout) findViewById(R.id.album_item);
        videoLayout = (LinearLayout) findViewById(R.id.video_item);
    }

    /**
     * 设置右滑退出忽略view
     */
    public void setSlideIgnoreView(BaseActivity activity) {
//        activity.addIgnoredView(albumLayout);
    }

    private final NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
            }
        }
    };
}
