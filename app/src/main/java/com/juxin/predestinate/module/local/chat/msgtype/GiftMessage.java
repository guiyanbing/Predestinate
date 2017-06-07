package com.juxin.predestinate.module.local.chat.msgtype;

import android.os.Bundle;
import android.text.TextUtils;
import com.juxin.library.log.PLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 礼物消息
 * Created by Kind on 2017/4/19.
 */

public class GiftMessage extends BaseMessage {

    private int giftID;
    private int giftCount;
    private long giftLogID;

    //临时发送用的
    private int gType;

    public GiftMessage() {
        super();
    }

    public GiftMessage(String channelID, String whisperID, int giftID, int giftCount) {
        super(channelID, whisperID);
        this.setGiftID(giftID);
        this.setGiftCount(giftCount);
    //    this.setGiftLogID(giftLogID);
        this.setType(BaseMessageType.gift.getMsgType());
    }

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setRu(object.optInt("ru"));

        parseCommonJson(object);
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        JSONObject json = new JSONObject();
        try {
            json.put("tid", new JSONArray().put(message.getWhisperID()));
            json.put("mtp", message.getType());
            json.put("mt", message.getTime());
            json.put("d", message.getMsgID());

            if (!TextUtils.isEmpty(message.getMsgDesc())) {
                json.put("mct", message.getMsgDesc());
            }

            JSONObject tmpGift = new JSONObject();
            tmpGift.put("count", ((GiftMessage) message).getGiftCount());
            tmpGift.put("gift_id", ((GiftMessage) message).getGiftID());
            tmpGift.put("gift_log_id", ((GiftMessage) message).getGiftLogID());
            json.put("gift", tmpGift);

            return json.toString();
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
        return null;
    }

    public GiftMessage(Bundle bundle, boolean fletter) {
        super(bundle, fletter);
        convertJSON(getJsonStr());
    }

    /**
     * 转换类 fmessage
     */
    public GiftMessage(Bundle bundle) {
        super(bundle);
        convertJSON(getJsonStr());
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getGiftID() {
        return giftID;
    }

    public void setGiftID(int giftID) {
        this.giftID = giftID;
    }

    public long getGiftLogID() {
        return giftLogID;
    }

    public void setGiftLogID(long giftLogID) {
        this.giftLogID = giftLogID;
    }

    public int getgType() {
        return gType;
    }

    public void setgType(int gType) {
        this.gType = gType;
    }

    @Override
    public void convertJSON(String jsonStr) {
        super.convertJSON(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setMsgDesc(object.optString("mct")); //消息内容

        parseCommonJson(object);
    }

    /**
     * 礼物消息数据解析
     */
    private void parseCommonJson(JSONObject object) {
        //索要礼物
        if (object.has("gift_id")) this.setGiftID(object.optInt("gift_id"));
        //收到礼物
        if (object.has("gift")) {
            JSONObject giftJSON = object.optJSONObject("gift");
            this.setGiftCount(giftJSON.optInt("count"));
            this.setGiftID(giftJSON.optInt("gift_id"));
            this.setGiftLogID(giftJSON.optLong("gift_log_id"));
        }
    }
}