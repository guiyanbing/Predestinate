package com.juxin.predestinate.module.local.chat.msgtype;

import com.juxin.predestinate.module.local.chat.inter.IBaseMessage;

/**
 * Created by Kind on 2017/3/17.
 */

public class BaseMessage implements IBaseMessage {

    private String channelID;
    private String whisperID;
    private long sendID;
    private long msgID;
    private long cMsgID;
    private String type;
    private int status;
    private int fStatus;
    private long time;
    private String content;


    @Override
    public BaseMessage parseJson(String jsonStr) {
        return null;
    }

    @Override
    public String getJson(BaseMessage message) {
        return null;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getWhisperID() {
        return whisperID;
    }

    public void setWhisperID(String whisperID) {
        this.whisperID = whisperID;
    }

    public long getSendID() {
        return sendID;
    }

    public void setSendID(long sendID) {
        this.sendID = sendID;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public long getcMsgID() {
        return cMsgID;
    }

    public void setcMsgID(long cMsgID) {
        this.cMsgID = cMsgID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getfStatus() {
        return fStatus;
    }

    public void setfStatus(int fStatus) {
        this.fStatus = fStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
