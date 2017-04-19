package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 关注消息
 * Created by Kind on 2017/4/19.
 */

public class ConcernMessage extends BaseMessage {

    private int gzStatus;//关注状态1为关注2为取消关注

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setGzStatus(object.optInt("gz"));

        return this;

    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public int getGzStatus() {
        return gzStatus;
    }

    public void setGzStatus(int gzStatus) {
        this.gzStatus = gzStatus;
    }
}
