package com.juxin.predestinate.ui.mail.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.library.view.roadlights.LMarqueeFactory;
import com.juxin.library.view.roadlights.LMarqueeView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.my.ChatInfo;
import com.juxin.predestinate.bean.my.GiftMessageList;
import com.juxin.predestinate.bean.my.UserNetInfo;
import com.juxin.predestinate.module.local.chat.MessageRet;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.msgview.ChatViewLayout;
import com.juxin.predestinate.module.local.msgview.chatview.ChatInterface;
import com.juxin.predestinate.module.local.pay.CheckYCoinBean;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.discover.SelectCallTypeDialog;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import com.juxin.predestinate.ui.user.my.view.GiftMessageInfoView;
import com.juxin.predestinate.ui.user.util.CenterConstant;

import java.util.List;

/**
 * 聊天页
 * Created by Kind on 2017/3/23.
 */
public class PrivateChatAct extends BaseActivity implements View.OnClickListener, PObserver {

    private long whisperID = 0;
    private String channel_uid = "";
    private String name;
    private int kf_id;
    private UserInfoLightweight otherInfo;
    private ChatViewLayout privateChat = null;
    private ImageView cus_top_title_img, cus_top_img_phone;
    private TextView base_title_title,net_top_title,cus_top_title_txt;
    private LMarqueeView lmvMeassages;
    private LMarqueeFactory<LinearLayout, GiftMessageList.GiftMessageInfo> marqueeView;

    private RelativeLayout llTitleRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanNotify(false);//设置该页面不弹出悬浮窗消息通知
        checkSingleState();
        setContentView(R.layout.p1_privatechatact);

        whisperID = getIntent().getLongExtra("whisperID", 0);
        name = getIntent().getStringExtra("name");
        kf_id = getIntent().getIntExtra("kf_id", -1);
        PLogger.d("------>whisperID: " + whisperID + " , name: " + name + " , kf_id: " + kf_id);

        initView();
        MsgMgr.getInstance().attach(this);
        checkReply();

        checkIsCanSendMsg();
        ModuleMgr.getPhizMgr().reqCustomFace();
    }

    public void checkIsCanSendMsg() {
        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();

        //缘分小秘书
        if (MailSpecialID.customerService.getSpecialID() == whisperID) {
            privateChat.setInputGiftviewVisibility(View.GONE);
            if (ModuleMgr.getCommonMgr().getCommonConfig().canSecretaryShow()
                    || (userDetail.isVip() || userDetail.getYcoin() > 0 || userDetail.getDiamondsSum() > 0)) {
                privateChat.getChatAdapter().showIsCanChat(true);//输入框显示
            } else {
                privateChat.getChatAdapter().showInputGONE();//输入框不显示
            }
            return;
        }

        if (!userDetail.isMan() //女性用户
                || (userDetail.isUnlock_vip() && userDetail.isUnlock_ycoin())//ip 并且Y币>0
                || ((userDetail.isUnlock_vip() || userDetail.isVip()) && userDetail.getYcoin() > 0) //ip 并且Y币>0
                || (userDetail.isUnlock_ycoin() && userDetail.isVip()) //并且是vip
                || (userDetail.getYcoin() > 79 && "0".equals(userDetail.getyCoinUserid())) //Y币 高于79 并且未绑定用户
                || (userDetail.getYcoin() > 79 && String.valueOf(whisperID).equals(userDetail.getyCoinUserid()))) { //Y币高于79，并且是绑定用户
            privateChat.getChatAdapter().showIsCanChat(true);
        } else {
            if (ModuleMgr.getChatListMgr().getTodayChatShow() && userDetail.getYcoin() == 0) { //当天末发送还要Y币==0 //最初状态
                privateChat.getChatAdapter().showIsCanChat(true);
            } else {
                privateChat.getChatAdapter().showIsCanChat(false);
            }
        }
    }

    private void executeYCoinTask() {
        ModuleMgr.getCommonMgr().checkycoin(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                CheckYCoinBean yCoinBean = new CheckYCoinBean();
                yCoinBean.parseJson(response.getResponseString());
                if (yCoinBean.isOk()) {
                    ModuleMgr.getCenterMgr().getMyInfo().setYcoin(yCoinBean.getY());
                    ModuleMgr.getCenterMgr().getMyInfo().setyCoinUserid(yCoinBean.getTouid());
                    if(privateChat != null && MailSpecialID.customerService.getSpecialID() != whisperID) {
                        privateChat.yTipsLogic(false);
                    }
                    checkIsCanSendMsg();
                }
            }
        });
    }

    /**
     * 聊天窗口信息--整合接口
     */
    private void reqChatInfo() {
        ModuleMgr.getCommonMgr().reqChatInfo(whisperID, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) return;

                ChatInfo chatInfo = new ChatInfo();
                chatInfo.parseJson(response.getResponseString());
                //Y币
                ModuleMgr.getCenterMgr().getMyInfo().setYcoin(chatInfo.getYcoin());

                //在线状态
                if("在线".equalsIgnoreCase(chatInfo.getOtherInfo().getLast_online())) {
                    net_top_title.setText(getString(R.string.net_online_pre) + chatInfo.getOtherInfo().netTp2Str(chatInfo.getOtherInfo().getNet_tp()));
                    if(MailSpecialID.customerService.getSpecialID() != whisperID) {
                        privateChat.getChatAdapter().lookAtHer(true);
                    }else {
                        privateChat.getChatAdapter().lookAtHer(false);
                    }
                }else {
                    net_top_title.setText(getString(R.string.net_offline));
                    privateChat.getChatAdapter().lookAtHer(false);
                }
                //音视频是否可见
                if(MailSpecialID.customerService.getSpecialID() != whisperID && ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
                    ChatInfo.OtherInfo.VideoConfig videoConfig = chatInfo.getOtherInfo().getVideoConfig();
                    if (videoConfig.isVideoChat()) {
                        privateChat.setInputLookAtHerVisibility(View.VISIBLE);
                    }else {
                        privateChat.setInputLookAtHerVisibility(View.GONE);
                    }
                }
            }
        });
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
        net_top_title = (TextView) baseTitleView.findViewById(R.id.net_top_title);
        cus_top_title_txt = (TextView) baseTitleView.findViewById(R.id.cus_top_title_txt);
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

        if (whisperID == MailSpecialID.customerService.getSpecialID()) {
            str = MailSpecialID.customerService.getSpecialIDName();
        }

        if (!TextUtils.isEmpty(nickName)) {
            str = nickName;
        }

        if (base_title_title != null) {
            base_title_title.setText(str.length() > 10 ? (str.substring(0, 10)) : (str));
        }
    }

    private void initView() {
        onTitleInit();

        privateChat = (ChatViewLayout) findViewById(R.id.privatechat_view);
        privateChat.getChatAdapter().setWhisperId(whisperID);
        otherInfo = privateChat.getChatAdapter().getUserInfo(whisperID);
        lmvMeassages = (LMarqueeView) findViewById(R.id.privatechat_lmv_messages);
        marqueeView = new GiftMessageInfoView(this);

        reqChatInfo();
        initLastGiftList();
        executeYCoinTask();
        privateChat.getChatAdapter().setOnUserInfoListener(new ChatInterface.OnUserInfoListener() {
            @Override
            public void onComplete(UserInfoLightweight infoLightweight) {
                if (infoLightweight != null && whisperID == infoLightweight.getUid()) {
                    setNickName(infoLightweight.getShowName());
                    if (infoLightweight.getGender() == 1) {//是男的显示豪,显示头布局
                        cus_top_title_img.setImageResource(R.drawable.f1_topc02);
                    }
                    if (infoLightweight.isToper()) {//Top榜
                        llTitleRight.setVisibility(View.VISIBLE);
                        cus_top_title_txt.setText("Top" + infoLightweight.getTop());
                    }

                    kf_id = infoLightweight.getKf_id();
                    if (kf_id != 0 || MailSpecialID.customerService.getSpecialID() == whisperID) {
                        privateChat.getChatAdapter().onDataUpdate();
                    }
                }
            }
        });

        if (MailSpecialID.customerService.getSpecialID() != whisperID) {
            privateChat.yTipsLogic(false);
            initFollow();
            isShowTopPhone();
        }

        //状态栏 + 标题 +（关注TA、查看手机）// （去掉滚动条高度） 高度
        PSP.getInstance().put(Constant.PRIVATE_CHAT_TOP_H, UIUtil.getViewHeight(getTitleView()) + UIUtil.getStatusHeight(this));
    }

    private void initFollow() {
        ModuleMgr.getCenterMgr().reqOtherInfo(whisperID, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) return;
                UserDetail userDetail = (UserDetail) response.getBaseData();
                kf_id = userDetail.getKf_id();
                channel_uid = String.valueOf(userDetail.getChannel_uid());
                name = userDetail.getNickname();
                setNickName(userDetail.getShowName());
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cus_top_title_img_phone://音视频
                new SelectCallTypeDialog(this, whisperID, channel_uid);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkReply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 更新备注名
        if (requestCode == CenterConstant.USER_SET_REQUEST_CODE) {
            switch (resultCode) {
                case CenterConstant.USER_SET_RESULT_CODE:
                    String remark = data.getStringExtra("remark");
                    setNickName(TextUtils.isEmpty(remark) ? name : remark);
                    break;
            }
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_Chat_Clear_History:
                long tmpID = (long) value;
                if(privateChat != null && whisperID == tmpID){
                    privateChat.getChatAdapter().clearHistory();
                }
                break;
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

            case MsgType.MT_MyInfo_Change://更新个人资料
                if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && ModuleMgr.getCenterMgr().getMyInfo().isVip()) {//男
                    privateChat.getChatAdapter().showIsCanChat(true);
                }
                break;
            case MsgType.MT_Update_Ycoin:
                if ((Boolean) value) {//去请求网络
                    executeYCoinTask();
                } else {//不请求网络
                    checkIsCanSendMsg();
                    if(privateChat != null && MailSpecialID.customerService.getSpecialID() != whisperID) {
                        privateChat.yTipsLogic(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
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