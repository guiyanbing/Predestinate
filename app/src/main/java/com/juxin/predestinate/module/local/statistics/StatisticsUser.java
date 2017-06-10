package com.juxin.predestinate.module.local.statistics;

import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.util.HashMap;

/**
 * 个人中心、个人资料统计项
 * Created by ZRP on 2017/6/10.
 */
public class StatisticsUser {

    /**
     * 手机验证统计
     *
     * @param phone    手机号输入框内容
     * @param code     验证码输入框内容
     * @param isCommit 是否是立即验证提交按钮
     */
    public static void userPhoneVerify(String phone, String code, boolean isCommit) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("tel", phone);//手机号文本框输入的内容
        params.put("verifycode", code);//短信验证码文本框的内容
        Statistics.userBehavior(isCommit ? SendPoint.menu_me_meauth_telauth_btnverify
                : SendPoint.menu_me_meauth_telauth_btnverifycode, params);
    }

    /**
     * 索要礼物统计
     *
     * @param msg    索要礼物文字内容或语音连接
     * @param giftId 礼物id
     */
    public static void userAskForGift(String msg, int giftId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("msg", msg);
        params.put("giftId", giftId);
        Statistics.userBehavior(SendPoint.menu_me_redpackage_sylw_send, params);
    }

    /**
     * 我的->我的钻石->点击钻石支付按钮
     *
     * @param gem_num 钻石数量按钮,只传钻石数量即可
     */
    public static void centerGemPay(int gem_num) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("gem_num", gem_num);
        Statistics.userBehavior(SendPoint.menu_me_gem_btnpay, params);
    }

    /**
     * 我的->我的相册(上传照片数量)
     */
    public static void centerAlbum() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("picture_number", ModuleMgr.getCenterMgr().getMyInfo().getUserPhotos().size());
        Statistics.userBehavior(SendPoint.menu_me_album, params);
    }
}
