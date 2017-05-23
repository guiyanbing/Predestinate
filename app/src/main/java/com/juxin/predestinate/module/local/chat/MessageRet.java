package com.juxin.predestinate.module.local.chat;

import com.juxin.predestinate.bean.net.BaseData;
import org.json.JSONObject;

/**
 * 消息返回
 * Created by Kind on 2017/5/11.
 */
public class MessageRet extends BaseData {

    public static final int MSG_CODE_BALANCE_INSUFFICIENT = -1;//余额不足或者不是VIP
    public static final int MSG_CODE_PULL_BLACK = -2;//已拉黑
    public static final int MSG_CODE_OK = 0;//成功

    //  {"d":301,"s":-1,"status":"ok","tm":1494484725}

    private long msgId;
    private long tm;
    private boolean isOk;
    private int s;

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setMsgId(jsonObject.optLong("msgId"));
        String status = jsonObject.optString("status");
        this.setOk("ok".equals(status));
        this.setTm(jsonObject.optLong("tm"));
        if (!jsonObject.isNull("s")) {//状态
            this.setS(jsonObject.optInt("s"));
        }
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getTm() {
        return tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public int getS() {
        return s;
    }

    public boolean isS() {
        return s == MSG_CODE_OK;
    }

    public void setS(int s) {
        this.s = s;
    }
}