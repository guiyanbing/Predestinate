package com.juxin.predestinate.module.local.chat.msgtype;

import android.text.TextUtils;
import com.juxin.library.log.PLogger;
import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.predestinate.module.local.chat.inter.IBaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MsgIDUtils;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatPanelType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.ui.mail.item.MailItemType;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * [消息类型处理](http://doc.dev.yuanfenba.net/pkg/yuanfen/common/msg_data/)
 * Created by Kind on 2017/3/17.
 */
public class BaseMessage implements IBaseMessage {

    public enum BaseMessageType {

        common(CommonMessage.class, 2),//文本消息
        hi(TextMessage.class, 3),//打招呼
        gift(GiftMessage.class, 10),//礼物消息
        hint(TextMessage.class, 14),//小提示消息
        wantGift(TextMessage.class, 15),//索要礼物消息
        html(HtmlMessage.class, 19),//html消息
        wantGiftTwo(GiftMessage.class, 20),//索要礼物消息第二版
        video(VideoMessage.class, 24),//视频消息
        ;

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
                PLogger.d("Msg_" + type);
            }
            return null;
        }

        public int getMsgType() {
            return this.msgType;
        }
    }

    //数据来源 1.本地  2.网络  3.离线(默认是本地) 4.模拟
    public static int ONE = 1;
    public static int TWO = 2;
    public static int THREE = 3;
    public static int FOUR = 4;

    public final static int NumDefault = 0;//数字默认值
    public final static String StrDefault = "";//字符默认值

    //显示权重
    public static int Max_Weight = 1000;//最大权重
    public static int Great_Weight = 100;//大权重
    public static int In_Weight = 50;//中等权重
    public static int Small_Weight = 1;//小权重

    /**
     * 消息类型，进行未读消息比对
     */
    public static final int Follow_MsgType = 5;//关注
    public static final int System_MsgType = 7;//系统消息
    public static final int TalkRed_MsgType = 12;//聊天红包
    public static final int RedEnvelopesBalance_MsgType = 17;//红包余额变动消息

    @Override
    public BaseMessage parseJson(String jsonStr) {
        PLogger.d(jsonStr);
        this.setJsonStr(jsonStr);
        return null;
    }

    @Override
    public String getJson(BaseMessage message) {
        return null;
    }

    /**
     * 判断是否为群私聊
     */
    public boolean isWhisper() {
        return !TextUtils.isEmpty(getChannelID()) && !TextUtils.isEmpty(getWhisperID());
    }

    /**
     * 是否为发送者
     */
    public boolean isSender() {
        return App.uid == getSendID();
    }

    private int displayWidth = DisplayWidth.getDisplayWidth();//消息显示的宽度，默认是80

    private long id;//自增ID
    private String channelID;// 频道ID 群ID
    private String whisperID;//私聊ID
    private long sendID;// 发送ID
    private long msgID = -1;//服务器消息ID
    private long cMsgID = -1;//客户端消息ID
    private long specialMsgID = -1;//客户端消息ID
    private long time;
    private String content;//具体内容
    private String jsonStr;//json串
    private int status;//1.发送成功2.发送失败3.发送中 10.未读11.已读//12未审核通过   私聊列表中是最后一条消息的状态
    private int fStatus = -1; // 给所有具有操作状态的消息用。1 表示可以操作；0 表示已经处理过
    private int type;//消息类型
    private int dataSource = 1;//数据来源 1.本地  2.网络  3.离线(默认是本地) 4.模拟消息
    private String customtype;//自定义类型
    private int version = 1;//版本
    private boolean isResending = false;//是否重发中
    private boolean isValid = false;//是否有效当前消息,用于五分钟内重发用
    private String msgDesc;//消息描述 mct
    private long ru;

    private boolean isRead = false;//未读消息（true已经是读过了）//这个字段专门给数据库用的，不是给界面用的
    private boolean isSave;//是否保存
    private boolean isAutoplay = false;//是否自动播放


    //这几个字段 目前基本是给私聊用的
    private String infoJson;//个人资料json
    private String name;// 名称
    private String avatar;// 头像
    private int localAvatar;// 本地头像
    private String aboutme;// 内心读白
    private int isMonth;//包月
    private int isVip;
    private int isOnline;//是否在线
    private int kfID;//是否是机器人
    private int num;//私聊列表专用字段
    private int Weight = Small_Weight;//Item的权重
    private int MailItemStyle = MailItemType.Mail_Item_Ordinary.type;//私聊列表样式

    public int getMailItemStyle() {
        return MailItemStyle;
    }

    public void setMailItemStyle(int mailItemStyle) {
        MailItemStyle = mailItemStyle;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getLocalAvatar() {
        return localAvatar;
    }

    public void setLocalAvatar(int localAvatar) {
        this.localAvatar = localAvatar;
    }

    /***********************/
    private ChatPanelType msgPanelType;  // 消息面板用的：是否使用自定义消息面板。

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    //转成LONG型
    public long getLWhisperID() {
        return TypeConvertUtil.toLong(whisperID);
    }

    public void setWhisperID(String whisperID) {
        this.whisperID = whisperID;
    }

    public String getSSendID() {
        return String.valueOf(sendID);
    }

    public long getSendID() {
        return sendID;
    }

    public void setSendID(long sendID) {
        this.sendID = sendID;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(int isMonth) {
        this.isMonth = isMonth;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean isAutoplay() {
        return isAutoplay;
    }

    public void setAutoplay(boolean autoplay) {
        isAutoplay = autoplay;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public JSONObject getJsonObj() {
        return getJsonObject(jsonStr);
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public long getSpecialMsgID() {
        return specialMsgID;
    }

    public void setSpecialMsgID(long specialMsgID) {
        this.specialMsgID = specialMsgID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getfStatus() {
        return fStatus;
    }

    public void setfStatus(int fStatus) {
        this.fStatus = fStatus;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSendError() {
        return status == 2;
    }

    public static long getCMsgID() {
        return MsgIDUtils.getMsgIDUtils().getMsgID();
    }

    public long getCurrentTime() {
        return ModuleMgr.getAppMgr().getTime();
    }

    public long getConversionTime(String date) {//转换时间
        return TimeUtil.stringTDateToMillisecond(date);
    }

    public boolean isCustomMsgPanel() {
        return ChatPanelType.CPT_Custome == msgPanelType;
    }

    public void setMsgPanelType(ChatPanelType msgPanelType) {
        this.msgPanelType = msgPanelType;
    }

    public ChatPanelType getMsgPanelType() {
        return msgPanelType;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public String getCustomtype() {
        return customtype;
    }

    public void setCustomtype(String customtype) {
        this.customtype = customtype;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getIsOnline() {
        return isOnline;
    }

//    public boolean isOnline() {
//        return ModuleMgr.getCenterMgr().isOnline(isOnline);
//    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getKfID() {
        return kfID;
    }

    public void setKfID(int kfID) {
        this.kfID = kfID;
    }

    /**
     * 是否是机器人
     *
     * @return ture是机器人
     */
//    public boolean isKF_ID() {
//        return ModuleMgr.getCenterMgr().isRobot(getKf_id());
//    }
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isResending() {
        return isResending;
    }

    public void setResending(boolean resending) {
        isResending = resending;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRu() {
        return ru;
    }

    /**
     * true 如果为1则为熟人消息，否则为0
     *
     * @return
     */
    public boolean isRu() {
        return ru == 1;
    }

    public void setRu(long ru) {
        this.ru = ru;
    }

    public JSONObject getJsonObject(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return new JSONObject(str);
            }
        } catch (JSONException var3) {
            PLogger.printThrowable(var3);
        }

        return new JSONObject();
    }

    public BaseMessage() {
        super();
    }

    public BaseMessage(String channelID, String whisperID) {
        super();
        this.setChannelID(channelID);
        this.setWhisperID(whisperID);
        this.setSendID(App.uid);
        this.setTime(getCurrentTime());
        this.setcMsgID(getCMsgID());
        this.setMsgID(getcMsgID());
        PLogger.d("getCMsgID()=" + getcMsgID() + "");
    }

    //fmessage
    public BaseMessage(String channelID, String whisperID, long sendID, long msgID, long cMsgID, long specialMsgID,
                       int type, int status, int fStatus, long time, String jsonStr) {
        this.channelID = channelID;
        this.whisperID = whisperID;
        this.sendID = sendID;
        this.msgID = msgID;
        this.cMsgID = cMsgID;
        this.specialMsgID = specialMsgID;
        this.type = type;
        this.status = status;
        this.fStatus = fStatus;
        this.time = time;
        this.jsonStr = jsonStr;
    }

    //内容表
    public BaseMessage(Map<String, Object> map, int type) {
        this.setId(Long.parseLong(map.get("id").toString()));
        this.setChannelID(map.get("channelId") == null ? StrDefault : map.get("channelId").toString());
        this.setWhisperID(map.get("whisperID") == null ? StrDefault : map.get("whisperID").toString());
        this.setSendID(map.get("sendId") == null ? NumDefault : TypeConvertUtil.toLong(map.get("sendId").toString()));
        this.setMsgID(map.get("msgid") == null ? NumDefault : TypeConvertUtil.toLong(map.get("msgid").toString()));
        this.setcMsgID(map.get("cMsgid") == null ? NumDefault : TypeConvertUtil.toLong(map.get("cMsgid").toString()));
        this.setStatus(map.get("status") == null ? NumDefault : TypeConvertUtil.toInt(map.get("status").toString()));
        this.setfStatus(map.get("f_status") == null ? NumDefault : TypeConvertUtil.toInt(map.get("f_status").toString()));
        this.setTime(map.get("time") == null ? NumDefault : TypeConvertUtil.toLong(map.get("time").toString()));
        this.setType(type);
    }

    //私聊列表
    public BaseMessage(long id, String userID, String infoJson, int type, int kfID,
                       int status, int ru, long time, String content, int num) {
        this.setId(id);
        this.setWhisperID(userID);
        this.setInfoJson(infoJson);
        this.setType(type);
        paseInfoJson(this.getInfoJson());
        this.setKfID(kfID);
        this.setStatus(status);
        this.setRu(ru);
        this.setTime(time);
        this.setNum(num);
        this.setJsonStr(content);
    }

    //私聊列表
    public BaseMessage(int type, Map<String, Object> map) {
        this.setId(Long.parseLong(map.get("id").toString()));
        this.setWhisperID(map.get("userid") == null ? StrDefault : map.get("userid").toString());
        this.setIsOnline(map.get("isOnline") == null ? NumDefault : TypeConvertUtil.toInt(map.get("isOnline").toString()));
        this.setKfID(map.get("kf_id") == null ? NumDefault : TypeConvertUtil.toInt(map.get("kf_id").toString()));
        this.setInfoJson(map.get("infoJson") == null ? StrDefault : map.get("infoJson").toString());
        paseInfoJson(this.getInfoJson());
        this.setTime(map.get("time") == null ? NumDefault : TypeConvertUtil.toLong(map.get("time").toString()));
        this.setStatus(map.get("status") == null ? NumDefault : TypeConvertUtil.toInt(map.get("status").toString()));

        this.setNum(map.get("num") == null ? NumDefault : TypeConvertUtil.toInt(map.get("num").toString()));
        this.setType(type);
    }

    /**
     * 解析
     *
     * @param jsonStr
     */
    private void paseInfoJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) return;

        JSONObject object = getJsonObject(jsonStr);
        this.setAvatar(object.optString("avatar"));
        this.setName(object.optString("nickname"));
        this.setIsMonth(object.optInt("ismonth"));
        this.setIsVip(object.optInt("isVip"));
    }


    public static List<BaseMessage> conversionListMsg(List<BaseMessage> cMessages) {
        List<BaseMessage> baseMessages = new ArrayList<BaseMessage>();
        if (cMessages == null || cMessages.size() <= 0) {
            return baseMessages;
        }

        for (BaseMessage tmp : cMessages) {
            BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(tmp.getType());
            if (messageType == null) {
                baseMessages.add(tmp);
                continue;
            }
            switch (messageType) {
                case hi:
                    baseMessages.add(new TextMessage(tmp));
                    break;
                case common:
                    baseMessages.add(new CommonMessage(tmp));
                    break;
                case gift:
                case wantGiftTwo:
                    baseMessages.add(new GiftMessage(tmp));
                    break;
                default:
                    break;
            }
        }
        return baseMessages;
    }


    /**
     * 列表显示转换
     *
     * @param msg
     * @return
     */
    public static String getContent(BaseMessage msg) {
        String str = "";
        BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(msg.getType());
        if (messageType == null) {
            return str;
        }
        switch (messageType) {
            case hi:
                String content = msg.getMsgDesc();
                if (TextUtils.isEmpty(content)) {
                    str = "[打招呼]";
                } else {
                    str = content;
                }
                break;
            case common:
                str = msg.getMsgDesc();
                break;
            //case hint:
            case html://html消息
                str = msg.getMsgDesc();
                break;
            case gift:
            case wantGiftTwo:
                str = msg.getMsgDesc();
                break;
            default:
                break;
        }
        return str;
    }
}