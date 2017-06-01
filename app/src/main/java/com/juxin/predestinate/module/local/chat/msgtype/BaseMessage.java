package com.juxin.predestinate.module.local.chat.msgtype;

import android.os.Bundle;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.predestinate.bean.db.FLetter;
import com.juxin.predestinate.bean.db.FMessage;
import com.juxin.predestinate.module.local.chat.inter.IBaseMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.local.chat.utils.MsgIDUtils;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatPanelType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.ui.mail.item.MailItemType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * [消息类型处理](http://doc.dev.yuanfenba.net/pkg/yuanfen/common/msg_data/)
 * Created by Kind on 2017/3/17.
 */
public class BaseMessage implements IBaseMessage {

    public enum BaseMessageType {

        common(CommonMessage.class, 2),//文本消息
        hi(CommonMessage.class, 3),//打招呼
        sys(SystemMessage.class, 7),//系统
        gift(GiftMessage.class, 10),//礼物消息
        hint(TextMessage.class, 14),//小提示消息
        html(TextMessage.class, 19),//html消息
        wantGift(GiftMessage.class, 20),//索要礼物消息
        video(VideoMessage.class, 24),//视频消息
        htmlText(TextMessage.class, 25),//HTML文本消息
        ;

        public Class<? extends BaseMessage> msgClass = null;
        public int msgType;

        BaseMessageType(Class<? extends BaseMessage> msgClass, int msgType) {
            this.msgClass = msgClass;
            this.msgType = msgType;
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

    /**
     * 消息类型，进行未读消息比对
     */
    public static final int Follow_MsgType = 5;//关注
    public static final int System_MsgType = 7;//系统消息
    public static final int TalkRed_MsgType = 12;//聊天红包
    public static final int RedEnvelopesBalance_MsgType = 17;//红包余额变动消息
    public static final int video_MsgType = 24;//视频消息

    @Override
    public BaseMessage parseJson(String jsonStr) {
        PLogger.d(jsonStr);
        this.setJsonStr(jsonStr);
        return this;
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
    private long ru = MessageConstant.Ru_Friend;//如果为1则为熟人消息，否则为0

    private boolean isRead = false;//未读消息（true已经是读过了）//这个字段专门给数据库用的，不是给界面用的
    private boolean isSave;//是否保存
    private boolean isAutoplay = false;//是否自动播放

    //这几个字段 目前基本是给私聊用的
    private String infoJson;//个人资料json
    private String name;// 名称
    private String avatar;// 头像
    private int localAvatar;// 本地头像
    private int top;
    private String aboutme;// 内心读白
    private int isVip;
    private int kfID;//是否是机器人
    private int num;//私聊列表专用字段
    private int Weight = MessageConstant.Small_Weight;//Item的权重
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

    public int getTop() {
        return top;
    }

    public boolean isTop() {
        return top != 0;
    }

    public void setTop(int top) {
        this.top = top;
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
        return ChatPanelType.CPT_Custom == msgPanelType;
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

    // true 如果为1则为熟人消息，否则为0
    public boolean isRu() {
        return ru == MessageConstant.Ru_Friend;
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
    public BaseMessage(Bundle bundle) {
        this.setId(bundle.getLong(FMessage._ID));
        this.setChannelID(bundle.getString(FMessage.COLUMN_CHANNELID));
        this.setWhisperID(bundle.getString(FMessage.COLUMN_WHISPERID));
        this.setSendID(bundle.getLong(FMessage.COLUMN_SENDID));
        this.setMsgID(bundle.getLong(FMessage.COLUMN_MSGID));
        this.setcMsgID(bundle.getLong(FMessage.COLUMN_CMSGID));
        this.setSpecialMsgID(bundle.getLong(FMessage.COLUMN_SPECIALMSGID));
        this.setType(bundle.getInt(FMessage.COLUMN_TYPE));
        this.setStatus(bundle.getInt(FMessage.COLUMN_STATUS));
        this.setfStatus(bundle.getInt(FMessage.COLUMN_FSTATUS));
        this.setTime(bundle.getLong(FMessage.COLUMN_TIME));
        this.setJsonStr(bundle.getString(FMessage.COLUMN_CONTENT));
    }

    //私聊列表
    public BaseMessage(Bundle bundle, boolean fletter) {
        this.setId(bundle.getLong(FLetter._ID));
        this.setWhisperID(bundle.getString(FLetter.COLUMN_USERID));
        this.setInfoJson(bundle.getString(FLetter.COLUMN_INFOJSON));
        this.setType(bundle.getInt(FLetter.COLUMN_TYPE));
        parseInfoJson(this.getInfoJson());
        this.setKfID(bundle.getInt(FLetter.COLUMN_KFID));
        this.setStatus(bundle.getInt(FLetter.COLUMN_STATUS));
        this.setRu(bundle.getInt(FLetter.COLUMN_RU));
        this.setTime(bundle.getLong(FLetter.COLUMN_TIME));
        this.setJsonStr(bundle.getString(FLetter.COLUMN_CONTENT));
        this.setNum(bundle.getInt(FLetter.Num));
    }

    /**
     * 转换JSON 转子类的时候用
     *
     * @param jsonStr
     */
    public void convertJSON(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) return;
        this.setJsonStr(jsonStr);
    }

    /**
     * 解析
     *
     * @param jsonStr
     */
    private void parseInfoJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) return;
        PLogger.printObject("jsonStr=" + jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setAvatar(object.optString("avatar"));
        this.setName(TextUtils.isEmpty(object.optString("remark")) ? object.optString("nickname") : object.optString("remark"));
        this.setIsVip(object.optInt("group"));
        this.setTop(object.optInt("top"));
    }

    /**
     * 私聊列表
     */
    public static BaseMessage parseToLetterMessage(Bundle bundle) {
        BaseMessage message = new BaseMessage();
        if (bundle == null) {
            return message;
        }
        BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(bundle.getInt(FLetter.COLUMN_TYPE));
        if (messageType == null) {
            message = new BaseMessage(bundle, true);
            return message;
        }
        switch (messageType) {
            case hi:
            case html:
            case hint:
            case htmlText:
                message = new TextMessage(bundle, true);
                break;
            case common:
                message = new CommonMessage(bundle, true);
                break;
            case gift:
            case wantGift:
                message = new GiftMessage(bundle, true);
                break;
            case sys:
                message = new SystemMessage(bundle, true);
                break;
            case video:
                message = new VideoMessage(bundle, true);
                break;
            default:
                message = new BaseMessage(bundle, true);
                break;
        }
        return message;
    }

    //内容表
    public static BaseMessage parseToBaseMessage(Bundle bundle) {
        BaseMessage message = new BaseMessage();
        if (bundle == null) {
            return message;
        }

        BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(bundle.getInt(FMessage.COLUMN_TYPE));
        if (messageType == null) {
            message = new BaseMessage(bundle);
            return message;
        }
        switch (messageType) {
            case hi:
            case html:
            case hint:
            case htmlText:
                message = new TextMessage(bundle);
                break;
            case common:
                message = new CommonMessage(bundle);
                break;
            case gift:
            case wantGift:
                message = new GiftMessage(bundle);
                break;
            case sys:
                message = new SystemMessage(bundle);
                break;
            case video:
                message = new VideoMessage(bundle);
                break;
            default:
                message = new BaseMessage(bundle);
                break;
        }
        return message;
    }

    /**
     * 列表显示转换
     *
     * @param msg
     * @return
     */
    public static String getContent(BaseMessage msg) {
        String result = "";
        BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(msg.getType());
        if (messageType == null) {
            return result;
        }
        switch (messageType) {
            case hi:
                String content = msg.getMsgDesc();
                if (TextUtils.isEmpty(content)) {
                    result = "[打招呼]";
                } else {
                    result = content;
                }
                break;
            case common:
                result = msg.getMsgDesc();
                if (TextUtils.isEmpty(result)) {
                    CommonMessage commonMessage = (CommonMessage) msg;

                    String videoUrl = commonMessage.getVideoUrl();
                    String localVideoUrl = commonMessage.getLocalVideoUrl();
                    String voiceUrl = commonMessage.getVoiceUrl();
                    String localVoiceUrl = commonMessage.getLocalVoiceUrl();
                    String img = commonMessage.getImg();
                    String localImg = commonMessage.getLocalImg();
                    if (!TextUtils.isEmpty(videoUrl) || !TextUtils.isEmpty(localVideoUrl)) {//视频
                        result = "[视频消息]";
                    } else if (!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)) {//语音
                        result = "[语音消息]";
                    } else if (!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)) {//图片
                        result = "[图片消息]";
                    }
                }
                break;
            case video: {
                VideoMessage videoMessage = (VideoMessage) msg;
                result = VideoMessage.transLastStatusText(videoMessage.getEmLastStatus(),
                        TimeUtil.getFormatTimeChatTip(TimeUtil.onPad(videoMessage.getTime())), videoMessage.isSender());
                break;
            }
            case hint:
            case html://html消息
            case htmlText:
            case gift:
            case wantGift:
                result = msg.getMsgDesc();
                break;
            case sys:
                String content1 = msg.getMsgDesc();
                if (TextUtils.isEmpty(content1)) {
                    result = "[已读]";
                } else {
                    result = content1;
                }
                break;
            default:
                result = msg.getMsgDesc();
                break;
        }
        return result;
    }
}