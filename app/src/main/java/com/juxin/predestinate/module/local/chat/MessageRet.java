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
    public static final int MSG_CODE_BALANCE_Y = -4;//Y币不够了
    public static final int MSG_CODE_OK = 0;//成功

    //{"d":3302,"msg_id":1166518,"s":0,"status":"ok","tm":1498109958}

    private long d;//本地消息ID
    private long msgId;//服务器消息ID
    private long tm;
    private boolean isOk;
    private int s;

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setD(jsonObject.optLong("d"));
        this.setMsgId(jsonObject.optLong("msg_id"));
        String status = jsonObject.optString("status");
        this.setOk("ok".equals(status));
        this.setTm(jsonObject.optLong("tm"));
        if (!jsonObject.isNull("s")) {//状态
            this.setS(jsonObject.optInt("s"));
        }
    }

    public long getD() {
        return d;
    }

    public void setD(long d) {
        this.d = d;
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