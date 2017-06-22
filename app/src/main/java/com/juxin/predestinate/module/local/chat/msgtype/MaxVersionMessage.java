package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 最大版本消息
 * Created by Kind on 2017/6/15.
 */

public class MaxVersionMessage extends BaseMessage {


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
        return super.getJson(message);
    }
}
