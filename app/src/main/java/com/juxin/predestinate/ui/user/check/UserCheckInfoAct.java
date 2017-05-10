package com.juxin.predestinate.ui.user.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.mumu.bean.message.Msg;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.message.MsgType;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 查看用户资料详情
 * Created by Su on 2016/5/30.
 */
public class UserCheckInfoAct extends BaseActivity implements MsgMgr.IObserver, RequestComplete {
    private int channel;  // 查看用户资料区分Tag，默认查看自己个人资料
    private UserDetail userDetail;   // 自己资料
    private UserProfile userProfile; // TA人资料

    private LinearLayout container, videoBottom, voiceBottom;

    /**********
     * panel
     ***********/
    private UserCheckInfoHeadPanel headPanel;
    private UserCheckInfoFootPanel footPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_info_layout);

        initData();
        initView();
    }

    private void initData() {
        channel = getIntent().getIntExtra(CenterConstant.USER_CHECK_INFO_KEY, CenterConstant.USER_CHECK_INFO_OWN);

        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            return;
        }
        userProfile = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
        ModuleMgr.getCenterMgr().reqVideoChatConfig(userProfile.getUid(), this); // 请求音视频开关配置
    }

    private void initView() {
        initTitle();
        container = (LinearLayout) findViewById(R.id.container);
        headPanel = new UserCheckInfoHeadPanel(this, channel, userDetail, userProfile);
        container.addView(headPanel.getContentView());

        footPanel = new UserCheckInfoFootPanel(this, channel, userDetail, userProfile);
        footPanel.setSlideIgnoreView(this);
        container.addView(footPanel.getContentView());
        initBottom();
    }

    private void initTitle() {
        setTitleBackground(R.color.transparent);
        setTitleLeftImg(R.drawable.p1_back_white_btn, listener);
        if (channel == CenterConstant.USER_CHECK_INFO_OTHER)
            setTitleRightImg(R.drawable.f1_more_vertical_dot, listener);
    }

    // 底部功能按钮展示逻辑
    private void initBottom() {
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) return;

        videoBottom = (LinearLayout) findViewById(R.id.ll_userinfo_bottom_video);
        voiceBottom = (LinearLayout) findViewById(R.id.ll_userinfo_bottom_voice);

        videoBottom.setOnClickListener(listener);
        voiceBottom.setOnClickListener(listener);
        findViewById(R.id.iv_gift).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_gift).setOnClickListener(listener);
        findViewById(R.id.userinfo_bottom).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_userinfo_bottom_send).setOnClickListener(listener);
        findViewById(R.id.ll_userinfo_bottom_hi).setOnClickListener(listener);
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.base_title_left_view:     // 标题左侧退出按钮
                    back();
                    break;

                case R.id.base_title_right_img_container:// 标题右侧按钮
                    UIShow.showUserOtherSetAct(UserCheckInfoAct.this, userProfile);
                    break;

                case R.id.ll_userinfo_bottom_send:  // 底部发信
                    UIShow.showPrivateChatAct(UserCheckInfoAct.this, userProfile.getUid(), null);
                    break;

                case R.id.ll_userinfo_bottom_hi:    // 底部打招呼
                    break;

                case R.id.ll_userinfo_bottom_video: // 底部发视频
                    break;

                case R.id.ll_userinfo_bottom_voice: // 底部发语音
                    break;

                case R.id.iv_gift:                  // 底部礼物悬浮框
                    // 礼物弹框
                    break;
            }
        }
    };

    @Override
    public void onMessage(MsgType msgType, Msg msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.reqVideoChatConfig) {
            if (response.isOk()) {
                VideoConfig config = (VideoConfig) response.getBaseData();
                if (config.getVideoChat() == 1) {  // 展示发视频
                    videoBottom.setVisibility(View.VISIBLE);
                }

                if (config.getVoiceChat() == 1) {
                    voiceBottom.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
