package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 系统消息
 * Created by Kind on 2017/4/19.
 */
public class SystemMessage extends BaseMessage {

    private int xtType;//系统消息类型（3为已读通知，5为正在输入，6为取消正在输入）

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setXtType(object.optInt("xt"));
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public int getXtType() {
        return xtType;
    }

    public void setXtType(int xtType) {
        this.xtType = xtType;
    }
}
