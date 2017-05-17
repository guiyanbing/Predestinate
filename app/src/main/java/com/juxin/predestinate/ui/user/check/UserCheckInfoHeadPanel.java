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
import com.juxin.predestinate.module.local.chat.MessageRet;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 查看用户资料头部panel
 */
public class UserCheckInfoHeadPanel extends BaseViewPanel implements IMProxy.SendCallBack {
    private final int channel;
    private UserProfile userProfile; // TA人资料
    private TextView user_follow;
    private ImageView iv_follow, iv_vip;  // 关注星标
    private int followType = 1;   // 关注、取消关注

    private String avatarUrl, distance, online;
    private long uid;
    private boolean isMan, isvip;
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
            isvip = userDetail.isVip();
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
        isvip = userProfile.isVip();
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
        iv_vip = (ImageView) findViewById(R.id.iv_vip);

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
        iv_vip.setVisibility(isvip ? View.VISIBLE : View.GONE);
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_guanzhu:       // 关注星标
                    handleFollow();
                    break;
            }
        }
    };

    // ---------------------------------------- 关注消息 --------------------------------------
    private void handleFollow() {
        if (userProfile == null) return;
        followType = 1;
        if (userProfile.isFollowed()) {  // 已关注
            followType = 2;
        }
        ModuleMgr.getChatMgr().sendAttentionMsg(userProfile.getUid(), "", userProfile.getKf_id(), followType, this);
    }

    private void handleFollowSuccess() {
        switch (followType) {
            case 1:
                follow += 1;
                PToast.showShort(getContext().getResources().getString(R.string.user_info_follow_suc));
                iv_follow.setImageResource(R.drawable.f1_followed_star);
                user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
                if (userProfile != null) {
                    userProfile.setIsFollowed(1);
                }
                break;

            case 2:
                follow -= 1;
                PToast.showShort(getContext().getResources().getString(R.string.user_info_unfollow_suc));
                iv_follow.setImageResource(R.drawable.f1_follow_star);
                user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
                if (userProfile != null) {
                    userProfile.setIsFollowed(0);
                }
                break;
        }
    }

    private void handleFollowFail() {
        String msg = "";
        switch (followType) {
            case 1:
                msg = getContext().getResources().getString(R.string.user_info_follow_fail);
                break;

            case 2:
                msg = getContext().getResources().getString(R.string.user_info_unfollow_fail);
                break;
        }
        PToast.showShort(msg);
    }

    /**
     * 发关注消息结果回调
     */
    @Override
    public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
        MessageRet messageRet = new MessageRet();
        messageRet.parseJson(contents);

        if (messageRet.getS() == 0) {
            handleFollowSuccess();
        } else {
            handleFollowFail();
        }
    }

    @Override
    public void onSendFailed(NetData data) {
        handleFollowFail();
    }
}
