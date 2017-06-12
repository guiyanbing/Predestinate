package com.juxin.predestinate.ui.user.check;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.detail.UserVideo;
import com.juxin.predestinate.module.local.chat.MessageRet;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;
import com.juxin.predestinate.module.util.my.AttentionUtil;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * 查看用户资料详情
 * Created by Su on 2016/5/30.
 */
public class UserCheckInfoAct extends BaseActivity implements PObserver, RequestComplete {
    public static final int REQUEST_CODE_UNLOCK_VIDEO = 100;//请求跳转到私密视频页面
    private int channel;  // 查看用户资料区分Tag，默认查看自己个人资料
    private UserDetail userDetail;   // 用户资料

    private ScrollView scrollLayout;
    private TextView tv_sayhi, tv_look_look;
    private LinearLayout container, videoBottom, voiceBottom, sayHibottom;

    /* *********  panel **********/
    private UserCheckInfoHeadPanel headPanel;
    private UserCheckInfoFootPanel footPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_info_layout);

        initData();
        initView();
        MsgMgr.getInstance().attach(this);
    }

    private void initData() {
        channel = getIntent().getIntExtra(CenterConstant.USER_CHECK_INFO_KEY, CenterConstant.USER_CHECK_INFO_OWN);
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            return;
        }
        userDetail = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
    }

    private void initView() {
        initTitle();
        scrollLayout = (ScrollView) findViewById(R.id.layout_scroll);
        container = (LinearLayout) findViewById(R.id.container);
        headPanel = new UserCheckInfoHeadPanel(this, channel, userDetail);
        container.addView(headPanel.getContentView());

        footPanel = new UserCheckInfoFootPanel(this, channel, userDetail);
        footPanel.setSlideIgnoreView(this);
        container.addView(footPanel.getContentView());
        initBottom();
    }

    private void initTitle() {
        setBackView(userDetail.getShowName());
        findViewById(R.id.cut_line).setVisibility(View.GONE);
        if (userDetail.isMan()) {
            setTitleBackground(R.color.picker_blue_color);
        }
        if (channel != CenterConstant.USER_CHECK_INFO_OWN)
            setTitleRightImg(R.drawable.f1_more_vertical_dot, listener);
    }

    // 底部功能按钮展示逻辑
    private void initBottom() {
        ModuleMgr.getCenterMgr().reqVideoChatConfig(userDetail.getUid(), this); // 请求音视频开关配置
        if (channel == CenterConstant.USER_CHECK_INFO_OWN) return;

        videoBottom = (LinearLayout) findViewById(R.id.ll_userinfo_bottom_video);
        voiceBottom = (LinearLayout) findViewById(R.id.ll_userinfo_bottom_voice);
        sayHibottom = (LinearLayout) findViewById(R.id.ll_userinfo_bottom_hi);
        tv_sayhi = (TextView) findViewById(R.id.tv_sayhello_text);
        tv_look_look = (TextView) findViewById(R.id.tv_look_look);

        videoBottom.setOnClickListener(listener);
        voiceBottom.setOnClickListener(listener);
        findViewById(R.id.iv_gift).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_gift).setOnClickListener(listener);
        findViewById(R.id.userinfo_bottom).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_userinfo_bottom_send).setOnClickListener(listener);

        if (userDetail == null) return;

        if (userDetail.isMan()) {
            tv_look_look.setText(R.string.user_info_look_at_he);
        } else {
            tv_look_look.setText(R.string.user_info_look_at_her);
        }

        if (userDetail.isSayHello()) {   // 已打招呼
            initSayHi();
        } else {
            sayHibottom.setOnClickListener(listener);
        }
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.base_title_right_img_container:// 标题右侧按钮
                    UIShow.showUserOtherSetAct(UserCheckInfoAct.this, userDetail.getUid(), userDetail, CenterConstant.USER_SET_FROM_CHECK);
                    break;

                case R.id.ll_userinfo_bottom_send:  // 底部发信
                    Statistics.userBehavior(SendPoint.userinfo_btnsendmessage, userDetail.getUid());
                    UserDetail info = ModuleMgr.getCenterMgr().getMyInfo();
                    if (info.isMan() && !info.isVip()) {
                        showVipTips();
                        return;
                    }
                    UIShow.showPrivateChatAct(UserCheckInfoAct.this, userDetail.getUid(), null);
                    break;

                case R.id.ll_userinfo_bottom_hi:    // 底部打招呼
                    Statistics.userBehavior(SendPoint.userinfo_btnsayhello, userDetail.getUid());
                    handleSayHi();
                    break;

                case R.id.ll_userinfo_bottom_video: // 底部发视频
                    Statistics.userBehavior(SendPoint.userinfo_btnvideo, userDetail.getUid());
                    VideoAudioChatHelper.getInstance().inviteVAChat(UserCheckInfoAct.this, userDetail.getUid(), VideoAudioChatHelper.TYPE_VIDEO_CHAT, true, Constant.APPEAR_TYPE_NO);
                    break;

                case R.id.ll_userinfo_bottom_voice: // 底部发语音
                    Statistics.userBehavior(SendPoint.userinfo_btnvoice, userDetail.getUid());
                    VideoAudioChatHelper.getInstance().inviteVAChat(UserCheckInfoAct.this, userDetail.getUid(), VideoAudioChatHelper.TYPE_AUDIO_CHAT);
                    break;

                case R.id.iv_gift:                  // 底部礼物悬浮框
                    Statistics.userBehavior(SendPoint.userinfo_btngirl);
                    UIShow.showBottomGiftDlg(UserCheckInfoAct.this, userDetail.getUid(), Constant.OPEN_FROM_INFO);
                    break;
            }
        }
    };

    /**
     * 开通Vip提示
     */
    private void showVipTips() {
        PickerDialogUtil.showSimpleTipDialogExt(this, new SimpleTipDialog.ConfirmListener() {
            @Override
            public void onCancel() {
                handleSayHi();
            }

            @Override
            public void onSubmit() {
                UIShow.showBuyCoinActivity(UserCheckInfoAct.this);
            }
        }, getString(R.string.hot_card_price_vip), "", getString(R.string.hot_card_price_cancle), getString(R.string.hot_card_price_sure), true, R.color.text_zhuyao_black);
    }

    private void initSayHi() {
        tv_sayhi.setText(getString(R.string.user_info_has_hi));
        if (Build.VERSION.SDK_INT >= HONEYCOMB) {
            sayHibottom.setAlpha(0.4f);
        }
    }

    /**
     * 底部已打招呼处理
     */
    private void handleSayHi() {
        if (userDetail.isSayHello()) {
            PToast.showShort(getString(R.string.user_info_hi_repeat));
            return;
        }

        ModuleMgr.getChatMgr().sendSayHelloMsg(String.valueOf(userDetail.getUid()),
                getString(R.string.say_hello_txt),
                userDetail.getKf_id(),
                ModuleMgr.getCenterMgr().isRobot(userDetail.getKf_id()) ?
                        Constant.SAY_HELLO_TYPE_ONLY : Constant.SAY_HELLO_TYPE_SIMPLE, new IMProxy.SendCallBack() {
                    @Override
                    public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                        MessageRet messageRet = new MessageRet();
                        messageRet.parseJson(contents);

                        if (messageRet.getS() == 0) { // 成功
                            sayHibottom.setClickable(false);
                            PToast.showShort(getString(R.string.user_info_hi_suc));
                            MsgMgr.getInstance().sendMsg(MsgType.MT_Say_HI_Notice, userDetail.getUid());
                            userDetail.setSayHello(true);
                            initSayHi();
                        } else {
                            PToast.showShort(getString(R.string.user_info_hi_fail));
                        }
                    }

                    @Override
                    public void onSendFailed(NetData data) {
                        PToast.showShort(getString(R.string.user_info_hi_fail));
                    }
                });
    }

    /**
     * 滚动到底部
     */
    private void scrollToBottom() {
        TimerUtil.beginTime(new TimerUtil.CallBack() {
            @Override
            public void call() {
                scrollLayout.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (channel == CenterConstant.USER_CHECK_CONNECT_OTHER)
            scrollToBottom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 更新备注名
        if (requestCode == CenterConstant.USER_SET_REQUEST_CODE) {
            switch (resultCode) {
                case CenterConstant.USER_SET_RESULT_CODE:
                    String remark = data.getStringExtra("remark");
                    userDetail.setRemark(remark);
                    setTitle(userDetail.getShowName());
                    footPanel.refreshView(userDetail);
                    //更新缓存
                    AttentionUtil.updateUserDetails(userDetail);
                    break;
            }
        } else if (requestCode == REQUEST_CODE_UNLOCK_VIDEO && resultCode == RESULT_OK && data != null) {
            ArrayList<Long> unlockList = (ArrayList<Long>) data.getSerializableExtra(CenterConstant.USER_CHECK_UNLOCK_VIDEO_LIST_KEY);
            List<UserVideo> videoList = userDetail.getUserVideos();
            for (Long id : unlockList) {
                for (UserVideo video : videoList) {
                    if (video.getId() == id) {
                        video.setCanView();
                        break;
                    }
                }
            }
            //刷新私密视频
            footPanel.freshSecretVideo();
        }
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
                footPanel.refreshChatPrice(config);
                if (channel == CenterConstant.USER_CHECK_INFO_OWN) return;
                if (config.getVideoChat() == 1) {  // 展示发视频
                    videoBottom.setVisibility(View.VISIBLE);
                }

                if (config.getVoiceChat() == 1) {
                    voiceBottom.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_MyInfo_Change:
                userDetail = ModuleMgr.getCenterMgr().getMyInfo();
                footPanel.refreshView(userDetail);
                break;

            default:
                break;
        }
    }
}
