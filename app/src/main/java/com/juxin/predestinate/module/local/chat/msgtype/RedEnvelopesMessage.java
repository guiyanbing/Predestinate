package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 红包消息
 * Created by Kind on 2017/4/19.
 */

public class RedEnvelopesMessage extends BaseMessage {

    private long redLogID;

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setRedLogID(object.optLong("red_log_id"));
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public long getRedLogID() {
        return redLogID;
    }

    public void setRedLogID(long redLogID) {
        this.redLogID = redLogID;
    }
}
