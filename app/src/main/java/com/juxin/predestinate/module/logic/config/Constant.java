package com.juxin.predestinate.module.logic.config;

/**
 * 应用常量
 * Created by ZRP on 2016/9/19.
 */
public class Constant {

    public static final String NO_HOST = "no_host";
    public static final String HOST_FILE_SERVER_URL = "http://test.image.xiaoyouapp.cn/"; // 文件上传地址

    // -------------------------正式服地址---------------------------
//    public static final String FATE_IT_HTTP = "http://api2.app.yuanfenba.net/";         //php正式服地址
//    public static final String FATE_IT_GO = "http://g.api.yuanfenba.net/";              //go正式服务器地址
//    public static final String FATE_IT_HTTP_PIC = "http://upload.img.yuanfenba.net/";   //图片上传正式服地址
//    public static final String FATE_IT_PROTOCOL = "http://p.app.yuanfenba.net/";        //支付正式服地址

    // -------------------------测试服地址---------------------------
    public static final String FATE_IT_HTTP = "http://test.api2.app.yuanfenba.net/";        //php测试服地址
    public static final String FATE_IT_GO = "http://123.59.187.33:8681/";                   //go测试服务器地址
    public static final String FATE_IT_HTTP_PIC = "http://test.upload.img.yuanfenba.net/";  //图片测试正式服地址
    public static final String FATE_IT_PROTOCOL = "http://test.p.app.yuanfenba.net/";       //支付测试服地址

    // ---------------------服务器默认地址配置 end-----------------------
    public static final String FATE_IT_CUP_HTTP = " http://pay.app.mumu123.cn/";        //银联地址
    public static final String HOST_URL = FATE_IT_HTTP;


    // -------------------------版本信息常量---------------------------

    // [以下3个参数对应文档](http://test.game.xiaoyouapp.cn:20080/juxin/api_doc/src/master/version/versions.md)
    public static final int MS_TYPE = 11;           //消息版本号
    public static final int PLATFORM_TYPE = 2;      //平台号：2为android，3为iphone
    public static final int SUB_VERSION = 7;        //客户端版本号
    public static final int REG_FLAG = 0;           //0缘分吧，1爱爱，2同城快约，3附近秘约，标记

    // 友盟
    public static final String UMENG_APPKEY = "549fccadfd98c51fae00019a";

    //微信
    public static String WEIXIN_APP_ID = "wxab302910f007a8ed";//聚鑫 //"wxc56bbddc3c2e0c18";//APPID
    public final static String WEIXIN_App_Key = "xCNyzKceB8szwWmUqT4laGqK5SapQn5L";

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

    public static final String ASET_DATA = "getASet_Flag";
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

    public static final long CHAT_SHOW_TIP_TIME_Interval = 20 * 60 * 1000;  //Chat相关
    public static final int CHAT_TEXT_LIMIT = 3478;//Chat相关

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

    // 摇钱树游戏逻辑接口地址
    public static final String FATE_GAME_LOGIC_BASE = "http://redbag2game.xiaoyaoai.cn/";
    // 游戏服务地址
    public static final String FATE_GAME_DOMAIN = "http://page.xiaoyaoai.cn/";
    // 切水果音乐资源根路径
    public static final String FATE_GAME_BASE = FATE_GAME_DOMAIN + "cutfruit/";
    // 切水果音乐文件配置
    public static final String FATE_GAME_SOUNDS = FATE_GAME_BASE + "assets/cutfruit/config/sounds.json";
    // 切水果擂台页
    public static final String FATE_REDBOX_RANKLIST = FATE_GAME_BASE + "fruit_ranklist.html";
    // 切水果说明页
    public static final String FATE_GAME_EXPLAIN = FATE_GAME_BASE + "play_help.html";
    // 摇钱树游戏页
    public static final String FATE_GAME_CASHCOW = FATE_GAME_DOMAIN + "cashcow/cashcow.html";
    // 摇钱树音乐文件配置
    public static final String FATE_GAME_CASH_COW_SOUNDS = FATE_GAME_DOMAIN + "cashcow/assets/cashcowres/configs/sounds.json";
    // 摇钱树音乐资源根路径
    public static final String FATE_GAME_CASH_COW_BASE = FATE_GAME_DOMAIN + "cashcow/";
    //摇钱树版本：玩家购买，使用的时候须进行拼接AppCfg.ASet.getCashcow_logic_url() + AppCfg.FATE_PLAYERS_SHOPPING
    public static final String FATE_PLAYERS_SHOPPING = "players/shopping";

    public static String U_IS_VIDEO_AUTH;
    public static String U_IS_VIDEO_IMG_URL;
    public static String U_IS_VIDEO_VIDEO_URL;


    //============= 打招呼类型 start =============\\
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

    //============= 打招呼类型 end =============\\

}
