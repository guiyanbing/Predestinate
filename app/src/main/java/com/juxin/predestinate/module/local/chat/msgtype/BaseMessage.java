package com.juxin.predestinate.module.local.chat.msgtype;

import com.juxin.mumu.bean.utils.TypeConvUtil;
import com.juxin.predestinate.module.local.chat.inter.IBaseMessage;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatPanelType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.ui.mail.item.MailItemType;

/**
 * Created by Kind on 2017/3/17.
 */

public class BaseMessage implements IBaseMessage {

    private String channelID;
    private String whisperID;
    private long sendID;
    private long msgID;
    private long cMsgID;
    private int type;
    private int status;
    private int fStatus;
    private long time;
    private String content;
    private String contentJson;

    private int displayWidth = DisplayWidth.getDisplayWidth();//消息显示的宽度，默认是80

    private ChatPanelType msgPanelType;  // 消息面板用的：是否使用自定义消息面板。

    private int MailItemStyle = MailItemType.Mail_Item_Ordinary.type;//私聊列表样式

    public BaseMessage() {
        super();
    }

    //fmessage
    public BaseMessage(String channelID, String whisperID, long sendID, long msgID, long cMsgID,
                       int type, int status, int fStatus, long time, String contentJson) {
        this.channelID = channelID;
        this.whisperID = whisperID;
        this.sendID = sendID;
        this.msgID = msgID;
        this.cMsgID = cMsgID;
        this.type = type;
        this.status = status;
        this.fStatus = fStatus;
        this.time = time;
        this.contentJson = contentJson;
    }




    @Override
    public BaseMessage parseJson(String jsonStr) {
        return null;
    }

    @Override
    public String getJson(BaseMessage message) {
        return null;
    }


    /**
     * 是否为发送者
     */
    public boolean isSender() {
        return App.uid == getSendID();
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

    //转成LONG型
    public long getLWhisperID() {
        return TypeConvUtil.toLong(whisperID);
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public boolean isCustomMsgPanel() {
        return true;
        //  return ChatPanelType.CPT_Custome == msgPanelType;
    }

    public void setMsgPanelType(ChatPanelType msgPanelType) {
        this.msgPanelType = msgPanelType;
    }

    public ChatPanelType getMsgPanelType() {
        return msgPanelType;
    }

    public int getMailItemStyle() {
        return MailItemStyle;
    }

    public void setMailItemStyle(int mailItemStyle) {
        MailItemStyle = mailItemStyle;
    }
}
