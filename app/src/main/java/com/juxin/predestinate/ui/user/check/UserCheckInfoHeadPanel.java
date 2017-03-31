package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.utils.BitmapUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIUtil;

/**
 * 查看用户资料头部panel
 */
public class UserCheckInfoHeadPanel extends BaseViewPanel {
    private final int channel;
    private UserDetail userProfile;

    private ImageView img_header, img_vip;
    private TextView user_alias, user_id, user_gender,
            user_age, user_addr,
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.check_header);
        Bitmap bitmap = BitmapUtils.getDecodeBitmap(getContext(), R.drawable.p1_theme_bg, 320, 240);
        UIUtil.setBackground(layout, new BitmapDrawable(null, bitmap));

        img_header = (ImageView) findViewById(R.id.img_header);
        img_vip = (ImageView) findViewById(R.id.img_vip);
        user_alias = (TextView) findViewById(R.id.user_alias);
        user_id = (TextView) findViewById(R.id.user_id);
        user_gender = (TextView) findViewById(R.id.user_gender);
        user_age = (TextView) findViewById(R.id.user_age);
        user_addr = (TextView) findViewById(R.id.user_addr);
        user_status = (TextView) findViewById(R.id.user_status);
        user_distance = (TextView) findViewById(R.id.user_distance);
    }

    private void initData() {
        ImageLoader.loadRoundCorners(getContext(), userProfile.getAvatar(), 10, img_header);
        if (!userProfile.isVip()) img_vip.setVisibility(View.GONE);
        user_alias.setText(userProfile.getNickname());
        user_gender.setText(userProfile.getGender() == 1 ? "男，" : "女，");
        user_age.setText(userProfile.getAge() + "，");
        user_addr.setText(userProfile.getAddress());
        user_id.setText("ID:" + userProfile.getUid());
    }
}
