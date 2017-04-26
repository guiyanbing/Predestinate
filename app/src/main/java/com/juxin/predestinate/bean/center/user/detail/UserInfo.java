package com.juxin.predestinate.bean.center.user.detail;

import org.json.JSONObject;

/**
 * 用户基本信息
 */
public class UserInfo extends UserBasic {
    private boolean isSayHello;     // 是否打过招呼 true是打过
    private String aboutme;         // 内心独白
    private String complete;        // 资料完整度(%)
    private int hits;               // 点击数
    private String c_uid;           // -1机器人
    private boolean isOnline;       // 在线数
    private int distance;           // 距离
    private boolean isFollowed;     // 是否关注
    private String qqNum;           // QQ号码
    private int qqNumAuth;          // 1为公开，2为保密
    private String phone;           // 手机号码
    private int phoneAuth;          // 1为公开，2为保密
    private boolean isVip;          // 是否开通VIP
    private String wechatNum;       //微信号码
    private int wechatAuth;         // 1为公开, 2为保密
    private boolean isVerifyCellphone; // 是否绑定了手机
    private boolean isMonthMail;    // 是否开通包月发信
    private String reasons;         // 上传头像审核未通过的原因
    private int photoNum;           // 相册的数目
    private long regTime;           // 注册时间
    private boolean isBindRose;     // 是否绑定了红娘
    private int ycoin = 0;          // Y币


    // -------2017-1-5红包来了添加字段 -------
    private String shareCode;       //邀请码
    private long invite_uid;        //邀请人ID,0表示无邀请人
    private UserRobbed userRobbed;  //抢夺信息

    @Override
    public void parseJson(String s) {
        super.parseJson(s);
        JSONObject detailObject = getJsonObject(s);

        this.setSayHello(detailObject.optBoolean("isSayHello"));
        this.setAboutme(detailObject.isNull("aboutme") ? null : detailObject.optString("aboutme"));
        this.setComplete(detailObject.optString("complete"));
        this.setHits(detailObject.optInt("hits"));
        this.setC_uid(detailObject.optString("c_uid"));
        this.setOnline(detailObject.optBoolean("is_online"));
        this.setDistance(detailObject.optInt("distance"));
        this.setFollowed(detailObject.optBoolean("is_followed"));
        this.setQqNum(detailObject.optString("qq"));
        this.setQqNumAuth(detailObject.optInt("qq_auth"));
        this.setPhone(detailObject.optString("mobile"));
        this.setPhoneAuth(detailObject.optInt("mobile_auth"));
        this.setVip(detailObject.optBoolean("is_month_mail"));
        this.setWechatNum(detailObject.optString("wechat"));
        this.setWechatAuth(detailObject.optInt("wechat_auth"));
        this.setVerifyCellphone(detailObject.optBoolean("is_verify_cellphone"));
        this.setMonthMail(detailObject.optBoolean("is_month_mail"));
        this.setReasons(detailObject.isNull("reasons") ? null : detailObject.optString("reasons"));
        this.setPhotoNum(detailObject.optInt("photoNum"));
        this.setRegTime(detailObject.optLong("reg_time"));
        this.setBindRose(detailObject.optBoolean("is_bind_roes"));
        this.setYcoin(detailObject.optInt("ycoin"));
        this.setShareCode(detailObject.optString("shareCode"));
        this.setInvite_uid(detailObject.optLong("invite_uid"));

        UserRobbed userRobbed = new UserRobbed();
        userRobbed.parseJson(detailObject.optString("rob"));
        this.setUserRobbed(userRobbed);
    }

    public boolean isSayHello() {
        return isSayHello;
    }

    public void setSayHello(boolean sayHello) {
        isSayHello = sayHello;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public int getQqNumAuth() {
        return qqNumAuth;
    }

    public void setQqNumAuth(int qqNumAuth) {
        this.qqNumAuth = qqNumAuth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhoneAuth() {
        return phoneAuth;
    }

    public void setPhoneAuth(int phoneAuth) {
        this.phoneAuth = phoneAuth;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    public int getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(int wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public boolean isVerifyCellphone() {
        return isVerifyCellphone;
    }

    public void setVerifyCellphone(boolean verifyCellphone) {
        isVerifyCellphone = verifyCellphone;
    }

    public boolean isMonthMail() {
        return isMonthMail;
    }

    public void setMonthMail(boolean monthMail) {
        isMonthMail = monthMail;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public int getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }

    public boolean isBindRose() {
        return isBindRose;
    }

    public void setBindRose(boolean bindRose) {
        isBindRose = bindRose;
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

    public long getInvite_uid() {
        return invite_uid;
    }

    public void setInvite_uid(long invite_uid) {
        this.invite_uid = invite_uid;
    }

    public UserRobbed getUserRobbed() {
        return userRobbed;
    }

    public void setUserRobbed(UserRobbed userRobbed) {
        this.userRobbed = userRobbed;
    }
}
