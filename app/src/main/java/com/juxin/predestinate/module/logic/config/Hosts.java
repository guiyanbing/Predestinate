package com.juxin.predestinate.module.logic.config;

/**
 * 地址配置
 * Created by ZRP on 2017/5/15.
 */
public class Hosts {

    // --------------------逻辑服务器地址 start-----------------------
    public static String NO_HOST = "no_host";

    public static int SERVER_TYPE = 1;//0-正式服，1-测试服，对应以上几个host-array的position

    private static final String[] PHP_HOST = {"http://api2.app.yuanfenba.net/", "http://test.api2.app.yuanfenba.net/"};
    private static final String[] GO_HOST = {"http://g.api.yuanfenba.net/", "http://123.59.187.33:8681/"};
    private static final String[] UPLOAD_HOST = {"http://upload.img.yuanfenba.net/", "http://test.upload.img.yuanfenba.net/"};
    private static final String[] PAY_HOST = {"http://p.app.yuanfenba.net/", "http://test.p.app.yuanfenba.net/"};
    private static final String[] CUP_HOST = {"http://pay.app.mumu123.cn/"};

    public static final String FATE_IT_HTTP = PHP_HOST[SERVER_TYPE];          //php地址
    public static final String FATE_IT_GO = GO_HOST[SERVER_TYPE];             //go地址
    public static final String FATE_IT_HTTP_PIC = UPLOAD_HOST[SERVER_TYPE];   //图片地址
    public static final String FATE_IT_PROTOCOL = PAY_HOST[SERVER_TYPE];      //支付地址
    public static final String FATE_IT_CUP_HTTP = CUP_HOST[0];                //银联地址

    public static final String HOST_URL = FATE_IT_HTTP;                       //默认host地址
    // -------------------逻辑服务器地址 end------------------------

    // ---------------------H5 assets路径 start--------------------
    private static final String H5_HOST = "file:///android_asset/webApp/YfbWebApp/pages/";
    public static final String H5_RANKING = H5_HOST + "windRanking/windRanking.html";  // 排行榜
    public static final String H5_ACTION = H5_HOST + "setting/activity.html";          // 活动相关
    public static final String H5_PREPAID_COIN = H5_HOST + "prepaid/prepaid.html";     // 购买Y币
    public static final String H5_PREPAID_VIP = H5_HOST + "prepaid/prepaid-vip.html";  // 购买VIP
    public static final String H5_GIFT = H5_HOST + "myGift/myGift.html";               // 我的礼物
    // ---------------------H5 assets路径 end--------------------
}