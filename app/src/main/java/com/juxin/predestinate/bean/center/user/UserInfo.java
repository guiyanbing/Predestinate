package com.juxin.predestinate.bean.center.user;

import android.os.Parcel;

import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.util.ChineseFilter;

import org.json.JSONObject;

/**
 * 用户基本信息
 */
public class UserInfo extends UserBasic {
    private long exp;  // 经验值
    private int active;  // 活跃值
    private String avatar_bg;    // 头像背景
    private String passedAvatar; // 过审头像地址，为空表示该用户从来都没有通过审核的头像
    private int avatarbg_suatus; // 头像背景审核状态：0为未审核，1为通过，2为拒绝,3未上传 4好，5很好 6待复审 7新版未审核
    private int charm;   // 魅力值
    private String complete; // 资料完整度(%)
    private int coin_count; // 金币数量
    private int diamond_count; // 钻石数量
    private boolean isVip;// 是否开通VIP
    private String mobile; // 手机号码
    private int mobileAuth;
    private String qqNum; // QQ号码
    private int qqNumAuth;// 0为公开，1, VIP可见，2为保密
    private double redTmp; // 今天的收益(临时红包) (单位：分)
    private double redOwn; // 拥有的红包(单位：分)
    private double redBefore; // 昨天的收益（单位：分）
    private int redStatus; // 红包认证状态 0未提交 1审核中 2通过 3未通过
    private long regTime; // 注册时间
    private int rich;    // 财富值
    private String sign; // 个性签名
    private int sign_status; // 个性签名状态  0 未审核 1 通过 2 未通过'
    private String star; // 星座
    private long updateTime; // 最后一次更新资料时间
    private long Vip_start;
    private long Vip_end;
    private String wechat; // 微信账号
    private int wechatAuth;
    private boolean isBindPhone; // 是否绑定手机号
    private long sugar;//棒棒糖
    // 他人
    private String distance;  // 我和对方距离的字符串表述
    private boolean isblack; //用户是否被拉黑
    private boolean isFriend; // 是否是好友
    private boolean isAddedFriend; // 是否被我加过好友
    private boolean is_online; // 用户是否在线
    private boolean isSayHello;// 是否打过招呼
    private String online_info; //在线状态信息
    private int kf_id;           // 机器人ID
    private int user_status; // 用户禁用状态：0/1为正常，2为删除，5为禁用

    // 暂时无用字段, 后续删除
    private boolean ismonth; // 包月状态
    private long Upgrade_start; //包月写信的UNIX时间戳
    private long Upgrade_end;

    @Override
    public void parseJson(String s) {
        InfoConfig infoConfig = InfoConfig.getInstance();
        JSONObject jsonObject = getJsonObject(s);
        if (!jsonObject.isNull("userDetail")) {
            String json = jsonObject.optString("userDetail");
            super.parseJson(json);
            JSONObject detailObject = this.getJsonObject(json);

            // A
            this.setActive(detailObject.optInt("active"));
            this.setAvatar_bg(detailObject.optString("avatar_bg"));
            this.setAvatarbg_suatus(detailObject.optInt("avatar_bg_status"));

            // C
            this.setComplete(detailObject.optString("complete"));

            this.setCharm(detailObject.optInt("charm"));

            // D
            this.setDistance(detailObject.optString("distance"));

            // E
            this.setExp(detailObject.optLong("exp"));

            // F
            // G
            this.setDiamond_count(detailObject.optInt("gem"));
            this.setCoin_count(detailObject.optInt("gold"));

            // H
            // I
            this.setIsmonth(detailObject.optBoolean("is_month"));
            this.setSayHello(detailObject.optBoolean("is_say_hello"));
            this.setFriend(detailObject.optBoolean("is_friend"));
            this.isAddedFriend = detailObject.optBoolean("is_wfriend");
            this.setVip(detailObject.optBoolean("is_vip"));
            this.setIsblack(detailObject.optBoolean("is_black"));
            this.setVerifyCellphone(detailObject.optBoolean("is_bind_phone"));
            this.setIs_online(detailObject.optBoolean("is_online"));

            // J
            // K
            this.setKf_id(detailObject.optInt("kf_id"));

            // L
            // M
            this.setMobile(detailObject.optString("mobile"));
            this.setMobileAuth(detailObject.optInt("mobile_auth"));

            // N
            // O
            this.setOnline_info(detailObject.optString("online_info"));

            // P
            this.setPassedAvatar(detailObject.optString("passed_avatar"));

            // Q
            this.setQqNum(detailObject.optString("qq"));
            this.setQqNumAuth(detailObject.optInt("qq_auth"));

            // R
            this.setRich(detailObject.optInt("rich"));
            this.setRedTmp(detailObject.optDouble("red_tmp"));
            this.setRedOwn(detailObject.optDouble("red_own"));
            this.setRedBefore(detailObject.optDouble("red_yesterday"));
            this.setRedStatus(detailObject.optInt("real_status"));
            this.setRegTime(detailObject.optLong("reg_time"));

            // S
            this.setStar(infoConfig.getStar().getShowWithSubmit(detailObject.optInt("star")));
            this.setSign(detailObject.optString("signname"));
            this.setSign_status(detailObject.optInt("signname_status"));
            this.setSugar(detailObject.optLong("sugar"));

            // T
            // U
            this.setUpdateTime(detailObject.optLong("updatetime"));
            this.setUpgrade_start(detailObject.optLong("upgrade_start"));
            this.setUpgrade_end(detailObject.optLong("upgrade_end"));
            this.setUser_status(detailObject.optInt("status"));

            // V
            this.setVip_start(detailObject.optLong("vip_start"));
            this.setVip_end(detailObject.optLong("vip_end"));

            // W
            this.setWeChat(detailObject.optString("wechat"));
            this.setWeChatAuth(detailObject.optInt("wechat_auth"));

            // X
            // Y
        }
    }

    public long getSugar() {
        return sugar;
    }

    public void setSugar(long sugar) {
        this.sugar = sugar;
    }

    public String getPassedAvatar() {
        return passedAvatar;
    }

    public void setPassedAvatar(String passedAvatar) {
        this.passedAvatar = passedAvatar;
    }

    public int getKf_id() {
        return kf_id;
    }

    public void setKf_id(int kf_id) {
        this.kf_id = kf_id;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public int getActive() {

        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCharm() {
        return charm;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

    public int getRich() {
        return rich;
    }

    public void setRich(int rich) {
        this.rich = rich;
    }

    public int getDiamond_count() {
        return diamond_count;
    }

    public void setDiamond_count(int diamond_count) {
        this.diamond_count = diamond_count;
    }

    public int getCoin_count() {
        return coin_count;
    }

    public void setCoin_count(int coin_count) {
        this.coin_count = coin_count;
    }

    public double getRedTmp() {
        return redTmp;
    }

    public String getRedTmpEx() {
        return ChineseFilter.subZeroString(redTmp + "");
    }

    public void setRedTmp(double redTmp) {
        this.redTmp = redTmp / 100;//保留两位小数
    }

    public double getRedOwn() {
        return redOwn;
    }

    public String getRedOwnEx() {
        return ChineseFilter.subZeroString(redOwn + "");
    }

    public void setRedOwn(double redOwn) {
        this.redOwn = redOwn / 100;
    }

    public double getRedBefore() {
        return redBefore;
    }

    public String getRedBeforeEx() {
        return ChineseFilter.subZeroString(redBefore + "");
    }

    public void setRedBefore(double redBefore) {
        this.redBefore = redBefore / 100;
    }

    public int getRedStatus() {
        return redStatus;
    }

    public void setRedStatus(int redStatus) {
        this.redStatus = redStatus;
    }

    public String getAvatar_bg() {
        return avatar_bg;
    }

    public void setAvatar_bg(String avatar_bg) {
        this.avatar_bg = avatar_bg;
    }

    public int getAvatarbg_suatus() {
        return avatarbg_suatus;
    }

    public void setAvatarbg_suatus(int avatarbg_suatus) {
        this.avatarbg_suatus = avatarbg_suatus;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getSign_status() {
        return sign_status;
    }

    public void setSign_status(int sign_status) {
        this.sign_status = sign_status;
    }

    public int getMobileAuth() {
        return mobileAuth;
    }

    public void setMobileAuth(int mobileAuth) {
        this.mobileAuth = mobileAuth;
    }

    public boolean isAddedFriend() {
        return isAddedFriend;
    }

    public void setAddedFriend(boolean addedFriend) {
        isAddedFriend = addedFriend;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean ismonth() {
        return ismonth;
    }

    public void setIsmonth(boolean ismonth) {
        this.ismonth = ismonth;
    }

    public boolean isblack() {
        return isblack;
    }

    public void setIsblack(boolean isblack) {
        this.isblack = isblack;
    }

    public long getUpgrade_start() {
        return Upgrade_start;
    }

    public void setUpgrade_start(long upgrade_start) {
        Upgrade_start = upgrade_start;
    }

    public long getUpgrade_end() {
        return Upgrade_end;
    }

    public void setUpgrade_end(long upgrade_end) {
        Upgrade_end = upgrade_end;
    }

    public boolean is_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    public long getVip_start() {
        return Vip_start;
    }

    public void setVip_start(long vip_start) {
        Vip_start = vip_start;
    }

    public long getVip_end() {
        return Vip_end;
    }

    public void setVip_end(long vip_end) {
        Vip_end = vip_end;
    }

    private boolean isSelf = true; // 默认解析自己的个人资料

    public void isParserSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public boolean getIsSayHello() {
        return isSayHello;
    }

    public void setSayHello(boolean isSayHello) {
        this.isSayHello = isSayHello;
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }

    public boolean isVerifyCellphone() {
        return isBindPhone;
    }

    public void setVerifyCellphone(boolean isVerifyCellphone) {
        this.isBindPhone = isVerifyCellphone;
    }

    public String getStar() {
        if ("不限".equals(star)) {
            return "";
        }
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getQqNum() {
        return "0".equals(qqNum) ? "" : qqNum;
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

    public String getMobile() {
        return "0".equals(mobile) ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeChat() {
        return wechat;
    }

    public void setWeChat(String wechat) {
        this.wechat = wechat;
    }

    public int getWeChatAuth() {
        return wechatAuth;
    }

    public void setWeChatAuth(int wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean isMan() {
//        return ModuleMgr.getCenterMgr().isMan(getGender());
        return false;
    }

    public String getOnline_info() {
        return online_info;
    }

    public void setOnline_info(String online_info) {
        this.online_info = online_info;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.exp);
        dest.writeInt(this.active);
        dest.writeString(this.avatar_bg);
        dest.writeString(this.passedAvatar);
        dest.writeInt(this.avatarbg_suatus);
        dest.writeInt(this.charm);
        dest.writeString(this.complete);
        dest.writeInt(this.coin_count);
        dest.writeInt(this.diamond_count);
        dest.writeByte(this.isVip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.ismonth ? (byte) 1 : (byte) 0);
        dest.writeString(this.mobile);
        dest.writeInt(this.mobileAuth);
        dest.writeString(this.qqNum);
        dest.writeInt(this.qqNumAuth);
        dest.writeDouble(this.redTmp);
        dest.writeDouble(this.redOwn);
        dest.writeDouble(this.redBefore);
        dest.writeInt(this.redStatus);
        dest.writeLong(this.regTime);
        dest.writeInt(this.rich);
        dest.writeString(this.sign);
        dest.writeInt(this.sign_status);
        dest.writeString(this.star);
        dest.writeLong(this.updateTime);
        dest.writeLong(this.Upgrade_start);
        dest.writeLong(this.Upgrade_end);
        dest.writeLong(this.Vip_start);
        dest.writeLong(this.Vip_end);
        dest.writeString(this.wechat);
        dest.writeInt(this.wechatAuth);
        dest.writeByte(this.isBindPhone ? (byte) 1 : (byte) 0);
        dest.writeLong(this.sugar);
        dest.writeString(this.distance);
        dest.writeByte(this.isblack ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFriend ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAddedFriend ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_online ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSayHello ? (byte) 1 : (byte) 0);
        dest.writeString(this.online_info);
        dest.writeInt(this.kf_id);
        dest.writeInt(this.user_status);
        dest.writeByte(this.isSelf ? (byte) 1 : (byte) 0);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        super(in);
        this.exp = in.readLong();
        this.active = in.readInt();
        this.avatar_bg = in.readString();
        this.passedAvatar = in.readString();
        this.avatarbg_suatus = in.readInt();
        this.charm = in.readInt();
        this.complete = in.readString();
        this.coin_count = in.readInt();
        this.diamond_count = in.readInt();
        this.isVip = in.readByte() != 0;
        this.ismonth = in.readByte() != 0;
        this.mobile = in.readString();
        this.mobileAuth = in.readInt();
        this.qqNum = in.readString();
        this.qqNumAuth = in.readInt();
        this.redTmp = in.readDouble();
        this.redOwn = in.readDouble();
        this.redBefore = in.readDouble();
        this.redStatus = in.readInt();
        this.regTime = in.readLong();
        this.rich = in.readInt();
        this.sign = in.readString();
        this.sign_status = in.readInt();
        this.star = in.readString();
        this.updateTime = in.readLong();
        this.Upgrade_start = in.readLong();
        this.Upgrade_end = in.readLong();
        this.Vip_start = in.readLong();
        this.Vip_end = in.readLong();
        this.wechat = in.readString();
        this.wechatAuth = in.readInt();
        this.isBindPhone = in.readByte() != 0;
        this.sugar = in.readLong();
        this.distance = in.readString();
        this.isblack = in.readByte() != 0;
        this.isFriend = in.readByte() != 0;
        this.isAddedFriend = in.readByte() != 0;
        this.is_online = in.readByte() != 0;
        this.isSayHello = in.readByte() != 0;
        this.online_info = in.readString();
        this.kf_id = in.readInt();
        this.user_status = in.readInt();
        this.isSelf = in.readByte() != 0;
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
