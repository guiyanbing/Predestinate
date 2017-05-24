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
import android.widget.TextView;

import com.juxin.library.log.PLogger;
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
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.msgview.ChatViewLayout;
import com.juxin.predestinate.module.local.msgview.chatview.ChatInterface;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.UIShow;
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
    private ImageView cus_top_title_img,cus_top_img_phone;
    private TextView base_title_title, cus_top_title_txt;
    private LMarqueeView lmvMeassages;
    private  LMarqueeFactory<LinearLayout, GiftMessageList.GiftMessageInfo> marqueeView;

    private LinearLayout privatechat_head;
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

        //addMessageListener(MsgType.MT_MyInfo_Change, this);//个人资料已更新
        //addMessageListener(MsgType.MT_Contacts_Change, this);//好友关系发生变化
        //addMessageListener(MsgType.MT_APP_Suspension_Notice, this);
        // addMessageListener(MsgType.MT_Pay_Success, this);//充值成功
        //ChatSpecialMgr.getChatSpecialMgr().attachSystemMsgListener(this);

        checkReply();

        if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && !ModuleMgr.getCenterMgr()
                .getMyInfo().isVip() && !ModuleMgr.getChatListMgr().getTodayChatShow()) {//男 非包月 //今天已经聊过了
            privateChat.getChatAdapter().showIsCanChat(false);
        }

//        if (MailSpecialID.customerService.getSpecialID() == whisperID) {//小友客服
//            privateChat.getChatAdapter().showInputGONE();//输入框不显示
//            privateChat.getChatAdapter().showIsCanChat(true);//显示输入框
//        } else {
//            if (ModuleMgr.getCenterMgr().isMan() && !ModuleMgr.getCenterMgr().isVIP()) {//男 非包月
//                if (onDetectHeart()) {
//                    if (!ModuleMgr.getChatListMgr().getTodayChatShow()) {//今天已经聊过了
//                        privateChat.getChatAdapter().showIsCanChat(false);
//                    }
//                }
//            }
//        }
    }

    private void initLastGiftList() {
        ModuleMgr.getCommonMgr().lastGiftList(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()){
                    GiftMessageList list = (GiftMessageList) response.getBaseData();
                    List<GiftMessageList.GiftMessageInfo> lastGiftMessages = list.getGiftMessageList();
                    if (lastGiftMessages != null && !lastGiftMessages.isEmpty()){
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
        base_title_title = (TextView) baseTitleView.findViewById(R.id.cus_top_title);
        cus_top_title_txt = (TextView) baseTitleView.findViewById(R.id.cus_top_title_txt);
        cus_top_title_img = (ImageView) baseTitleView.findViewById(R.id.cus_top_title_img);
        cus_top_img_phone = (ImageView) baseTitleViewRight.findViewById(R.id.cus_top_title_img_phone);
        cus_top_img_phone.setOnClickListener(this);





        setNickName(name);
        if (MailSpecialID.customerService.getSpecialID() != whisperID) {//小友客服
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
                    setNickName(infoLightweight.getNickname());
                    if (infoLightweight.getGender() == 1)//是男的显示豪
                        cus_top_title_img.setBackgroundResource(R.drawable.f1_top02);
                    if (MailSpecialID.customerService.getSpecialID() != whisperID && infoLightweight.getGender() == 2 &&
                            (infoLightweight.isVideo_available() || infoLightweight.isVideo_available()))
                        cus_top_img_phone.setVisibility(View.VISIBLE);

                    kf_id = infoLightweight.getKf_id();
                    name = infoLightweight.getNickname();
                    privateChat.getChatAdapter().setKf_id(infoLightweight.getKf_id());
                }
            }
        });

        privateChat.getChatAdapter().setWhisperId(whisperID);
        initHeadView();
        initFollow();
    }

    private void initFollow() {
        ModuleMgr.getCenterMgr().reqOtherInfo(whisperID, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) return;
                UserDetail userDetail = (UserDetail) response.getBaseData();
                isFollow = userDetail.isFollow();
                kf_id = userDetail.getKf_id();
            //    cus_top_title_txt.setText(userDetail.get);
            }
        });
    }

    private void initHeadView() {
        privatechat_head = (LinearLayout) findViewById(R.id.privatechat_head);
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
                ModuleMgr.getChatMgr().sendAttentionMsg(whisperID, "", kf_id, isFollow ? 2 : 1, new IMProxy.SendCallBack() {
                    @Override
                    public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                        MessageRet messageRet = new MessageRet();
                        messageRet.parseJson(contents);
                        if (messageRet.isOk() && messageRet.isS()) {
                            if(isFollow){
                                chat_title_attention_name.setText("取消关注");
                                chat_title_attention_icon.setBackgroundResource(R.drawable.f1_chat01);
                            }else {
                                chat_title_attention_name.setText("关注TA");
                                chat_title_attention_icon.setBackgroundResource(R.drawable.f1_chat01);
                            }
                        }else {
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
                UIShow.showCheckOtherInfoAct(this, whisperID);
                break;
            case R.id.chat_title_wx://微信
                UIShow.showCheckOtherInfoAct(this, whisperID);
                break;
            case R.id.chat_title_yb://Y币
                UIShow.showGoodsYCoinDlgOld(this);
                break;
            case R.id.cus_top_title_img_phone://音视频
                new SelectCallTypeDialog(this, whisperID);
                break;
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
                if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && !ModuleMgr.getCenterMgr().getMyInfo().isVip()) {//男
                    if ((Boolean) ((Msg) value).getData()) {
                        privateChat.getChatAdapter().showIsCanChat(true);
                    } else {//不能回复信息
                        privateChat.getChatAdapter().showIsCanChat(false);
                    }
                }
                break;
        }
    }


//    @Override
//    public void onMessage(MsgType msgType, Msg msg) {
//        switch (msgType) {
//            case MT_Chat_Can:
//                if (ModuleMgr.getCenterMgr().isMan() && !ModuleMgr.getCenterMgr().isVIP()) {//男
//                    if ((Boolean) msg.getData()) {
//                        privateChat.getChatAdapter().showIsCanChat(true);
//                    } else {//不能回复信息
//                        privateChat.getChatAdapter().showIsCanChat(false);
//                    }
//                }
//                break;
//            case MT_MyInfo_Change://更新个人资料
//                if (ModuleMgr.getCenterMgr().isMan() && ModuleMgr.getCenterMgr().isVIP()) {//男
//                    privateChat.getChatAdapter().showIsCanChat(true);
//                }
//                break;
//            case MT_Pay_Success://充值成功
////                    if (ModuleMgr.getCenterMgr().isMan() && !ModuleMgr.getCenterMgr().isMonthMail()) {//男
////                        privateChat.getChatAdapter().showIsCanChat(ModuleMgr.getCenterMgr().getMyInfo().isMonthMail());
////                    }
//                break;
//            case MT_Contacts_Change://好友关系发生变化
//                if (ModuleMgr.getMsgCommonMgr().getFriendsData().isContains(whisperID)) {
//                    setRightImg();
//                } else {
//                    setRightText();
//                }
//                break;
//            case MT_APP_Suspension_Notice:
//                Map<String, Object> parms = (Map<String, Object>) msg.getData();
//                if (parms.containsKey(TipsBarMgr.TipsMgrIsShow)) {
//                    String isShow = (String) parms.get(TipsBarMgr.TipsMgrIsShow);
//                    Log.d("_test", "PrivateChatAct onMessage isshow = " + isShow);
//                    if (isShow.equals(TipsBarMgr.TipsMgrIsShow_true)) {
//                        showFloatTip(viewGroup);
//                    } else if (isShow.equals(TipsBarMgr.TipsMgrIsShow_false) || isShow.equals(TipsBarMgr.TipsMgrIsShow_none)) {
//                        TipToolUtils.dismiss();
//                    }
//                }
//                break;
//        }
//    }
//
//
//    private void showFloatTip(final ViewGroup view) {
//        Log.d("_test", "showFloatTip --- view cont = " + view.getChildCount());
//        final View anchorView = findViewById(R.id.back_but);
//        final ViewGroup root = (ViewGroup) getWindow().getDecorView();
//        MsgMgr.getInstance().sendMsgToUI(new Runnable() {
//            @Override
//            public void run() {
//                TipToolUtils.dismiss();
//                TipToolUtils.showTip(PrivateChatAct.this, anchorView, view, root, null);
//            }
//        });
//    }
//
//    @Override
//    public void onSystemMsg(BaseMessage message) {
//        try {
//            if (whisperID != message.getLWhisperID()) {
//                return;
//            }
//            String str = message.getJsonStr();
//            if (TextUtils.isEmpty(str)) return;
//            JSONObject jsonObject = new JSONObject(str);
//            switch (jsonObject.optInt("xt")) {
//                case 5://5为正在输入
//                    setBackView(R.id.back_view, "对方正在输入...");
//                    break;
//                case 6://6为取消正在输入
//                    setNickName(name);
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private JSONObject getParmsJson() {
//        JSONObject object = null;
//        TipsParms parms = new TipsParms();
//        parms.setTipsTex("聊天请使用文明用语,发布不良信息被举报将被封号");
//        parms.setUid(whisperID);
//        String jsonStr = new Gson().toJson(parms);
//        if (!TextUtils.isEmpty(jsonStr))
//            try {
//                object = new JSONObject(jsonStr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        return object;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        privateChat.getChatAdapter().detach();
        //  ChatSpecialMgr.getChatSpecialMgr().detachSystemMsgListener(this);
        lastActivity = null;
        //   ModuleMgr.getTipsBarMgr().detach();
        //  TipToolUtils.dismiss();
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