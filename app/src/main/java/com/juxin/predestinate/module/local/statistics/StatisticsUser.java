package com.juxin.predestinate.module.local.statistics;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心、个人资料统计项
 * Created by ZRP on 2017/6/10.
 */
public class StatisticsUser {

    /**
     * 用户资料->照片(传递to_uid,第几张照片,照片ID,是否成功查看)
     *
     * @param to_uid        被查看相册的用户uid
     * @param picture       查看的照片
     * @param picture_index 被查看的照片位置,从左往右排序,从1开始
     * @param success       是否成功打开照片
     */
    public static void userAlbum(long to_uid, String picture, int picture_index, boolean success) {
        Map<String, Object> params = new HashMap<>();
        params.put("picture", picture);
        params.put("picture_index", picture_index);
        params.put("success", success);
        Statistics.userBehavior(SendPoint.userinfo_album, to_uid, params);
    }

    /**
     * 用户资料->照片->翻相册
     *
     * @param to_uid  被查看相册的用户uid
     * @param picture 查看的照片
     * @param isRight 是否为向右滑动相册
     */
    public static void userAlbumFlip(long to_uid, String picture, boolean isRight) {
        Map<String, Object> params = new HashMap<>();
        params.put("picture", picture);
        Statistics.userBehavior(isRight ? SendPoint.userinfo_navalbum_rightflip
                : SendPoint.userinfo_navalbum_leftflip, to_uid, params);
    }

    /**
     * 用户资料->更多->备注名
     *
     * @param to_uid 被备注的用户uid
     * @param remark 备注名
     */
    public static void userRemark(long to_uid, String remark) {
        Map<String, Object> params = new HashMap<>();
        params.put("remark", remark);
        Statistics.userBehavior(SendPoint.userinfo_more_setting_remark, to_uid, params);
    }

    /**
     * 用户资料->更多->举报->提交按钮
     *
     * @param to_uid  被举报的用户uid
     * @param type    举报类型
     * @param content 举报内容
     */
    public static void userReport(long to_uid, String type, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("content", content);
        Statistics.userBehavior(SendPoint.userinfo_more_setting_jubao_submit, to_uid, params);
    }

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

    /**
     * 我的认证->身份认证->提交按钮(上传资料信息)
     *
     * @param name //姓名
     * @param id_number 身份证号
     * @param pay_type 支付方式(zhifubao/bank)
     * @param bank_kaihuhang 开户行
     * @param bank_zhihang 开户支行
     * @param bank_cardid 银行卡号
     * @param zhifubao_account 支付宝账号
     * @param pic_idnumber_positive 身份证正面图片URL
     * @param pic_idnumber_contrary 身份证正反面图片URL
     * @param pic_idnumberinhand 手持身份证照片
     */
    public static void meauthIdSubmit(String name, String id_number, int pay_type, String bank_kaihuhang, String bank_zhihang, String bank_cardid, String zhifubao_account, String pic_idnumber_positive, String pic_idnumber_contrary, String pic_idnumberinhand) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("id_number", id_number);
        params.put("pay_type", pay_type);
        params.put("bank_kaihuhang", bank_kaihuhang);
        params.put("bank_zhihang", bank_zhihang);
        params.put("bank_cardid", bank_cardid);
        params.put("zhifubao_account", zhifubao_account);
        params.put("pic_idnumber_positive", pic_idnumber_positive);
        params.put("pic_idnumber_contrary", pic_idnumber_contrary);
        params.put("pic_idnumberinhand", pic_idnumberinhand);
        Statistics.userBehavior(SendPoint.menu_me_meauth_id_submit, params);
    }

    /**
     * 登录页->登录按钮(无需检测登录成功状态,点击按钮一次上报一次日志)
     *
     * @param username 用户账号,取文本框输入内容
     * @param password 账号密码,取文本框输入内容
     */
    public static void userLogin(String username, String password) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("msg", "");//登录状态消息(可选)
        Statistics.userBehavior(SendPoint.login_btnlogin, params);
    }

    /**
     * 用户注册->注册按钮(无需检测注册成功状态,点击按钮记录一次日志)
     *
     * @param nick   昵称
     * @param age    年龄
     * @param gender 性别
     */
    public static void userRegister(String nick, int age, int gender) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("nick", nick);
        params.put("age", age);
        params.put("sex", gender == 1 ? "男" : "女");
        params.put("msg", "");//登录状态消息(可选)
        Statistics.userBehavior(SendPoint.regist_btnreg, params);
    }

    // 登录后引导->一键打招呼,登录后引导(男用户)
    public static void dailyOneKeySayHi(List<UserInfoLightweight> data) {
        List<Long> uids = new ArrayList<>();
        for (UserInfoLightweight lightInfo : data) {
            uids.add(lightInfo.getUid());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid", uids.toArray(new Long[uids.size()]));
        Statistics.userBehavior(SendPoint.login_guide_onekeysayhello, params);
    }
}
