package com.juxin.predestinate.module.logic.config;

/**
 * 应用常量
 * Created by ZRP on 2016/9/19.
 */
public class Constant {

    // -------------------------版本信息常量---------------------------

    // [以下3个参数对应文档](http://test.game.xiaoyouapp.cn:20080/juxin/api_doc/src/master/version/versions.md)
    public static final int MS_TYPE = 20;           //消息版本号
    public static final int SUB_VERSION = 20;       //客户端版本号
    public static final int PLATFORM_TYPE = 2;      //平台号：2为android，3为iphone
    public static final int REG_FLAG = 0;           //0缘分吧，1爱爱，2同城快约，3附近秘约，标记

    // 友盟
    public static final String UMENG_APPKEY = "549fccadfd98c51fae00019a";

    //微信
    public static String WEIXIN_APP_ID = "wxab302910f007a8ed";//聚鑫 //"wxc56bbddc3c2e0c18";//APPID
    public final static String WEIXIN_App_Key = "xCNyzKceB8szwWmUqT4laGqK5SapQn5L";

    public static final int APPEAR_TYPE_NO = 0;//默认没有选择
    public static final int APPEAR_TYPE_OWN = 1;//自己露脸
    public static final int APPEAR_TYPE_NO_OWN = 2;//自己不露脸

    // -----------------------私有K-V-----------------------
    public static final String PRIVATE_CHAT_TOP_H = "private_chat_top_h"; //私聊页顶部三个高度
    public static final String APPEAR_FOREVER_TYPE = "appear_forever_type"; //默认出场方式
    public static final String FLIP_ALBUM_UID = "flip_album_uid";//被查看的相册用户id

    // -------------------------K-V---------------------------
    public static final String IS_SHOW_MESSAGE = "is_show_message";             // 是否显示过通知栏
    public static final String SETTING_QUIT_MESSAGE = "setting_quit_message";   //是否进行锁屏弹窗，存储key及默认值
    public static final boolean SETTING_QUIT_MESSAGE_DEFAULT = true;
    public static final String SETTING_MESSAGE = "setting_message";             //是否进行消息提示，存储key及默认值
    public static final boolean SETTING_MESSAGE_DEFAULT = true;
    public static final String SETTING_VIBRATION = "setting_vibration";         //是否进行新消息震动提示，存储key及默认值
    public static final boolean SETTING_VIBRATION_DEFAULT = true;
    public static final String SETTING_VOICE = "setting_voice";                 //是否进行新消息声音提示，存储key及默认值
    public static final boolean SETTING_VOICE_DEFAULT = true;
    public static final String SETTING_SLEEP_MESSAGE = "setting_sleep_message"; //是否睡眠免打扰，存储key及默认值
    public static final boolean SETTING_SLEEP_MESSAGE_DEFAULT = true;

    public static final String SETTING_VIDEO_CHAT = "setting_video_chat";
    public static final boolean SETTING_VIDEO_CHAT_DEFAULT = true;
    public static final String SETTING_AUDIO_CHAT = "setting_audio_chat";
    public static final boolean SETTING_AUDIO_CHAT_DEFAULT = true;

    // 图片
    public static final int INT_AVATAR = 101;       // 头像
    public static final int INT_ALBUM = 102;        // 相册
    public static final int INT_CHAT_PIC = 103;     // 聊天图片
    public static final int INT_PRAISE_PIC = 104;   // 集赞图片: 暂时替代动态图片和身份验证

    // 语音
    public static final int INT_CHAT_VOICE = 201;   // 聊天语音
    public static final int INT_PRAISE_VOICE = 202; // 集赞语音
    public static final int INT_GAME_VOICE = 203;   // 游戏语音

    // 视频
    public static final int INT_CHAT_VIDEO = 301;   // 聊天小视频

    public static final long CHAT_RESEND_TIME = 5 * 60 * 1000;              //5分钟内重发消息
    public static final long CHAT_SHOW_TIP_TIME_Interval = 20 * 60 * 1000;  //Chat相关
    public static final int CHAT_TEXT_LIMIT = 3478;//Chat相关
    public static final long TWO_HOUR_TIME = 2 * 60 * 60 * 1000;              //2小时

    // ------ 文件长存储/短存储 start --------
    public static final String STR_SHORT_TAG = "oss";    // 短存储图片截取标志
    public static final String STR_LONG_TAG = "jxfile";  // 长存储图片截取标志

    public static final String SINCE_GROWTHID = "since_growthid"; //消息自增长ID
    public static final long SINCE_GROWTHID_DEFAULT = -1;         // 默认值

    // 老缘分吧上传文件类型
    public static final String UPLOAD_TYPE_FACE = "face";           // 自定义表情
    public static final String UPLOAD_TYPE_VOICE = "voice";         // 语音
    public static final String UPLOAD_TYPE_PHOTO = "photo";         // 图片
    public static final String UPLOAD_TYPE_VIDEO_CHAT = "videochat";// 头像认证
    public static final String UPLOAD_TYPE_VIDEO = "video";         // 视频

    // 支付类型
    public static final int GOOD_DIAMOND = 1;    //钻石
    public static final int GOOD_VIP = 2;        //VIP服务
    public static final int REQ_PAYLISTACT = 0x15;       //支付请求码
    public static final int PAY_WEIXIN = 0x16;// 微信支付
    public static final int PAYMENTACT = 0x17;
    public static final int PAY_VOICE_OK = 0x18; // 银联语音支付
    public static final int PAY_VOICE_DETAIL = 0x19; // 银联语音支付
    public static final int PAY_VOICEACT = 0x20;// 银联语音支付
    public static final int PAYMENTACT_TO = 0x21;// 跳转支付宝网页
    public static final int PAY_ALIPAY_WEB_ACT = 0x22;// 支付宝网页支付

    public static String U_IS_VIDEO_AUTH;
    public static String U_IS_VIDEO_IMG_URL;
    public static String U_IS_VIDEO_VIDEO_URL;

    //============= 打招呼类型 start =============
    /**
     * 0为普通
     */
    public static final int SAY_HELLO_TYPE_SIMPLE = 0;
    /**
     * 1为向机器人一键打招呼
     */
    public static final int SAY_HELLO_TYPE_ROBOT = 1;
    /**
     * 3附近的人群打招呼
     */
    public static final int SAY_HELLO_TYPE_NEAR = 3;
    /**
     * 4为向机器人单点打招呼(包括首页和详细资料页等)
     */
    public static final int SAY_HELLO_TYPE_ONLY = 4;
    //============= 打招呼类型 end =============

    //===================打开来源 ===========
    public static final int OPEN_FROM_HOT = 1;         //热门打开送礼物
    public static final int OPEN_FROM_CHAT_FRAME = 2;  //私聊页打开送礼物
    public static final int OPEN_FROM_INFO = 3;        //个人资料页打开送礼物
    public static final int OPEN_FROM_CHAT_PLUGIN = 4; //音视频时钻石不足弹窗

    /**
     * 退出登录resultcode
     */
    public static final int EXITLOGIN_RESULTCODE = 200;

}
