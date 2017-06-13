package com.juxin.predestinate.ui.user.paygoods;

/**
 * 商品相关常亮
 * Created by Su on 2017/4/12.
 */
public class GoodsConstant {

    // ----- 支付渠道 ------
    public static final int PAY_TYPE_WECHAT = 6;  // 微信支付
    public static final int PAY_TYPE_ALIPAY = 1;  // 支付宝支付
    public static final int PAY_TYPE_OTHER = -1;   // 其他支付

    // ----- 支付类型 在线配置接口定义，不可随便更改------
    public static final String PAY_TYPE_WECHAT_NAME = "wechat";  // 微信支付
    public static final String PAY_TYPE_ALIPAY_NAME = "alipay";  // 支付宝支付
    public static final String PAY_TYPE_OTHER_NAME = "other";    // 其他支付

    public static final int PAY_TYPE_OLD = 0;     // 布局类型：老布局样式
    public static final int PAY_TYPE_NEW = 1;     // 布局类型：新布局样式

    // ----- 选择状态 ------
    public static final int PAY_STATUS_UNCHOOSE = 0; // 未选中支付
    public static final int PAY_STATUS_CHOOSE = 1;   // 选中支付

    // 钻石充值弹框
    public static final String DLG_TYPE_KEY = "typeKey";    // 弹框样式
    public static final String DLG_GIFT_NEED = "needKey";   // 钻石不足数目
    public static final int DLG_DIAMOND_NORMAL = 0;         // 默认弹框样式
    public static final int DLG_DIAMOND_GIFT_SHORT = 1;     // 送礼钻石不足弹框
    public static final String DLG_OPEN_FROM = "frome_tag"; //打开弹框的来源（统计用 可选）
    public static final String DLG_OPEN_TOUID = "frome_touid"; //是否因为某个用户充值 （统计用 可选）
    public static final String DLG_OPEN_CHANNEL_UID = "channel_uid"; //是否因为某个用户充值渠道uid （统计用 可选）


    // vip弹框类型
    public static final String DLG_VIP_TYPE = "vip_type";   // vip key
    public static final int DLG_VIP_PRIVEDEG = 1;           // 开通VIP特权
    public static final int DLG_VIP_LOW_POWER = 2;          // 体力不足
    public static final int DLG_VIP_LOW_WAKAN = 3;          // 灵力不足
    public static final int DLG_VIP_GET_DOG = 4;            // 获取斗牛犬
    public static final int DLG_VIP_HEIGH_AREANA = 5;       // 高级擂台

    // Y币
    public static final int DLG_YCOIN_NEW = 6;                // 新购买Y币弹框
    public static final int DLG_YCOIN_PRIVEDEG = 7;           // 购买Y币
    public static final String DLG_YCOIN_REMAIN = "remain";   // Y币余额
    public static final String DLG_YCOIN_POWER = "buy_power"; // 购买体力

    // 送礼钻石弹框
    public static final int DLG_DIAMOND_GIFT = 8;              // 充值钻石
}
