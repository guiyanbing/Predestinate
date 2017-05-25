package com.juxin.predestinate.module.local.chat.msgtype;

import com.juxin.library.log.PLogger;
import org.json.JSONArray;
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

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);

        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setRu(object.optInt("ru"));
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        JSONObject json = new JSONObject();
        try {
            json.put("tid", new JSONArray().put(message.getWhisperID()));
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

    public TextMessage(long id, String channelID, String whisperID, long sendID, long msgID, long cMsgID, long specialMsgID,
                       int type, int status, int fStatus, long time, String jsonStr) {
        super(id, channelID, whisperID, sendID, msgID, cMsgID, specialMsgID, type, status, fStatus, time, jsonStr);
        convertJSON(getJsonStr());
    }

    //私聊列表
    public TextMessage(long id, String userID, String infoJson, int type, int kfID,
                       int status, int ru, long time, String content, int num) {
        super(id, userID, infoJson, type, kfID, status, ru, time, content, num);
        convertJSON(getJsonStr());
    }

    @Override
    public void convertJSON(String jsonStr) {
        super.convertJSON(jsonStr);
        this.setMsgDesc(getJsonObject(jsonStr).optString("mct")); //消息内容
    }
}
