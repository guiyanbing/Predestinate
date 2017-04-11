package com.juxin.predestinate.module.local.chat.msgtype;

import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.utils.TypeConvUtil;
import com.juxin.predestinate.module.local.chat.inter.IBaseMessage;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatPanelType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.ui.mail.item.MailItemType;

/**
 * Created by Kind on 2017/3/17.
 */

public class BaseMessage implements IBaseMessage {

    //数据来源 1.本地  2.网络  3.离线(默认是本地) 4.模拟
    public static int ONE = 1;
    public static int TWO = 2;
    public static int THREE = 3;
    public static int FOUR = 4;


    public enum BaseMessageType {
        text(TextMessage.class, 2),//文本消息
//        hi(SayMessage.class, 3),//打招呼
//        read(SystemMessage.class, 7),//已读
//        heart(HeartMessage.class, 8),//对我心动的消息
//        hint(HintMessage.class, 9), //小提示，在消息框中为灰色小字
//        voice(VoiceMessage.class, 10), //语音消息
//        videoSmall(VideoSmallMessage.class, 11), //小视频
//        img(ImgMessage.class, 12), //图片
//        act(ActivityMessage.class, 13),//活动
//        addFriend(FriendsMessage.class, 14),//好友消息
//        game(InterActMessage.class, 15),//游戏互动消息
//        html(HtmlMessage.class, 17)//html消息
        ;
        //  del_msg(DeleteMessage.class),//删除消息类型

        public Class<? extends BaseMessage> msgClass = null;
        public int msgType;

        BaseMessageType(Class<? extends BaseMessage> msgClass, int msgType) {
            this.msgClass = msgClass;
            this.msgType = msgType;
        }

        public static BaseMessageType getBaseMessageByType(String str) {
            for (BaseMessageType messageType : BaseMessageType.values()) {
                if (messageType.toString().equals(str)) {
                    return messageType;
                }
            }
            return null;
        }

        public static BaseMessageType valueOf(int msgType) {
            for (BaseMessageType messageType : BaseMessageType.values()) {
                if (messageType.getMsgType() == msgType) {
                    return messageType;
                }
            }
            return null;
        }

        /**
         * 获取消息对应的结构体
         *
         * @param type
         * @return
         */
        public static Class<? extends BaseMessage> getMsgClass(int type) {
            try {
                BaseMessageType messageType = BaseMessageType.valueOf("Msg_" + type);
                return messageType.msgClass;
            } catch (Exception e) {
                MMLog.autoDebug(type);
            }
            return null;
        }

        public int getMsgType() {
            return this.msgType;
        }
    }

    private int dataSource = 1;//数据来源 1.本地  2.网络  3.离线(默认是本地) 4.模拟消息

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

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }
}
