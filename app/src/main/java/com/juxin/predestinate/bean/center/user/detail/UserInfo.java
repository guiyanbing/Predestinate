package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;

import org.json.JSONObject;

/**
 * 用户基本信息
 */
public class UserInfo extends UserBasic {
    private String aboutme;         // 内心独白
    private String avatar_121;      // 头像小图 121*153
    private String avatar_146;      // 头像小图 146*185
    private boolean c_user;         // 自增用户
    private boolean isOnline;       // 是否在线
    private int idcard_validation;  // 身份证验证状态  0 未提交 1 待审核 2 审核通过 3 审核不通过
    private String mobile;          // 手机号码
    private int mobileAuth;         // 1为公开，2为保密
    private int mobile_validation;  // 手机是否已验证
    private String qq;              // QQ号码
    private int qqAuth;             // 1为公开，2为保密
    private String weChat;          // 微信号码
    private int wechatAuth;         // 1为公开, 2为保密
    private String shareCode;       // 邀请码
    private int user_status;        // 用户封禁状态  1为正常，2为删除，5为禁用
    private boolean miss_info;      // 资料是否完整
    private int videoAuth;          // 用户视频权限
    private int ycoin = 0;          // Y币
    private int topN;               // 用户排行
    private int group;              // 1 普通用户  2,3包月用户

    // --------------- 自己字段 -----------------------
    private String complete;        // 资料完整度(%)
    private String idcard;          // 身份证号
    private double redbagsum;       // 红包总额
    private String reasons;         // 头像被拒原因
    private int diamand;            // 我的钻石
    private long memdatenum;        // 计算会员到期时间
    private String cell_phone;      // 认证的手机号

    // --------------- TA人字段 ------------------------
    private int kf_id;
    private int distance;           // 距离
    private int followmecount;      // 关注数
    private int isfollow;           // 是否已关注该用户
    private String online_text;     // 在线时间 "七天前在线"
    private boolean isSayHello;     // 是否已打招呼

    // 个人空间
    private String datingfor;       // 目的，交友意向
    private String concept;         // 观念
    private String favplace;        // 地点
    private String favaction;       // 希望

    @Override
    public void parseJson(String s) {
        super.parseJson(s);
        JSONObject detailObject = getJsonObject(s);

        // A
        this.setAboutme(detailObject.optString("aboutme"));
        this.setAvatar_121(detailObject.optString("avatar_121"));
        this.setAvatar_146(detailObject.optString("avatar_146"));

        // B
        // C
        this.setC_user(detailObject.optBoolean("c_user"));
        this.setComplete(detailObject.optString("complete"));
        this.setCell_phone(detailObject.optString("cell_phone"));
        this.setConcept(detailObject.optString("concept"));

        // D
        this.setDiamand(detailObject.optInt("diamand"));
        this.setDistance(detailObject.optInt("distance"));
        this.setDatingfor(detailObject.optString("datingfor"));

        // F
        this.setFollowmecount(detailObject.optInt("followmecount"));
        this.setFavplace(detailObject.optString("favplace"));
        this.setFavaction(detailObject.optString("favaction"));

        // G
        this.setGroup(detailObject.optInt("group"));

        // I
        this.setOnline(detailObject.optBoolean("is_online"));
        this.setIdcard(detailObject.optString("idcard"));
        this.setIdcard_validation(detailObject.optInt("idcard_validation"));
        this.setIsfollow(detailObject.optInt("is_follow"));
        this.setSayHello(detailObject.optBoolean("isSayHello"));

        // K
        this.setKf_id(detailObject.optInt("kf_id"));

        // M
        this.setMemdatenum(detailObject.optLong("memdatenum"));
        this.setMobile(detailObject.optString("mobile"));
        this.setMobileAuth(detailObject.optInt("mobile_auth"));
        this.setMobile_validation(detailObject.optInt("mobile_validation"));
        this.setMiss_info(detailObject.optBoolean("miss_info"));

        // O
        this.setOnline_text(detailObject.optString("online_text"));

        // P
        // Q
        this.setQQ(detailObject.optString("qq"));
        this.setQQAuth(detailObject.optInt("qq_auth"));

        // R
        this.setRedbagsum(detailObject.optDouble("redbagsum"));
        this.setReasons(detailObject.optString("reasons"));

        // S
        this.setShareCode(detailObject.optString("shareCode"));

        // T
        this.setTopN(detailObject.optInt("topN"));

        // U
        this.setUser_status(detailObject.optInt("user_status"));

        // V
        this.setVideoAuth(detailObject.optInt("video_auth"));

        // W
        this.setWeChat(detailObject.optString("wechat"));
        this.setWechatAuth(detailObject.optInt("wechat_auth"));

        // Y
        this.setYcoin(detailObject.optInt("ycoin"));
    }

    /**
     * 是否通过真人认证
     */
    public boolean isVerifyIdcard() {
        return idcard_validation == 2;
    }

    /**
     * 判断是否是vip用户通过 'group' 判断
     */
    public boolean isVip() {
        return group == 2 || group == 3;
    }

    /**
     * 是否绑定手机
     */
    public boolean isVerifyCellphone() {
        return mobile_validation != 0;
    }

    /**
     * 是否已关注
     */
    public boolean isFollow() {
        return isfollow != 0;
    }

    /**
     * 是否已打招呼
     */
    public boolean isSayHello() {
        return isSayHello;
    }

    /**
     * 用户是否处于正常状态
     *
     * @return true 正常  false  删除/禁用
     */
    public boolean isUserNormal() {
        return user_status == 1;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setSayHello(boolean sayHello) {
        isSayHello = sayHello;
    }

    public void setVerifyCellphone(boolean verifyCellphone) {
        if (verifyCellphone) {
            mobile_validation = 1;
            return;
        }
        mobile_validation = 0;
    }

    public String getDatingfor() {
        return datingfor;
    }

    public void setDatingfor(String datingfor) {
        this.datingfor = datingfor;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getFavplace() {
        return favplace;
    }

    public void setFavplace(String favplace) {
        this.favplace = favplace;
    }

    public String getFavaction() {
        return favaction;
    }

    public void setFavaction(String favaction) {
        this.favaction = favaction;
    }

    public int getTopN() {
        return topN;
    }

    public void setTopN(int topN) {
        this.topN = topN;
    }

    public boolean isMiss_info() {
        return miss_info;
    }

    public void setMiss_info(boolean miss_info) {
        this.miss_info = miss_info;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getKf_id() {
        return kf_id;
    }

    public void setKf_id(int kf_id) {
        this.kf_id = kf_id;
    }

    public String getOnline_text() {
        return online_text;
    }

    public void setOnline_text(String online_text) {
        this.online_text = online_text;
    }


    public void setIsfollow(int isfollow) {
        this.isfollow = isfollow;
    }

    public void setMobile_validation(int mobile_validation) {
        this.mobile_validation = mobile_validation;
    }

    public int getFollowmecount() {
        return followmecount;
    }

    public void setFollowmecount(int followmecount) {
        this.followmecount = followmecount;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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


    public UserInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.aboutme);
        dest.writeString(this.avatar_121);
        dest.writeString(this.avatar_146);
        dest.writeByte(this.c_user ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOnline ? (byte) 1 : (byte) 0);
        dest.writeInt(this.idcard_validation);
        dest.writeString(this.mobile);
        dest.writeInt(this.mobileAuth);
        dest.writeInt(this.mobile_validation);
        dest.writeString(this.qq);
        dest.writeInt(this.qqAuth);
        dest.writeString(this.weChat);
        dest.writeInt(this.wechatAuth);
        dest.writeString(this.shareCode);
        dest.writeInt(this.user_status);
        dest.writeByte(this.miss_info ? (byte) 1 : (byte) 0);
        dest.writeInt(this.videoAuth);
        dest.writeInt(this.ycoin);
        dest.writeInt(this.topN);
        dest.writeInt(this.group);
        dest.writeString(this.complete);
        dest.writeString(this.idcard);
        dest.writeDouble(this.redbagsum);
        dest.writeString(this.reasons);
        dest.writeInt(this.diamand);
        dest.writeLong(this.memdatenum);
        dest.writeString(this.cell_phone);
        dest.writeInt(this.kf_id);
        dest.writeInt(this.distance);
        dest.writeInt(this.followmecount);
        dest.writeInt(this.isfollow);
        dest.writeString(this.online_text);
        dest.writeByte(this.isSayHello ? (byte) 1 : (byte) 0);
        dest.writeString(this.datingfor);
        dest.writeString(this.concept);
        dest.writeString(this.favplace);
        dest.writeString(this.favaction);
    }

    protected UserInfo(Parcel in) {
        super(in);
        this.aboutme = in.readString();
        this.avatar_121 = in.readString();
        this.avatar_146 = in.readString();
        this.c_user = in.readByte() != 0;
        this.isOnline = in.readByte() != 0;
        this.idcard_validation = in.readInt();
        this.mobile = in.readString();
        this.mobileAuth = in.readInt();
        this.mobile_validation = in.readInt();
        this.qq = in.readString();
        this.qqAuth = in.readInt();
        this.weChat = in.readString();
        this.wechatAuth = in.readInt();
        this.shareCode = in.readString();
        this.user_status = in.readInt();
        this.miss_info = in.readByte() != 0;
        this.videoAuth = in.readInt();
        this.ycoin = in.readInt();
        this.topN = in.readInt();
        this.group = in.readInt();
        this.complete = in.readString();
        this.idcard = in.readString();
        this.redbagsum = in.readDouble();
        this.reasons = in.readString();
        this.diamand = in.readInt();
        this.memdatenum = in.readLong();
        this.cell_phone = in.readString();
        this.kf_id = in.readInt();
        this.distance = in.readInt();
        this.followmecount = in.readInt();
        this.isfollow = in.readInt();
        this.online_text = in.readString();
        this.isSayHello = in.readByte() != 0;
        this.datingfor = in.readString();
        this.concept = in.readString();
        this.favplace = in.readString();
        this.favaction = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
