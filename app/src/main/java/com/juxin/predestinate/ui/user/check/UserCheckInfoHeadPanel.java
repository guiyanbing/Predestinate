package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 查看用户资料头部panel
 */
public class UserCheckInfoHeadPanel extends BaseViewPanel {
    private final int channel;
    private UserDetail userDetail;   // 个人资料
    private UserProfile userProfile; // TA人资料

    private RelativeLayout rl_header;   // 头部背景
    private LinearLayout ll_guanzhu;    // 关注
    private ImageView img_header, img_gender;
    private TextView user_height, user_id, user_online_time,
            user_age, user_distance, user_follow;

    private String avatarUrl, distance, online;
    private long uid;
    private boolean isMan;
    private int age, height, follow;

    public UserCheckInfoHeadPanel(Context context, int channel, UserDetail userDetail, UserProfile userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_header);
        this.channel = channel;
        this.userDetail = userDetail;
        this.userProfile = userProfile;

        initData();
        initView();
    }

    private void initData() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            if (userDetail == null) return;
            avatarUrl = userDetail.getAvatar();
            uid = userDetail.getUid();
            isMan = userDetail.isMan();
            age = userDetail.getAge();
            height = userDetail.getHeight();
            distance = "5km以内";
            online = getContext().getString(R.string.user_online);
            return;
        }

        if (userProfile == null) {
            PToast.showShort(getContext().getString(R.string.user_other_info_req_fail));
            return;
        }
        avatarUrl = userProfile.getAvatar();
        uid = userProfile.getUid();
        isMan = userProfile.isMan();
        age = userProfile.getAge();
        height = userProfile.getHeight();
        distance = userProfile.getDistance();
        online = TimeUtil.formatBeforeTimeWeek(userProfile.getLastOnlineTime());
        follow = userProfile.getFollowCont();
    }

    private void initView() {
        rl_header = (RelativeLayout) findViewById(R.id.check_header);
        ll_guanzhu = (LinearLayout) findViewById(R.id.ll_guanzhu);
        img_header = (ImageView) findViewById(R.id.img_header);
        img_gender = (ImageView) findViewById(R.id.iv_sex);
        user_id = (TextView) findViewById(R.id.user_id);
        user_age = (TextView) findViewById(R.id.tv_age);
        user_height = (TextView) findViewById(R.id.tv_height);
        user_distance = (TextView) findViewById(R.id.tv_distance);
        user_online_time = (TextView) findViewById(R.id.tv_last_online);
        user_follow = (TextView) findViewById(R.id.tv_guanzhu);

        if (channel == CenterConstant.USER_CHECK_INFO_OTHER) {
            ll_guanzhu.setOnClickListener(listener);
        }

        ImageLoader.loadRoundCorners(getContext(), avatarUrl, 10, img_header);
        if (isMan) {
            rl_header.setBackgroundColor(getContext().getResources().getColor(R.color.picker_blue_color));
            img_gender.setImageDrawable(getContext().getResources().getDrawable(R.drawable.f1_sex_male_2));
        }
        user_age.setText(getContext().getString(R.string.user_info_age, age));
        user_id.setText("ID:" + uid);
        user_height.setText(height + "cm");
        user_distance.setText(distance);
        user_online_time.setText(online);
        user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_guanzhu:  // 关注星标
                    break;
            }
        }
    };
}
