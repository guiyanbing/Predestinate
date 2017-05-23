package com.juxin.predestinate.module.local.chat.msgtype;

import com.juxin.library.log.PLogger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 文本消息包括打招呼
 * Created by Kind on 2017/3/31.
 */

public class TextMessage extends BaseMessage {

    //打招呼
    private int kf;
    private int sayHelloType;

    //关注状态1为关注2为取消关注
    private int gz;

    public TextMessage() {
        super();
    }

    /**
     * 打招呼
     * @param whisperID
     * @param content
     * @param kf
     * @param sayHelloType
     */
    public TextMessage(String whisperID, String content, int kf, int sayHelloType) {
        super(null, whisperID);
        this.setMsgDesc(content);
        this.setType(BaseMessageType.hi.getMsgType());
        this.setKf(kf);
        this.setSayHelloType(sayHelloType);
    }


    /**
     * 构造文本消息
     *
     * @param channelID 频道ID
     * @param whisperID 私聊ID
     * @param content
     * @return
     */
//    public TextMessage(String channelID, String whisperID, String content) {
//        super(channelID, whisperID);
//        this.setMsgDesc(content);
//        this.setType(BaseMessageType.text.getMsgType());
//    }

    /**
     * 机器人专用
     * @param whisperID
     * @param content
     */
//    public TextMessage(long whisperID, String content) {
//        super();
//        this.setChannelID(null);
//        this.setWhisperID(String.valueOf(whisperID));
//        this.setSendID(whisperID);
//        this.setTime(getCurrentTime());
//        this.setcMsgid(getCMsgID());
//        this.setMsgDesc(content);
//        this.setType(BaseMessageType.text.getMsgType());
//    }

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);

        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        JSONObject json = new JSONObject();
        try {
            json.put("tid", message.getWhisperID());
            json.put("mtp", message.getType());
            json.put("mct", message.getMsgDesc());
            json.put("mt", getCurrentTime());
            json.put("d", message.getMsgID());

            int kf = ((TextMessage)message).getKf();
            int sayHelloType = ((TextMessage)message).getSayHelloType();

            if(kf != -1){
                json.put("kf", kf);
            }
            if(sayHelloType != -1){
                json.put("j", sayHelloType);
            }

            return json.toString();
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
        return null;
    }

//    public TextMessage(Map<String, Object> map, int type) {
//        super(map, type);
//        convertJSON(map);
//    }
//
//    public TextMessage(int type, Map<String, Object> map) {
//        super(type, map);
//        convertJSON(map);
//    }

//    @Override
//    public void convertJSON(Map<String, Object> map) {
//        super.convertJSON(map);
//        this.setMsgDesc(getJsonObject(getJsonStr()).optString("mct"));
//    }


    public int getKf() {
        return kf;
    }

    public void setKf(int kf) {
        this.kf = kf;
    }

    public int getSayHelloType() {
        return sayHelloType;
    }

    public void setSayHelloType(int sayHelloType) {
        this.sayHelloType = sayHelloType;
    }

    public TextMessage(BaseMessage message) {
        super(message.getChannelID(), message.getWhisperID(), message.getSendID(), message.getMsgID(),
                message.getcMsgID(), message.getSpecialMsgID(), message.getType(),message.getStatus(),
                message.getfStatus(), message.getTime(), message.getJsonStr());
        parseJson(getJsonStr());
    }
}
