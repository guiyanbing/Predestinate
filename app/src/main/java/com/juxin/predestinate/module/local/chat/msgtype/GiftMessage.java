package com.juxin.predestinate.module.local.chat.msgtype;

import android.text.TextUtils;

import com.juxin.library.log.PLogger;
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


    public GiftMessage(String channelID, String whisperID, int giftID, int giftCount, long giftLogID) {
        super(channelID, whisperID);
        this.setGiftID(giftID);
        this.setGiftCount(giftCount);
        this.setGiftLogID(giftLogID);
        this.setType(BaseMessageType.common.getMsgType());
    }

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64

        if(getType() == 20){
            this.setGiftID(object.optInt("gift_id"));
        }else {//10
            if(!object.isNull("gift")){
                JSONObject giftJSON = object.optJSONObject("gift");
                this.setGiftCount(giftJSON.optInt("count"));
                this.setGiftID(giftJSON.optInt("gift_id"));
                this.setGiftLogID(giftJSON.optLong("gift_log_id"));
            }
        }
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        JSONObject json = new JSONObject();
        try {
            json.put("tid", message.getWhisperID());
            json.put("mtp", message.getType());
            json.put("mt", message.getTime());
            json.put("d", message.getMsgID());

            if(!TextUtils.isEmpty(message.getMsgDesc())){
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
}