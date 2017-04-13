package com.juxin.predestinate.module.logic.config;

/**
 * 应用常量
 * Created by ZRP on 2016/9/19.
 */
public class Constant {

    public static final String NO_HOST = "no_host";
    public static final String HOST_URL = "http://123.59.187.13:10001/";
    public static final String HOST_FILE_SERVER_URL = "http://test.image.xiaoyouapp.cn/"; // 文件上传地址

    // [以下3个参数对应文档](http://test.game.xiaoyouapp.cn:20080/juxin/api_doc/src/master/version/versions.md)
    public static final int MS = 9;                 //消息版本号
    public static final int PLATFORM_TYPE = 2;      //平台号
    public static final int SUB_VERSION = 3;        //客户端版本号

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
}
