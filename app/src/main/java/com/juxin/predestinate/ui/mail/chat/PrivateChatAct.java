package com.juxin.predestinate.ui.mail.chat;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.message.Msg;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.message.MsgType;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatViewLayout;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.item.MailMsgID;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * Created by Kind on 2017/3/23.
 */
public class PrivateChatAct extends BaseActivity implements View.OnClickListener {

    private long whisperID = 0;
    private String name;
    private int kf_id;
    private ChatViewLayout privateChat = null;
    private CustomFrameLayout viewGroup;

    private LinearLayout privatechat_head;
    private ImageView chat_title_attention_icon;
    private TextView chat_title_attention_name;

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
        //addMessageListener(MsgType.MT_Chat_Can, this);
        //addMessageListener(MsgType.MT_MyInfo_Change, this);//个人资料已更新
        //addMessageListener(MsgType.MT_Contacts_Change, this);//好友关系发生变化
        //addMessageListener(MsgType.MT_APP_Suspension_Notice, this);
        // addMessageListener(MsgType.MT_Pay_Success, this);//充值成功
        //ChatSpecialMgr.getChatSpecialMgr().attachSystemMsgListener(this);

        checkReply();

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

        //进入该页面之后请求视频权限
//        PermissionUtils.requestCamera(this, new Runnable() {
//            @Override
//            public void run() {
//                Camera camera = CameraHelper.getCameraInstance(CameraHelper.getDefaultCameraID());
//                if (camera != null) camera.release();
//
//                //获取到视频权限之后再请求录音权限
//                PermissionUtils.requestAudio(PrivateChatAct.this, new Runnable() {
//                    @Override
//                    public void run() {
//                        ChatMediaRecord.getInstance().requestPermission();
//                    }
//                });
//            }
//        });
    }

    /**
     * 黑名单
     */
    private void onpullToBlack() {
//        ModuleMgr.getCenterMgr().pullToBlack(PrivateChatAct.this, whisperID, getTitleView(), new CenterMgr.PullBlackComplete() {
//
//            @Override
//            public void onReqComplete(boolean isOk, String errorStr, PullTitleResult pullTitleResult) {
//                if (!isOk) {
//                    if (TextUtils.isEmpty(errorStr)) {
//                        MMToast.showShort("请求出错!");
//                    } else {
//                        MMToast.showShort(errorStr);
//                    }
//                } else {
//                    switch (pullTitleResult.getBigType()) {
//                        case -1://拉黑
//                            MMToast.showShort("拉黑成功!");
//                            break;
//                        case -2://拉黑并举报
//                            MMToast.showShort("拉黑并举报成功!");
//                            break;
//                        case -3://解除拉黑成功
//                            MMToast.showShort("解除成功!");
//                            break;
//                    }
//                }
//            }
//        });
    }

    private boolean onDetectHeart() {
//        List<MutualHeartUnList.MutualHeartUn> mutualHeartUns = ModuleMgr.getCfgMgr().getMutualHeartUn();
//        for (MutualHeartUnList.MutualHeartUn tmp : mutualHeartUns) {
//            if (whisperID == tmp.getUid()) {
//                /**
//                 * 如果发送时间是等于0或是小于0，说明没有聊过，可以聊一句，否者不能聊
//                 */
//                if (tmp.getSendtime() <= 0) {//如果是用完今天聊的了。相互心动了也不能聊了
//                    privateChat.getChatAdapter().showIsCanChat(true);
//                } else {
//                    privateChat.getChatAdapter().showIsCanChat(false);
//                }
//
//                return false;
//            }
//        }
        return true;
    }

    /**
     * 桌面弹窗
     */
    private void checkReply() {
//        String replyMsg = getIntent().getStringExtra("replyMsg");
//        if (!TextUtils.isEmpty(replyMsg)) {
//            UserDetail user = ModuleMgr.getCenterMgr().getMyInfo();
//            if (!user.isVip() && !ModuleMgr.getChatListMgr().getTodayChatShow()) {//男
//                MMToast.showShort("免费发信次数已用完!");
//                return;
//            }
//            if (!ModuleMgr.getCommonMgr().headRemindOnChat()) {
//                return;
//            }
//            ModuleMgr.getChatMgr().sendTextMsg(ChatListMgr.Folder.whisper, null, String.valueOf(whisperID), replyMsg);
//        }
    }

    /**
     * 初始化标题
     */
    private void onTitleInit() {
        setBackView(R.id.base_title_back);
        View baseTitleView = LayoutInflater.from(this).inflate(R.layout.f1_privatechatact_titleview, null);
        setTitleCenterContainer(baseTitleView);
        TextView base_title_title = (TextView) baseTitleView.findViewById(R.id.cus_top_title);

        String str = whisperID + "";
        MailMsgID mailMsgID = MailMsgID.getMailMsgID(whisperID);
        if (mailMsgID != null) {
            switch (mailMsgID) {
//                case matchmaker_msg://红娘
//                    str = ModuleMgr.getChatListMgr().getMatchMakerNickname();
//                    break;
            }
        } else {
//            if (!TextUtils.isEmpty(nickName)) {
//                if (nickName.length() > 10) {
//                    str = nickName.substring(0, 8);
//                } else {
//                    str = nickName;
//                }
//            }
            if (!TextUtils.isEmpty(name)) {
                str = name;
            }
        }
        base_title_title.setText("111111");
       // base_title_title.setText(str.length() > 10 ? ("与" + str + "...的私信") : ("与" + str + "的私信");




        setTitleRightImg(R.drawable.f1_user_ico, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIShow.showUserOtherSetAct(PrivateChatAct.this, null);
            }
        });

//        if (MailMsgID.getMailMsgID(whisperID) == null && MailSpecialID.customerService.getSpecialID() != whisperID) {
//            if (ModuleMgr.getMsgCommonMgr().getFriendsData().isContains(whisperID)) {
//                setRightImg();
//            } else {
//                setRightText();
//            }
//        }
    }

    private void initView() {
        onTitleInit();

       // viewGroup = (CustomFrameLayout) LayoutInflater.from(this).inflate(R.layout.y2_tips_view_group, null);
        privateChat = (ChatViewLayout) findViewById(R.id.privatechat_view);

//        if(message != null && ChatListMgr.Folder.sys_notice.equals(message.getFolder())){
//            privateChat.getChatAdapter().setFolder(ChatListMgr.Folder.sys_notice);
//        }

        // if (IS_REPLY) {//是否是首次回复的消息
        //     privateChat.getChatAdapter().setNewMsg(true);

//        if (kf_id != App.KF_ID) {
//            privateChat.getChatAdapter().setKf_id(kf_id);
//        }
//
//        privateChat.getChatAdapter().setOnUserInfoListener(new ChatInterface.OnUserInfoListener() {
//            @Override
//            public void onComplete(UserInfoLightweight userProfileSimple) {
//                if (userProfileSimple != null && whisperID == userProfileSimple.getUid()) {
//                    setNickName(userProfileSimple.getNickname());
//                    kf_id = userProfileSimple.getKf_id();
//                    name = userProfileSimple.getNickname();
//                    privateChat.getChatAdapter().setKf_id(userProfileSimple.getKf_id());
//                }
//            }
//        });
        privateChat.getChatAdapter().setWhisperId(whisperID);
        initHeadView();
    }

    private void initHeadView(){
        privatechat_head = (LinearLayout) findViewById(R.id.privatechat_head);
        findViewById(R.id.chat_title_attention).setOnClickListener(this);
        findViewById(R.id.chat_title_phone).setOnClickListener(this);
        findViewById(R.id.chat_title_wx).setOnClickListener(this);
        findViewById(R.id.chat_title_yb).setOnClickListener(this);

        chat_title_attention_icon = (ImageView) findViewById(R.id.chat_title_attention_icon);
        chat_title_attention_name = (TextView) findViewById(R.id.chat_title_attention_name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chat_title_attention:{
                break;
            }
            case R.id.chat_title_phone:
                UIShow.showCheckOtherInfoAct(this, whisperID);
                break;
            case R.id.chat_title_wx:
                UIShow.showCheckOtherInfoAct(this, whisperID);
                break;
            case R.id.chat_title_yb:
                UIShow.showGoodsYCoinDlgOld(this);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkReply();
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
    protected void onResume() {
        super.onResume();
    //    ModuleMgr.getTipsBarMgr().attach(TipsBarMsg.Chat_Page, viewGroup, getParmsJson());
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  ModuleMgr.getTipsBarMgr().detach();
      //  TipToolUtils.dismiss();
    }

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
                MMLog.printThrowable(e);
            }
        }
        lastActivity = this;
    }


}