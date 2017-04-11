package com.juxin.predestinate.module.local.chat;

import android.text.TextUtils;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收消息处理类
 * Created by Kind on 2017/4/1.
 */

public class RecMessageMgr implements IMProxy.IMListener{

    public void init() {
        IMProxy.getInstance().attach(this);
    }

    public void release() {
        IMProxy.getInstance().detach(this);
    }

    @Override
    public void onMessage(long msgID, boolean group, String groupId, long senderID, String jsonStr) {
        try {
            MMLog.autoDebug("reMsg-jsonStr=" + jsonStr);
            JSONObject object = new JSONObject(jsonStr);
            if(senderID <= 0){//如果小于或等于0
                senderID = object.optLong("fid");
            }
            int type = object.optInt("mtp");

            BaseMessage.BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(type);
            BaseMessage message;
            //基本消息
            if (messageType != null) {
                message = messageType.msgClass.newInstance();
                onSaveSendUI(true, message, group, msgID, groupId, senderID, jsonStr, type);
            } else {
                if(group){//是群聊
                    //如果是重复消息或小于当前ID的消息就扔掉
                    if (!checkNewMsgGId(msgID) && msgID != -1) {
                        MMLog.autoDebug("重复群聊消息：" + this.recMsgGId + "-" + msgID);
                        return;
                    }
                }else{
                    //如果是重复消息或小于当前ID的消息就扔掉
                    if (!checkNewMsgId(msgID) && msgID != -1) {
                        MMLog.autoDebug("重复私聊消息：" + this.recMsgId + "-" + msgID);
                        return;
                    }
                }
                message = new BaseMessage();
                onSaveSendUI(false, message, group, msgID, groupId, senderID, jsonStr, type);
            }
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }
    }

    private void onSaveSendUI(boolean isSave, BaseMessage message, boolean group, long msgID, String groupId, long senderID, String jsonStr, int type) throws JSONException {
        MMLog.autoDebug(message.getWhisperID());
        message.setSendID(senderID);
        message.setMsgID(msgID);
        message.setType(type);
        message.parseJson(jsonStr);
        message.setDataSource(BaseMessage.TWO);

        if (group) {// 群聊
            message.setChannelID(groupId);
            ModuleMgr.getChatMgr().onChatMsgUpdate(message.getChannelID(), null, true, message);
        } else {//私聊或群私聊
            if (!TextUtils.isEmpty(groupId)) {//群里面的私聊
                message.setChannelID(groupId);
            }
            message.setWhisperID(String.valueOf(senderID));
            JSONObject object = new JSONObject(jsonStr);

            //接收特殊消息
//            ModuleMgr.getChatListMgr().setSpecialMsg(message);
//            if(BaseMessage.addFriend_MsgType == message.getType() && ((FriendsMessage)message).getAddtype() == 2){
//                return;
//            }
//
//            if(BaseMessage.system_MsgType == message.getType()){//系统消息不保存
//                isSave = false;
//            }

            if (isSave) {//是否保存
                ModuleMgr.getChatMgr().onReceiving(message);
//              if(BaseMessage.BaseMessageType.hint.toString().equals(type)){//灰色消息
//                  if(((RoseMessage)message).isSave()){//如果是true就存 否则不存储
//                        ModuleMgr.getChatMgr().onReceiving(message);
//                  }else {
//                        ModuleMgr.getChatMgr().onChatMsgUpdate(null, message.getWhisperID(), true, message);
//                    }
//                }else{
//                    ModuleMgr.getChatMgr().onReceiving(message);
//                }
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
    //        recMsgId = ModuleMgr.getCfgMgr().getLong(REC_KEY_MSGID, REC_DEFVALUE_MSGID);
        }
        if (this.recMsgId >= msgId) {
            return false;
        }

        this.recMsgId = msgId;
  //      ModuleMgr.getCfgMgr().setLong(REC_KEY_MSGID, msgId);
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
          //  recMsgGId = ModuleMgr.getCfgMgr().getLong(REC_KEY_GMSGID, REC_DEFVALUE_MSGID);
        }
        if (this.recMsgGId >= msgId) {
            return false;
        }

        this.recMsgGId = msgId;
    //    ModuleMgr.getCfgMgr().setLong(REC_KEY_GMSGID, msgId);
        return true;
    }
}
