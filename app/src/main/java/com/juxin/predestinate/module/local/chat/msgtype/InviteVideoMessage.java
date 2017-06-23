package com.juxin.predestinate.module.local.chat.msgtype;

import android.os.Bundle;
import android.text.TextUtils;
import com.juxin.library.log.PLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 女性对男性的语音（视频）邀请
 * Created by Kind on 2017/6/21.
 */

public class InviteVideoMessage extends BaseMessage {

    private long invite_id;//邀请id，即为邀请流水号，接受邀请并发起视频的时候使用
    private int media_tp;//媒体类型1视频，2语音
    private long price;//聊天单价
    private long timeout_tm;//超时时间，unix时间戳

    public InviteVideoMessage() {
        super();
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

    public BaseMessage parseJs(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setRu(object.optInt("ru"));

        this.setInvite_id(object.optLong("vc_id"));
        this.setMedia_tp(object.optInt("media_tp"));
        this.setPrice(object.optLong("vc_price"));

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

            return json.toString();
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
        return null;
    }

    public InviteVideoMessage(Bundle bundle, boolean fletter) {
        super(bundle, fletter);
        convertJSON(getJsonStr());
    }

    /**
     * 转换类 fmessage
     */
    public InviteVideoMessage(Bundle bundle) {
        super(bundle);
        convertJSON(getJsonStr());
    }

    public long getInvite_id() {
        return invite_id;
    }

    public void setInvite_id(long invite_id) {
        this.invite_id = invite_id;
    }

    public int getMedia_tp() {
        return media_tp;
    }

    public void setMedia_tp(int media_tp) {
        this.media_tp = media_tp;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTimeout_tm() {
        return timeout_tm;
    }

    public void setTimeout_tm(long timeout_tm) {
        this.timeout_tm = timeout_tm;
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
        this.setInvite_id(object.optLong("invite_id"));
        this.setMedia_tp(object.optInt("media_tp"));
        this.setPrice(object.optLong("price"));
        this.setTimeout_tm(object.optLong("timeout_tm"));
    }
}