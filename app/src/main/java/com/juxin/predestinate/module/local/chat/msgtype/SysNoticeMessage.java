package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 系统通知消息
 * Created by Kind on 2017/6/13.
 */

public class SysNoticeMessage extends BaseMessage {

    private String info;//消息详细文本
    private String pic;//消息中的图片
    private String btn_text;//按钮文字
    private String btn_action;//按钮动作

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64

        this.setInfo(object.optString("info"));
        this.setPic(object.optString("pic"));
        this.setBtn_text(object.optString("btn_text"));
        this.setBtn_action(object.optString("btn_action"));
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBtn_text() {
        return btn_text;
    }

    public void setBtn_text(String btn_text) {
        this.btn_text = btn_text;
    }

    public String getBtn_action() {
        return btn_action;
    }

    public void setBtn_action(String btn_action) {
        this.btn_action = btn_action;
    }
}
