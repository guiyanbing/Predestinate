package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * Created by Kind on 2017/4/19.
 */

public class HtmlMessage extends BaseMessage {

    private String htm;

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setHtm(object.optString("htm"));
        this.setRu(object.optInt("ru"));
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public String getHtm() {
        return htm;
    }

    public void setHtm(String htm) {
        this.htm = htm;
    }
}
