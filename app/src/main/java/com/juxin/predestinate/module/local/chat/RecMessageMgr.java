package com.juxin.predestinate.module.local.chat;

import android.content.Intent;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.VideoMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收消息处理类
 * Created by Kind on 2017/4/1.
 */

public class RecMessageMgr implements IMProxy.IMListener {

    public void init() {
        IMProxy.getInstance().attach(this);
    }

    public void release() {
        IMProxy.getInstance().detach(this);
    }

    @Override
    public void onMessage(long msgID, boolean group, String groupId, long senderID, String jsonStr) {
        try {
            PLogger.d("reMsg-jsonStr=" + jsonStr);
            JSONObject object = new JSONObject(jsonStr);
            if (senderID <= 0) {//如果小于或等于0
                senderID = object.optLong("fid");
            }
            int type = object.optInt("mtp");
            notifyPlugin(object);
            BaseMessage.BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(type);
            BaseMessage message;
            //基本消息
            if (messageType != null) {
                message = messageType.msgClass.newInstance();
                onSaveSendUI(true, message, group, msgID, groupId, senderID, jsonStr, type);
            } else {
                if (group) {//是群聊
                    //如果是重复消息或小于当前ID的消息就扔掉
                    if (!checkNewMsgGId(msgID) && msgID != -1) {
                        PLogger.d("重复群聊消息：" + this.recMsgGId + "-" + msgID);
                        return;
                    }
                } else {
                    //如果是重复消息或小于当前ID的消息就扔掉
                    if (!checkNewMsgId(msgID) && msgID != -1) {
                        PLogger.d("重复私聊消息：" + this.recMsgId + "-" + msgID);
                        return;
                    }
                }
                message = new BaseMessage();
                onSaveSendUI(false, message, group, msgID, groupId, senderID, jsonStr, type);
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    private void notifyPlugin(JSONObject object) {
        try {
            int mtp = object.getInt("mtp");
            String mct = object.optString("mct");
            long fid = object.optLong("fid");
            JSONObject jo = null;
            if (mtp == 2 && !TextUtils.isEmpty(mct)) {
                jo = new JSONObject();
                jo.put("mt", 2);
                jo.put("text", mct);
            }

            if (mtp == 10) {
                JSONObject giftJo = object.getJSONObject("gift");
                if(giftJo.getInt("recved") == 1){
                    jo = new JSONObject();
                    jo.put("mt", 4);
                    jo.put("gift_id", giftJo.getInt("gift_id"));
                    jo.put("num",giftJo.getInt("count"));
                }
            }

            if (mtp == 26) {
                jo = new JSONObject();
                jo.put("mt", 6);
                jo.put("ctype", object.getInt("ctype"));// 1关闭视频 2打开视频
            }

            if (mtp == 27) {
                jo = new JSONObject();
                jo.put("mt", 5);
                JSONObject dataJo = object.getJSONObject("g_data");
                jo.put("wave_index", dataJo.getInt("wave_index"));
            }
            if (jo != null) {
                jo.put("fid", fid);
                jo.put("msgId",object.getLong("d"));
                Intent intent = new Intent("com.juxin.action.plugin");
                intent.putExtra("extra_json", jo.toString());
                App.context.sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onSaveSendUI(boolean isSave, BaseMessage message, boolean group, long msgID,
                              String groupId, long senderID, String jsonStr, int type) throws JSONException {
        PLogger.d(message.getWhisperID());
        message.setSendID(senderID);
        message.setMsgID(msgID);
        message.setType(type);
        message.parseJson(jsonStr);
        message.setDataSource(MessageConstant.TWO);

        if (group) {// 群聊
            message.setChannelID(groupId);
            ModuleMgr.getChatMgr().onChatMsgUpdate(message.getChannelID(), null, true, message);
        } else {//私聊或群私聊
            if (!TextUtils.isEmpty(groupId)) {//群里面的私聊
                message.setChannelID(groupId);
            }

            message.setWhisperID(String.valueOf(senderID));
            //接收特殊消息
            ModuleMgr.getChatListMgr().setSpecialMsg(message);
            if (BaseMessage.TalkRed_MsgType == message.getType() || BaseMessage.System_MsgType == message.getType()) {//红包消息不保存，也不通知上层
                return;
            }

            if (BaseMessage.Follow_MsgType == message.getType() || BaseMessage.RedEnvelopesBalance_MsgType == message.getType()) {
                isSave = false;
            }

            if (isSave) {//是否保存
                message.setInfoJson(null);
                if (BaseMessage.BaseMessageType.video.getMsgType() == message.getType()) {
                    VideoMessage videoMessage = (VideoMessage) message;
                    //   reMsg-jsonStr={"d":167211,"fid":110872900,"mct":"","media_tp":1,"mt":1496199691,"mtp":24,"ru":1,"tid":110872803,"vc_esc_code":3,"vc_id":100000496,"vc_tp":3}
                    if (VideoAudioChatHelper.getInstance().isContain(videoMessage.getVideoID())) {//发送方
                        message.setSendID(App.uid);
                    }

                    //3拒绝或取消 4挂断（挂断可能会收到不止一次）
                    if (videoMessage.getVideoTp() == 3 || videoMessage.getVideoTp() == 4) {
                        VideoAudioChatHelper.getInstance().remove(videoMessage.getVideoID());
                    }
                    //{"d":167075,"fid":110872922,"mct":"","media_tp":1,"mt":1495891670,"mtp":24,"ru":1,"tid":110872541,"vc_id":100000459,"vc_tp":1}
                    ModuleMgr.getChatMgr().onReceivingVideo((VideoMessage) message);
                } else {
                    ModuleMgr.getChatMgr().onReceiving(message);
                }
            } else {
                ModuleMgr.getChatMgr().onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), true, message);
            }
        }
    }

    /**
     * 检测私聊新的消息Id是否是合法的，保存并写入文件。
     *
     * @param msgId 新的私聊消息Id。
     * @return 合法的返回true。
     */
    private long recMsgId = 0;
    private final String REC_KEY_MSGID = "rec_key_message";
    private final long REC_DEFVALUE_MSGID = 0;

    public synchronized boolean checkNewMsgId(long msgId) {
        if (recMsgId == 0) {
            recMsgGId = PSP.getInstance().getLong(REC_KEY_MSGID, REC_DEFVALUE_MSGID);
        }
        if (this.recMsgId >= msgId) {
            return false;
        }

        this.recMsgId = msgId;
        PSP.getInstance().put(REC_KEY_MSGID, msgId);
        return true;
    }

    private long recMsgGId = 0;
    private final String REC_KEY_GMSGID = "rec_key_gmessage";

    /**
     * 检测新的消息Id是否是合法的，保存并写入文件。
     *
     * @param msgId 新的群聊消息Id。
     * @return 合法的返回true。
     */
    public synchronized boolean checkNewMsgGId(long msgId) {
        if (recMsgGId == 0) {
            recMsgGId = PSP.getInstance().getLong(REC_KEY_GMSGID, REC_DEFVALUE_MSGID);
        }
        if (this.recMsgGId >= msgId) {
            return false;
        }

        this.recMsgGId = msgId;
        PSP.getInstance().put(REC_KEY_GMSGID, msgId);
        return true;
    }
}