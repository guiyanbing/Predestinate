package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView img_header, img_vip, img_gender;
    private TextView user_alias, user_id, user_gender,
            user_age, user_height, user_addr,
            user_status, user_distance;

    public UserCheckInfoHeadPanel(Context context, int channel, UserDetail userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_header);
        this.channel = channel;
        this.userProfile = userProfile;

        initView();
        initData();
    }

    private void initView() {
        img_header = (ImageView) findViewById(R.id.img_header);
        img_vip = (ImageView) findViewById(R.id.img_vip);
        user_alias = (TextView) findViewById(R.id.user_alias);
        user_id = (TextView) findViewById(R.id.user_id);
        img_gender = (ImageView) findViewById(R.id.user_gender_img);
        user_gender = (TextView) findViewById(R.id.user_gender);
        user_age = (TextView) findViewById(R.id.user_age);
        user_height = (TextView) findViewById(R.id.user_height);
        user_addr = (TextView) findViewById(R.id.user_addr);
        user_status = (TextView) findViewById(R.id.user_status);
        user_distance = (TextView) findViewById(R.id.user_distance);
    }

    private void initData() {
        ImageLoader.loadRoundCorners(getContext(), userProfile.getAvatar(), 15, img_header);
        if (!userProfile.isVip()) img_vip.setVisibility(View.GONE);

        user_alias.setText(userProfile.getNickname() + "昵称");
        user_id.setText("ID:" + userProfile.getUid());
    }
}
