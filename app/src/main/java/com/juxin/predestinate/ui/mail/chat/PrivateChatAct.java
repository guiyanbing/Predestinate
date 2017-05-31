package com.juxin.predestinate.ui.mail.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.Msg;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.view.roadlights.LMarqueeFactory;
import com.juxin.library.view.roadlights.LMarqueeView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.my.GiftMessageList;
import com.juxin.predestinate.module.local.chat.MessageRet;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.msgview.ChatViewLayout;
import com.juxin.predestinate.module.local.msgview.chatview.ChatInterface;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.discover.SelectCallTypeDialog;
import com.juxin.predestinate.ui.mail.item.MailMsgID;
import com.juxin.predestinate.ui.user.my.view.GiftMessageInforView;
import com.juxin.predestinate.ui.user.util.CenterConstant;

import java.util.List;

/**
 * 聊天页
 * Created by Kind on 2017/3/23.
 */
public class PrivateChatAct extends BaseActivity implements View.OnClickListener, PObserver {

    private long whisperID = 0;
    private String name;
    private boolean isFollow = false;
    private int kf_id;
    private ChatViewLayout privateChat = null;
    private ImageView cus_top_title_img, cus_top_img_phone;
    private TextView base_title_title, cus_top_title_txt;
    private View cus_top_title_view;
    private LMarqueeView lmvMeassages;
    private LMarqueeFactory<LinearLayout, GiftMessageList.GiftMessageInfo> marqueeView;

    private LinearLayout privatechat_head;
    private RelativeLayout llTitleRight;
    private ImageView chat_title_attention_icon;
    private TextView chat_title_attention_name, chat_title_yb_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanNotify(false);//设置该页面不弹出悬浮窗消息通知
        checkSingleState();

        whisperID = getIntent().getLongExtra("whisperID", 0);
        name = getIntent().getStringExtra("name");
        kf_id = getIntent().getIntExtra("kf_id", -1);
        Log.d("_test", "whisperID = " + whisperID);
        setContentView(R.layout.p1_privatechatact);

        initView();
        MsgMgr.getInstance().attach(this);
        checkReply();


        if (MailSpecialID.customerService.getSpecialID() == whisperID) {//缘分小秘书
            privateChat.getChatAdapter().showInputGONE();//输入框不显示
            privateChat.setInput_giftviewVisibility(View.GONE);
//            privateChat.getChatAdapter().showIsCanChat(true);
        } else {
            if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && !ModuleMgr.getCenterMgr()
                    .getMyInfo().isVip() && !ModuleMgr.getChatListMgr().getTodayChatShow()) {//男 非包月 //今天已经聊过了
                privateChat.getChatAdapter().showIsCanChat(false);
            }
        }
    }

    private void initLastGiftList() {
        if (MailSpecialID.customerService.getSpecialID() != whisperID)
            ModuleMgr.getCommonMgr().lastGiftList(new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (response.isOk()) {
                        GiftMessageList list = (GiftMessageList) response.getBaseData();
                        List<GiftMessageList.GiftMessageInfo> lastGiftMessages = list.getGiftMessageList();
                        if (lastGiftMessages != null && !lastGiftMessages.isEmpty()) {
                            lmvMeassages.setVisibility(View.VISIBLE);
                            marqueeView.setData(lastGiftMessages);
                            lmvMeassages.setAnimInAndOut(R.anim.top_in, R.anim.bottom_out);
                            lmvMeassages.setMarqueeFactory(marqueeView);
                            lmvMeassages.startFlipping();
                        }
                    }
                }
            });
    }

    /**
     * 桌面弹窗
     */
    private void checkReply() {
        String replyMsg = getIntent().getStringExtra("replyMsg");
        if (!TextUtils.isEmpty(replyMsg)) {
            UserDetail user = ModuleMgr.getCenterMgr().getMyInfo();
            if (!user.isVip() && !ModuleMgr.getChatListMgr().getTodayChatShow()) {//男
                PToast.showShort("免费发信次数已用完!");
                return;
            }
            ModuleMgr.getChatMgr().sendTextMsg(null, String.valueOf(whisperID), replyMsg);
        }
    }

    /**
     * 初始化标题
     */
    private void onTitleInit() {
        setBackView(R.id.base_title_back);
        View baseTitleView = LayoutInflater.from(this).inflate(R.layout.f1_privatechatact_titleview, null);
        View baseTitleViewRight = LayoutInflater.from(this).inflate(R.layout.f1_privatechatact_titleview_right, null);
        setTitleCenterContainer(baseTitleView);
        setTitleRightContainer(baseTitleViewRight);
        llTitleRight = (RelativeLayout) baseTitleView.findViewById(R.id.cus_top_title_ll);
        base_title_title = (TextView) baseTitleView.findViewById(R.id.cus_top_title);
        cus_top_title_txt = (TextView) baseTitleView.findViewById(R.id.cus_top_title_txt);
        cus_top_title_view = baseTitleView.findViewById(R.id.cus_top_title_view);
        cus_top_title_img = (ImageView) baseTitleView.findViewById(R.id.cus_top_title_img);
        cus_top_img_phone = (ImageView) baseTitleViewRight.findViewById(R.id.cus_top_title_img_phone);
        cus_top_img_phone.setOnClickListener(this);


        setNickName(name);
        if (MailSpecialID.customerService.getSpecialID() != whisperID) {//缘分小秘书
            setTitleRightImg(R.drawable.f1_user_ico, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIShow.showUserOtherSetAct(PrivateChatAct.this, whisperID, null, CenterConstant.USER_SET_FROM_CHAT);
                }
            });
        }
    }

    private void setNickName(String nickName) {
        String str = whisperID + "";
        MailMsgID mailMsgID = MailMsgID.getMailMsgID(whisperID);
        if (mailMsgID != null) {
            switch (mailMsgID) {
//                case matchmaker_msg://红娘
//                    str = ModuleMgr.getChatListMgr().getMatchMakerNickname();
//                    break;
            }
        } else {
            if (!TextUtils.isEmpty(nickName)) {
                str = nickName;
            }
        }

        if (base_title_title != null) {
            base_title_title.setText(str.length() > 10 ? (str.substring(0, 10)) : (str));
        }
    }

    private void initView() {
        onTitleInit();

        privateChat = (ChatViewLayout) findViewById(R.id.privatechat_view);
        privateChat.getChatAdapter().setWhisperId(whisperID);
        lmvMeassages = (LMarqueeView) findViewById(R.id.privatechat_lmv_messages);
        marqueeView = new GiftMessageInforView(this);

        // if (IS_REPLY) {//是否是首次回复的消息
        //     privateChat.getChatAdapter().setNewMsg(true);

//        if (kf_id != App.KF_ID) {
//            privateChat.getChatAdapter().setKf_id(kf_id);
//        }
//
        initLastGiftList();
        privateChat.getChatAdapter().setOnUserInfoListener(new ChatInterface.OnUserInfoListener() {
            @Override
            public void onComplete(UserInfoLightweight infoLightweight) {
                if (infoLightweight != null && whisperID == infoLightweight.getUid()) {
                    setNickName(infoLightweight.getShowName());
                    if (infoLightweight.getGender() == 1) {//是男的显示豪,显示头布局
                        cus_top_title_img.setBackgroundResource(R.drawable.f1_top02);
                    }
                    if (infoLightweight.isToper()) {//Top榜
                        cus_top_title_view.setVisibility(View.VISIBLE);
                        llTitleRight.setVisibility(View.VISIBLE);
                        cus_top_title_txt.setText("Top" + infoLightweight.getTop());
                    }

                    kf_id = infoLightweight.getKf_id();
                    name = infoLightweight.getShowName();
                    privateChat.getChatAdapter().setKf_id(infoLightweight.getKf_id());
                }
            }
        });

        if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && MailSpecialID.customerService.getSpecialID() != whisperID) {
            initHeadView();
            initFollow();
            isShowTopPhone();
        }

        //VIP男用户展示浮动提示
        if (ModuleMgr.getCenterMgr().getMyInfo().isVip() && ModuleMgr.getCenterMgr().getMyInfo().getGender() == 1
                && PSP.getInstance().getBoolean(FinalKey.SP_CHAT_SHOW_GIFT_GREETING_TIPS, false)) {
            privateChat.mGiftTipsContainerV.setVisibility(View.VISIBLE);
        }

        //状态栏 + 标题 +（关注TA、查看手机）+ 滚动条 高度
        if (ModuleMgr.getCenterMgr().getMyInfo().getGender() == 1 && MailSpecialID.customerService.getSpecialID() != whisperID)
            PSP.getInstance().put(Constant.PRIVATE_CHAT_TOP_H, getTitleView().getHeight() + lmvMeassages.getHeight() + privatechat_head.getHeight() + UIUtil.getStatusHeight(this));
        else
            PSP.getInstance().put(Constant.PRIVATE_CHAT_TOP_H, getTitleView().getHeight() + lmvMeassages.getHeight() + UIUtil.getStatusHeight(this));
    }

    private void initFollow() {
        ModuleMgr.getCenterMgr().reqOtherInfo(whisperID, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) return;
                UserDetail userDetail = (UserDetail) response.getBaseData();
                isFollow = userDetail.isFollow();
                kf_id = userDetail.getKf_id();
                if (isFollow) {
                    chat_title_attention_name.setText("已关注");
                    chat_title_attention_icon.setBackgroundResource(R.drawable.f1_chat01);
                } else {
                    chat_title_attention_name.setText("关注她");
                    chat_title_attention_icon.setBackgroundResource(R.drawable.f1_follow_star);
                }
//                cus_top_title_txt.setText(userDetail.get);
            }
        });
    }

    private void isShowTopPhone() {
        ModuleMgr.getChatMgr().getNetSingleProfile(whisperID, new ChatMsgInterface.InfoComplete() {
            @Override
            public void onReqComplete(boolean ret, UserInfoLightweight infoLightweight) {
                if (MailSpecialID.customerService.getSpecialID() != whisperID && infoLightweight.getGender() == 2 &&
                        (infoLightweight.isVideo_available() || infoLightweight.isAudio_available()))//女性用户显示可通话图标
                    cus_top_img_phone.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initHeadView() {
        privatechat_head = (LinearLayout) findViewById(R.id.privatechat_head);
        privatechat_head.setVisibility(View.VISIBLE);
        findViewById(R.id.chat_title_attention).setOnClickListener(this);
        findViewById(R.id.chat_title_phone).setOnClickListener(this);
        findViewById(R.id.chat_title_wx).setOnClickListener(this);
        findViewById(R.id.chat_title_yb).setOnClickListener(this);

        chat_title_attention_icon = (ImageView) findViewById(R.id.chat_title_attention_icon);
        chat_title_attention_name = (TextView) findViewById(R.id.chat_title_attention_name);
        chat_title_yb_name = (TextView) findViewById(R.id.chat_title_yb_name);

        chat_title_yb_name.setText("Y币:" + ModuleMgr.getCenterMgr().getMyInfo().getYcoin());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_title_attention: {//关注
                String content = ModuleMgr.getCenterMgr().getMyInfo().getNickname();
                if (!TextUtils.isEmpty(content) && !"null".equals(content))
                    content = "[" + content + "]" + getString(R.string.just_looking_for_you);
                else
                    content = getString(R.string.just_looking_for_you);
                ModuleMgr.getChatMgr().sendAttentionMsg(whisperID, content, kf_id, isFollow ? 2 : 1, new IMProxy.SendCallBack() {
                    @Override
                    public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                        MessageRet messageRet = new MessageRet();
                        messageRet.parseJson(contents);
                        if (messageRet.isOk() && messageRet.isS()) {
                            isFollow = !isFollow;
                            if (isFollow) {
                                chat_title_attention_name.setText("已关注");
                                chat_title_attention_icon.setBackgroundResource(R.drawable.f1_chat01);
                            } else {
                                chat_title_attention_name.setText("关注她");
                                chat_title_attention_icon.setBackgroundResource(R.drawable.f1_follow_star);
                            }
                        } else {
                            PToast.showShort(isFollow ? "取消关注失败！" : "关注失败！");
                        }
                    }

                    @Override
                    public void onSendFailed(NetData data) {
                        PToast.showShort(isFollow ? "取消关注失败！" : "关注失败！");
                    }
                });
                break;
            }
            case R.id.chat_title_phone://手机
                checkAndShowVip();
                break;
            case R.id.chat_title_wx://微信
                checkAndShowVip();
                break;
            case R.id.chat_title_yb://Y币
                UIShow.showGoodsYCoinDlgOld(this);
                break;
            case R.id.cus_top_title_img_phone://音视频
                new SelectCallTypeDialog(this, whisperID);
                break;
        }
    }

    private void checkAndShowVip() {
        if (ModuleMgr.getCenterMgr().getMyInfo().isVip()) {
            UIShow.showCheckOtherInfoAct(this, whisperID);
        } else {
            UIShow.showGoodsVipDlgOld(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkReply();
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_Chat_Can:
                if (MailSpecialID.customerService.getSpecialID() != whisperID &&
                        ModuleMgr.getCenterMgr().getMyInfo().isMan() && !ModuleMgr.getCenterMgr().getMyInfo().isVip()) {//男
                    if ((Boolean) ((Msg) value).getData()) {
                        privateChat.getChatAdapter().showIsCanChat(true);
                    } else {//不能回复信息
                        privateChat.getChatAdapter().showIsCanChat(false);
                    }
                }
                break;

            case MsgType.MT_SEND_GIFT_FLAG:
                if (!Constant.GIFT_CHAT.equals((String) value)) return;
                PSP.getInstance().put(FinalKey.SP_CHAT_SHOW_GIFT_GREETING_TIPS, false);
                privateChat.mGiftTipsContainerV.setVisibility(View.GONE);
                break;

            case MsgType.MT_MyInfo_Change://更新个人资料
                if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && ModuleMgr.getCenterMgr().getMyInfo().isVip()) {//男
                    privateChat.getChatAdapter().showIsCanChat(true);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        privateChat.getChatAdapter().detach();
        lastActivity = null;
    }

    private static BaseActivity lastActivity = null;

    private void checkSingleState() {
        if (lastActivity != null) {
            try {
                lastActivity.finish();
            } catch (Exception e) {
                PLogger.printThrowable(e);
            }
        }
        lastActivity = this;
    }
}