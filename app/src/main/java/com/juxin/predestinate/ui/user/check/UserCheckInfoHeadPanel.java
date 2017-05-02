package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * 查看用户资料头部panel
 */
public class UserCheckInfoHeadPanel extends BaseViewPanel {
    private final int channel;
    private UserDetail userProfile;

    private RelativeLayout rl_header;   // 头部背景
    private ImageView img_header;
    private TextView user_alias, user_id, user_gender,
            user_age, user_addr,
            user_distance;

    public UserCheckInfoHeadPanel(Context context, int channel, UserDetail userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_header);
        this.channel = channel;
        this.userProfile = userProfile;

        initView();
        initData();
    }

    private void initView() {
        rl_header = (RelativeLayout) findViewById(R.id.check_header);
        img_header = (ImageView) findViewById(R.id.img_header);
        user_id = (TextView) findViewById(R.id.user_id);
    }

    private void initData() {
        ImageLoader.loadRoundCorners(getContext(), userProfile.getAvatar(), 10, img_header);
        user_id.setText("ID:" + userProfile.getUid());
    }
}
