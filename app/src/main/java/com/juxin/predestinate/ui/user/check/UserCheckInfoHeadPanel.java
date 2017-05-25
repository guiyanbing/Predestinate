package com.juxin.predestinate.ui.user.check;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.chat.MessageRet;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 查看用户资料头部panel
 */
public class UserCheckInfoHeadPanel extends BasePanel implements IMProxy.SendCallBack {

    private final int channel;
    private UserDetail userDetail; // 用户资料
    private TextView user_follow;
    private ImageView iv_follow;  // 关注星标
    private int followType = 1;   // 关注、取消关注

    private String distance, online;
    private int follow;

    public UserCheckInfoHeadPanel(Context context, int channel, UserDetail userProfile) {
        super(context);
        setContentView(R.layout.p1_user_checkinfo_header);
        this.channel = channel;
        this.userDetail = userProfile;

        initData();
        initView();
    }

    private void initData() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            distance = "5km以内";
            online = getContext().getString(R.string.user_online);
            return;
        }

        if (userDetail == null) {
            PToast.showShort(getContext().getString(R.string.user_other_info_req_fail));
            return;
        }
        distance = userDetail.getDistance() + "km";
        online = userDetail.getOnline_text();
        follow = userDetail.getFollowmecount();
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
        FrameLayout fl_topN = (FrameLayout) findViewById(R.id.fl_top_n);
        TextView tv_topN = (TextView) findViewById(R.id.tv_top_n);
        ImageView iv_vip = (ImageView) findViewById(R.id.iv_vip);
        img_header.setOnClickListener(listener);
        user_follow = (TextView) findViewById(R.id.tv_guanzhu);
        iv_follow = (ImageView) findViewById(R.id.iv_guanzhu);

        if (channel == CenterConstant.USER_CHECK_INFO_OTHER) {
            findViewById(R.id.ll_guanzhu).setOnClickListener(listener);
            if (userDetail != null) {
                if (userDetail.isFollow())
                    iv_follow.setImageResource(R.drawable.f1_followed_star);
            }
        }

        ImageLoader.loadRoundCorners(getContext(), userDetail.getAvatar(), 10, img_header);
        if (userDetail.isMan()) {
            rl_header.setBackgroundColor(getContext().getResources().getColor(R.color.picker_blue_color));
            img_gender.setImageResource(R.drawable.f1_sex_male_2);
        }
        user_age.setText(getContext().getString(R.string.user_info_age, userDetail.getAge()));
        user_id.setText("ID:" + userDetail.getUid());
        user_height.setText(userDetail.getHeight() + "cm");
        user_distance.setText(distance);
        user_online_time.setText(online);
        user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
        iv_vip.setVisibility(userDetail.isVip() ? View.VISIBLE : View.GONE);
        fl_topN.setVisibility(userDetail.getTopN() <= 0 ? View.GONE : View.VISIBLE);
        tv_topN.setText("TOP" + userDetail.getTopN());
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.img_header:
                    if (TextUtils.isEmpty(userDetail.getAvatar())) return;
                    UIShow.showPhotoOfBigImg((FragmentActivity) App.getActivity(), userDetail.getAvatar());
                    break;

                case R.id.ll_guanzhu:       // 关注星标
                    handleFollow();
                    break;
            }
        }
    };

    // ---------------------------------------- 关注消息 --------------------------------------
    private void handleFollow() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) return;
        followType = 1;
        if (userDetail.isFollow()) {  // 已关注
            followType = 2;
        }
        ModuleMgr.getChatMgr().sendAttentionMsg(userDetail.getUid(), "", userDetail.getKf_id(), followType, this);
    }

    private void handleFollowSuccess() {
        switch (followType) {
            case 1:
                follow += 1;
                PToast.showShort(getContext().getResources().getString(R.string.user_info_follow_suc));
                iv_follow.setImageResource(R.drawable.f1_followed_star);
                user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
                if (userDetail != null) {
                    userDetail.setIsfollow(1);
                    userDetail.setFollowmecount(follow);
                }
                break;

            case 2:
                follow -= 1;
                PToast.showShort(getContext().getResources().getString(R.string.user_info_unfollow_suc));
                iv_follow.setImageResource(R.drawable.f1_follow_star);
                user_follow.setText(getContext().getString(R.string.user_info_follow_count, follow));
                if (userDetail != null) {
                    userDetail.setIsfollow(0);
                    userDetail.setFollowmecount(follow);
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
