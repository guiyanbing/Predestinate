package com.juxin.predestinate.module.logic.config;

/**
 * 地址配置
 * Created by ZRP on 2017/5/15.
 */
public class Hosts {

    // --------------------逻辑服务器地址 start-----------------------
    public static String NO_HOST = "no_host";

    public static int SERVER_TYPE = 1;//0-正式服，1-测试服，对应以上几个host-array的position

    private static final String[] TCP_HOST = {"sc.app.yuanfenba.net", "123.59.187.33"};
    private static final String[] PHP_HOST = {"http://api2.app.yuanfenba.net/", "http://test.api2.app.yuanfenba.net/"};
    private static final String[] GO_HOST = {"http://g.api.yuanfenba.net/", "http://123.59.187.33:8681/"};
    private static final String[] UPLOAD_HOST = {"http://upload.img.yuanfenba.net/", "http://test.upload.img.yuanfenba.net/"};
    private static final String[] PAY_HOST = {"http://p.app.yuanfenba.net/", "http://test.p.app.yuanfenba.net/"};
    private static final String[] CUP_HOST = {"http://pay.app.mumu123.cn/"};

    public static final String FATE_IT_TCP = TCP_HOST[SERVER_TYPE];         //socket地址
    public static final String FATE_IT_HTTP = PHP_HOST[SERVER_TYPE];        //php地址
    public static final String FATE_IT_GO = GO_HOST[SERVER_TYPE];           //go地址
    public static final String FATE_IT_HTTP_PIC = UPLOAD_HOST[SERVER_TYPE]; //图片地址
    public static final String FATE_IT_PROTOCOL = PAY_HOST[SERVER_TYPE];    //支付地址
    public static final String FATE_IT_CUP_HTTP = CUP_HOST[0];              //银联地址

    public static final String HOST_URL = FATE_IT_HTTP;                     //默认host地址
    // -------------------逻辑服务器地址 end------------------------

    // ---------------------H5 assets路径 start--------------------
    private static final String H5_HOST = "file:///android_asset/webApp/pages/";
    public static final String H5_RANKING = H5_HOST + "windRanking/windRanking.html";  // 排行榜
    public static final String H5_ACTION = H5_HOST + "setting/activity.html";          // 活动相关
    public static final String H5_PREPAID = H5_HOST + "prepaid/prepaid.html";          // 购买页面，根据type区分Y币和vip
    public static final String H5_GIFT = H5_HOST + "myGift/myGift.html";               // 我的礼物
    public static final String H5_BILL = H5_HOST + "setting/tollCollection.html";      // 话费领取
    public static final String H5_ROTARY = H5_HOST + "turntable/turntable.html";       // 大转盘设置
    public static final String H5_EARN_RED_BAG = H5_HOST + "earnRedBag/earn-red-bag.html";    //我要赚红包
    // ---------------------H5 assets路径 end--------------------

    // -----------------------备份地址 start---------------------------
    // 广场本地备份地址
    public static final String LOCAL_SQUARE_URL = "http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/square/square.html";
    // -----------------------备份地址 end---------------------------
}
