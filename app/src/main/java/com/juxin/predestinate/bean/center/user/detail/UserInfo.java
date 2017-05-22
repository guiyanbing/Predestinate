package com.juxin.predestinate.bean.center.user.detail;

import org.json.JSONObject;

/**
 * 用户基本信息
 */
public class UserInfo extends UserBasic {
    private String aboutme;         // 内心独白
    private String avatar_121;      // 头像小图 121*153
    private String avatar_146;      // 头像小图 146*185
    private int blood;              // 血型
    private String c_uid;           // -1机器人
    private boolean c_user;         // 自增用户
    private String complete;        // 资料完整度(%)
    private int diamand;            // 我的钻石
    private boolean isMonthMail;    // 是否开通包月发信
    private boolean isOnline;       // 是否在线
    private String idcard;          // 身份证号
    private int idcard_validation;  // 省份证验证
    private long memdatenum;        // 计算会员到期时间
    private String mobile;          // 手机号码
    private int mobileAuth;         // 1为公开，2为保密
    private int mobile_validation;  // 手机是否已验证
    private int photoNum;           // 相册的数目
    private String qq;              // QQ号码
    private int qqAuth;             // 1为公开，2为保密
    private double redbagsum;       // 红包总额
    private String shareCode;       // 邀请码
    private int videoAuth;          // 用户视频权限
    private String weChat;          // 微信号码
    private int wechatAuth;         // 1为公开, 2为保密
    private int ycoin = 0;          // Y币

    @Override
    public void parseJson(String s) {
        super.parseJson(s);
        JSONObject detailObject = getJsonObject(s);

        // A
        this.setAboutme(detailObject.optString("aboutme"));
        this.setAvatar_121(detailObject.optString("avatar_121"));
        this.setAvatar_146(detailObject.optString("avatar_146"));

        // B
        this.setBlood(detailObject.optInt("blood"));

        // C
        this.setC_uid(detailObject.optString("c_uid"));
        this.setC_user(detailObject.optBoolean("c_user"));
        this.setComplete(detailObject.optString("complete"));

        // D
        this.setDiamand(detailObject.optInt("diamand"));

        // I
        this.setMonthMail(detailObject.optBoolean("is_month_mail"));
        this.setOnline(detailObject.optBoolean("is_online"));
        this.setIdcard(detailObject.optString("Idcard"));
        this.setIdcard_validation(detailObject.optInt("Idcard_validation"));

        // M
        this.setMemdatenum(detailObject.optLong("memdatenum"));
        this.setMobile(detailObject.optString("mobile"));
        this.setMobileAuth(detailObject.optInt("mobile_auth"));
        this.setMobile_validation(detailObject.optInt("mobile_validation"));

        // P
        this.setPhotoNum(detailObject.optInt("photoNum"));

        // Q
        this.setQQ(detailObject.optString("qq"));
        this.setQQAuth(detailObject.optInt("qq_auth"));

        // R
        this.setRedbagsum(detailObject.optDouble("redbagsum"));

        // S
        this.setShareCode(detailObject.optString("shareCode"));

        // V
        this.setVideoAuth(detailObject.optInt("video_auth"));

        // W
        this.setWeChat(detailObject.optString("wechat"));
        this.setWechatAuth(detailObject.optInt("wechat_auth"));

        // Y
        this.setYcoin(detailObject.optInt("ycoin"));
    }

    /**
     * 判断是否是vip用户通过 'isMonthMail' 判断
     */
    public boolean isVip() {
        return isMonthMail;
    }

    /**
     * 是否绑定手机
     */
    public boolean isVerifyCellphone() {
        return mobile_validation != 0;
    }

    public void setVerifyCellphone(boolean verifyCellphone) {
        if (verifyCellphone) {
            mobile_validation = 1;
            return;
        }
        mobile_validation = 0;
    }

    public void setMobile_validation(int mobile_validation) {
        this.mobile_validation = mobile_validation;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public int getIdcard_validation() {
        return idcard_validation;
    }

    public void setIdcard_validation(int idcard_validation) {
        this.idcard_validation = idcard_validation;
    }

    public int getVideoAuth() {
        return videoAuth;
    }

    public void setVideoAuth(int videoAuth) {
        this.videoAuth = videoAuth;
    }

    public long getMemdatenum() {
        return memdatenum;
    }

    public void setMemdatenum(long memdatenum) {
        this.memdatenum = memdatenum;
    }

    public boolean isC_user() {
        return c_user;
    }

    public void setC_user(boolean c_user) {
        this.c_user = c_user;
    }

    public String getAvatar_121() {
        return avatar_121;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public void setAvatar_121(String avatar_121) {
        this.avatar_121 = avatar_121;
    }

    public String getAvatar_146() {
        return avatar_146;
    }

    public void setAvatar_146(String avatar_146) {
        this.avatar_146 = avatar_146;
    }

    public int getDiamand() {
        return diamand;
    }

    public void setDiamand(int diamand) {
        this.diamand = diamand;
    }

    public double getRedbagsum() {
        return redbagsum;
    }

    public void setRedbagsum(double redbagsum) {
        this.redbagsum = redbagsum;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getC_uid() {
        return c_uid;
    }

    public void setC_uid(String c_uid) {
        this.c_uid = c_uid;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getQQ() {
        return qq;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public int getQQAuth() {
        return qqAuth;
    }

    public void setQQAuth(int qqAuth) {
        this.qqAuth = qqAuth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMobileAuth() {
        return mobileAuth;
    }

    public void setMobileAuth(int mobileAuth) {
        this.mobileAuth = mobileAuth;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public int getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(int wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public boolean isMonthMail() {
        return isMonthMail;
    }

    public void setMonthMail(boolean monthMail) {
        isMonthMail = monthMail;
    }

    public int getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
    }

    public int getYcoin() {
        return ycoin;
    }

    public void setYcoin(int ycoin) {
        this.ycoin = ycoin;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
