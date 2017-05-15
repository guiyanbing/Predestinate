package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 查看用户资料头部panel
 */
public class UserCheckInfoHeadPanel extends BaseViewPanel implements RequestComplete {
    private final int channel;
    private UserProfile userProfile; // TA人资料
    private TextView user_follow;
    private ImageView iv_follow;  // 关注星标

    private String avatarUrl, distance, online;
    private long uid;
    private boolean isMan;
    private int age, height, follow;

    public UserCheckInfoHeadPanel(Context context, int channel, UserProfile userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_header);
        this.channel = channel;
        this.userProfile = userProfile;

        initData();
        initView();
    }

    private void initData() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
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
        online = userProfile.getOnline_text();
        follow = userProfile.getFollowCont();
    }

    private void initView() {
        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.check_header); // 头部背景
        ImageView img_header = (ImageView) findViewById(R.id.img_header);
        ImageView img_gender = (ImageView) findViewById(R.id.iv_sex);
        TextView user_id = (TextView) findViewById(R.id.user_id);
        TextView user_age = (TextView) findViewById(R.id.tv_age);
        TextView user_height = (TextView) findViewById(R.id.tv_height);
        TextView user_distance = (TextView) findViewById(R.id.tv_distance);
        TextView user_online_time = (TextView) findViewById(R.id.tv_last_online);
        user_follow = (TextView) findViewById(R.id.tv_guanzhu);
        iv_follow = (ImageView) findViewById(R.id.iv_guanzhu);

        if (channel == CenterConstant.USER_CHECK_INFO_OTHER) {
            findViewById(R.id.ll_guanzhu).setOnClickListener(listener);
            if (userProfile != null) {
                if (userProfile.isFollowed())
                    iv_follow.setImageResource(R.drawable.f1_followed_star);
            }
        }

        ImageLoader.loadRoundCorners(getContext(), avatarUrl, 10, img_header);
        if (isMan) {
            rl_header.setBackgroundColor(getContext().getResources().getColor(R.color.picker_blue_color));
            img_gender.setImageResource(R.drawable.f1_sex_male_2);
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
                case R.id.ll_guanzhu:       // 关注星标
                    if (userProfile == null) return;
                    if (userProfile.isFollowed()) {  // 已关注
                        ModuleMgr.getCommonMgr().unfollow(userProfile.getUid(), UserCheckInfoHeadPanel.this);
                        return;
                    }
                    ModuleMgr.getCommonMgr().follow(userProfile.getUid(), UserCheckInfoHeadPanel.this);

//                    ModuleMgr.getChatMgr().sendAttentionMsg();
                    break;
            }
        }
    };

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.follow) {
            follow += 1;
            iv_follow.setImageResource(R.drawable.f1_followed_star);
            user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
            if (userProfile != null) {
                userProfile.setIsFollowed(1);
            }
            IMProxy.getInstance().send(null, new IMProxy.SendCallBack() {
                @Override
                public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {

                }

                @Override
                public void onSendFailed(NetData data) {

                }
            });
            return;
        }

        if (response.getUrlParam() == UrlParam.unfollow) {
            follow -= 1;
            iv_follow.setImageResource(R.drawable.f1_follow_star);
            user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
            if (userProfile != null) {
                userProfile.setIsFollowed(0);
            }
            return;
        }

    }
}
