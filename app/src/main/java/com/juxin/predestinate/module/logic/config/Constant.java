package com.juxin.predestinate.module.logic.config;

/**
 * 应用常量
 * Created by ZRP on 2016/9/19.
 */
public class Constant {

    public static final String NO_HOST = "no_host";
    //    public static final String HOST_URL = "http://api2.app.yuanfenba.net/";
    public static final String HOST_URL = "http://test.api2.app.yuanfenba.net/";
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

    public static final String FATE_IT_CUP_HTTP = " http://pay.app.mumu123.cn/";        //银联地址



    // -------------------------版本信息常量---------------------------

    // [以下3个参数对应文档](http://test.game.xiaoyouapp.cn:20080/juxin/api_doc/src/master/version/versions.md)
    public static final int MS = 9;                 //消息版本号
    public static final int PLATFORM_TYPE = 2;      //平台号
    public static final int SUB_VERSION = 5;        //客户端版本号

    //微信
    public static String WEIXIN_APP_ID = "wxab302910f007a8ed";//聚鑫 //"wxc56bbddc3c2e0c18";//APPID
    public final static String WEIXIN_App_Key = "xCNyzKceB8szwWmUqT4laGqK5SapQn5L";

    // -------------------------K-V---------------------------

    public static final String IS_SHOW_MESSAGE = "is_show_message";             // 是否显示过通知栏
    public static final String SETTING_QUIT_MESSAGE = "setting_quit_message";   //是否进行锁屏弹窗，存储key及默认值
    public static final Boolean SETTING_QUIT_MESSAGE_DEFAULT = true;
    public static final String SETTING_MESSAGE = "setting_message";             //是否进行消息提示，存储key及默认值
    public static final Boolean SETTING_MESSAGE_DEFAULT = true;
    public static final String SETTING_VIBRATION = "setting_vibration";         //是否进行新消息震动提示，存储key及默认值
    public static final Boolean SETTING_VIBRATION_DEFAULT = true;
    public static final String SETTING_VOICE = "setting_voice";                 //是否进行新消息声音提示，存储key及默认值
    public static final Boolean SETTING_VOICE_DEFAULT = true;
    public static final String SETTING_SLEEP_MESSAGE = "setting_sleep_message"; //是否睡眠免打扰，存储key及默认值
    public static final Boolean SETTING_SLEEP_MESSAGE_DEFAULT = true;

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

    /*注册参数*/
    public static int msType = 7;  //消息版本号(MS) Y币版本：5，缘分吧红包版：7，红包来了：9，消息排队版，自动回复 + 升级关系（测试版）：10，视频通话 + 表情：11
    //客户端版本号（version） 礼物版：1，红包版：2，语音版：3，消息排队 + 私密视频：4，取消排队：5，自动回复 + 升级关系（测试版）：6，视频通话 + 表情：7
    public static int VERSION = 5;
    public static int regFlag = 0;// 0缘分吧 1爱爱 2同城快约 3附近秘约 标记
    public static int clientType = 2;// 2为android 3为iphone

}
