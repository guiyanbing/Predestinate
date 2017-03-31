package com.juxin.predestinate.module.local.chat.msgtype;

import com.juxin.mumu.bean.log.MMLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Kind on 2017/3/31.
 */

public class TextMessage extends BaseMessage {

    public TextMessage() {
        super();
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

//        JSONObject object = getJsonObject(jsonStr);
//        this.setType(object.optInt("mtp")); //消息类型
//        this.setMsgDesc(object.optString("mct")); //消息内容
//        this.setTime(object.optLong("mt")); //消息时间 int64
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        JSONObject json = new JSONObject();
        try {
            json.put("mtp", message.getType());
   //         json.put("mct", message.getMsgDesc());
  //          json.put("mt", getCurrentTime());
            return json.toString();
        } catch (JSONException e) {
            MMLog.printThrowable(e);
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
}
