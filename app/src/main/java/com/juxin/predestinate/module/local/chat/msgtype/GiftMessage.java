package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 礼物消息
 * Created by Kind on 2017/4/19.
 */

public class GiftMessage extends BaseMessage {

    private int giftID;
    private int giftCount;
    private long giftLogID;

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64

        if(getType() == 20){
            this.setGiftID(object.optInt("gift_id"));
        }else {
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
        return super.getJson(message);
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