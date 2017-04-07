package com.juxin.predestinate.module.logic.notify;

import android.text.TextUtils;
import android.view.View;

import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.notify.view.CustomFloatingPanel;
import com.juxin.predestinate.module.logic.notify.view.LockChatPanel;
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.CommonUtil;
import com.juxin.predestinate.module.util.MediaNotifyUtils;
import com.juxin.predestinate.module.util.UIShow;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 消息通知管理manager
 */
public class NotifyMgr implements ModuleBase {

    @Override
    public void init() {
    }

    @Override
    public void release() {
    }

    /**
     * @return 判断是否在睡眠模式的区间范围
     */
    public static Boolean isInSleep() {
        if (PSP.getInstance().getBoolean(Constant.SETTING_SLEEP_MESSAGE, Constant.SETTING_SLEEP_MESSAGE_DEFAULT)) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            return hour < 8 || hour >= 22;
        }
        return false;
    }

    //------------------新消息通知start--------------------

    //TODO 消息抛出提示
//    @Override
//    public void onChatRecently(boolean ret, List<BaseMessage> baseMessages) {
//    }
//
//    @Override
//    public void onChatHistory(boolean ret, List<BaseMessage> baseMessages) {
//    }
//
//    @Override
//    public void onChatUpdate(boolean ret, BaseMessage message) {
//        MMLog.autoDebug("---onChatUpdate--->ret：" + ret + "，message：" + MMLog.printObjectFields(message));
//        if (message.getSendID() == App.uid) return;
//
//        showTextNotify(message);
//    }

    //进行悬浮窗通知的消息类型
    private final ArrayList<Integer> NOTIFY_TYPE = new ArrayList<Integer>() {//进行悬浮窗提示的消息类型
        {
//            add(BaseMessage.BaseMessageType.text.getMsgType());         //文本消息:2
//            add(BaseMessage.BaseMessageType.voice.getMsgType());        //语音消息:10
//            add(BaseMessage.BaseMessageType.img.getMsgType());          //图片消息:12
//            add(BaseMessage.BaseMessageType.videoSmall.getMsgType());   //小视频消息:11
        }
    };

    /**
     * 进行聊天消息通知
     *
     * @param message 消息基类
     */
    private void showTextNotify(BaseMessage message) {
        if (!PSP.getInstance().getBoolean(Constant.SETTING_MESSAGE, Constant.SETTING_MESSAGE_DEFAULT)) {
            return;
        }
        show(-1, "");//TODO
    }

    public void show(int type, String content) {
//        if (!NOTIFY_TYPE.contains(type)) return; //如果不是提示列表中的某一种消息类型，就不进行提示

        // 请求用户资料
//        ModuleMgr.getChatMgr().getUserInfoLightweight(getSendID(), new ChatMsgInterface.InfoComplete() {
//            @Override
//            public void onReqComplete(boolean ret, final UserInfoLightweight simpleData) {
//                if (ret) {//资料获取成功
//                    MMLog.autoDebug("---文本消息提示--->type：" + type + "，content：" + content + "，simpleData：" + MMLog.printObjectFields(simpleData));
//                    if (simpleData == null || TextUtils.isEmpty(content)) return;
//                    if (!ModuleMgr.getMsgCommonMgr().isFriend(simpleData.getUid()) &&
//                            simpleData.getUid() != MailSpecialID.customerService.getSpecialID()) {
//                        MMLog.autoDebug("---文本消息提示--->陌生人");
//                        return;//如果不是好友，且不是小友客服，就不进行提示
//                    }
//
//                    if (simpleData.getUid() == MailSpecialID.customerService.getSpecialID()) {
//                        simpleData.setNickname(MailSpecialID.customerService.getSpecialIDName());
//                    }
                    UserInfoLightweight simpleData = new UserInfoLightweight();
                    if (BaseUtil.isScreenLock(App.context)) {//锁屏状态，锁屏弹窗
//                        content = ChatSmile.getSimpleSmiledText(content);
                        LockScreenMgr.getInstance().setChatData(simpleData, content);
                        popupActivity();
                    } else {//解锁
                        if (CommonUtil.isForeground() && ModuleMgr.getAppMgr().isForeground()) {//在前台，应用内悬浮窗
                            if (App.getActivity() instanceof BaseActivity &&
                                    !((BaseActivity) App.getActivity()).isCanNotify()) return;

                            final CustomFloatingPanel floatingPanelChat = new CustomFloatingPanel(App.context);
                            floatingPanelChat.initView();
                            floatingPanelChat.init(TextUtils.isEmpty(simpleData.getNickname()) ? String.valueOf(simpleData.getUid()) : simpleData.getNickname(),
                                    content, simpleData.getAvatar(), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            UIShow.showPrivateChatAct(App.activity, Long.valueOf(getWhisperID()), simpleData.getNickname());
                                            FloatingMgr.getInstance().removePanel(floatingPanelChat);
                                        }
                                    });
                            FloatingMgr.getInstance().addPanel(floatingPanelChat);
                        } else {//在后台，桌面悬浮窗
//                            if (BaseUtil.IsRunningForegroundMe(App.context) || (
//                                    App.appState != App.AppState.AS_Run && !LockScreenMgr.isTip())) {
//                                //应用在前台/(应用在退出状态且应用设置为退出不提示)：不进行应用外弹框
//                                MMLog.autoDebug("------>应用外弹框，return");
//                                return;
//                            }
                            UIShow.showUserMailNotifyAct(type, simpleData, content);
                        }
                        playSound();
                        vibrator();
                    }
//                }
//            }
//        });
    }
    //------------------新消息通知end--------------------

    /**
     * 震动
     */
    public void vibrator() {
        if (PSP.getInstance().getBoolean(Constant.SETTING_VIBRATION, Constant.SETTING_VIBRATION_DEFAULT)) {
            MediaNotifyUtils.vibrator();
        }
    }

    /**
     * 响铃
     */
    public void playSound() {
        if (PSP.getInstance().getBoolean(Constant.SETTING_VOICE, Constant.SETTING_VOICE_DEFAULT)) {
            MediaNotifyUtils.playSound();
        }
    }

    /**
     * 弹窗
     */
    public void popupActivity() {
        if (LockScreenMgr.getInstance().popupActivity(!isInSleep())) {
            playSound();
            vibrator();
        }
    }
}
