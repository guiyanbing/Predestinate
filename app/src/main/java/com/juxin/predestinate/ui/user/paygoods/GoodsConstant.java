package com.juxin.predestinate.ui.user.paygoods;

/**
 * 商品相关常亮
 * Created by Su on 2017/4/12.
 */
public class GoodsConstant {

    // ----- 支付渠道 ------
    public static final int PAY_TYPE_WECHAT = 0;  // 微信支付
    public static final int PAY_TYPE_ALIPAY = 1;  // 支付宝支付
    public static final int PAY_TYPE_OTHER = 2;   // 其他支付

    // ----- 选择状态 ------
    public static final int PAY_STATUS_UNCHOOSE = 0; // 未选中支付
    public static final int PAY_STATUS_CHOOSE = 1;   // 选中支付

    // vip弹框类型
    public static final String DLG_VIP_TYPE = "vip_type";   // vip key
    public static final int DLG_VIP_PRIVEDEG = 1;           // 开通VIP特权
    public static final int DLG_VIP_LOW_POWER = 2;          // 体力不足
    public static final int DLG_VIP_LOW_WAKAN = 3;          // 灵力不足
    public static final int DLG_VIP_GET_DOG = 4;            // 获取斗牛犬
    public static final int DLG_VIP_HEIGH_AREANA = 5;       // 高级擂台
}
