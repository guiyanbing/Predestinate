package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;

import org.json.JSONObject;

/**
 * 用户基本信息
 */
public class UserInfo extends UserBasic {
    private long regTime;       // 注册时间
    private String signName;    // 备注签名
    private int signName_status;// 签名审核状态

    // 未确定共有字段
    private int gem;
    private int status;

    // 自己
    private boolean isVip;      // 是否是VIP
    private long vipEnd;        // VIP到期时间
    private String mobile;      // 手机
    private int money;          // 资产
    private int channel_sid;
    private int channel_uid;
    private int real_status;

    // 他人
    private int kf_id;

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);
        if (!jsonObject.isNull("detail")) {
            String json = jsonObject.optString("detail");
            super.parseJson(json);
            JSONObject detailObject = this.getJsonObject(json);

            // C
            this.setChannel_sid(detailObject.optInt("channel_sid"));
            this.setChannel_uid(detailObject.optInt("channel_uid"));

            // G
            this.setGem(detailObject.optInt("gem"));

            // I
            this.setVip(detailObject.optBoolean("is_vip"));
            this.setVipEnd(detailObject.optLong("vip_end"));

            // K
            this.setKf_id(detailObject.optInt("kf_id"));

            // M
            this.setMobile(detailObject.optString("mobile"));
            this.setMoney(detailObject.optInt("money"));

            // R
            this.setRegTime(detailObject.optLong("reg_time"));
            this.setReal_status(detailObject.optInt("real_status"));

            // S
            this.setSignName(detailObject.optString("signname"));
            this.setSignName_status(detailObject.optInt("signname_status"));
            this.setStatus(detailObject.optInt("status"));
        }
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public int getSignName_status() {
        return signName_status;
    }

    public void setSignName_status(int signName_status) {
        this.signName_status = signName_status;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public long getVipEnd() {
        return vipEnd;
    }

    public void setVipEnd(long vipEnd) {
        this.vipEnd = vipEnd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getChannel_sid() {
        return channel_sid;
    }

    public void setChannel_sid(int channel_sid) {
        this.channel_sid = channel_sid;
    }

    public int getChannel_uid() {
        return channel_uid;
    }

    public void setChannel_uid(int channel_uid) {
        this.channel_uid = channel_uid;
    }

    public int getReal_status() {
        return real_status;
    }

    public void setReal_status(int real_status) {
        this.real_status = real_status;
    }

    public int getKf_id() {
        return kf_id;
    }

    public void setKf_id(int kf_id) {
        this.kf_id = kf_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.regTime);
        dest.writeString(this.signName);
        dest.writeInt(this.signName_status);
        dest.writeInt(this.gem);
        dest.writeInt(this.status);
        dest.writeByte(this.isVip ? (byte) 1 : (byte) 0);
        dest.writeLong(this.vipEnd);
        dest.writeString(this.mobile);
        dest.writeInt(this.money);
        dest.writeInt(this.channel_sid);
        dest.writeInt(this.channel_uid);
        dest.writeInt(this.real_status);
        dest.writeInt(this.kf_id);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        super(in);
        this.regTime = in.readLong();
        this.signName = in.readString();
        this.signName_status = in.readInt();
        this.gem = in.readInt();
        this.status = in.readInt();
        this.isVip = in.readByte() != 0;
        this.vipEnd = in.readLong();
        this.mobile = in.readString();
        this.money = in.readInt();
        this.channel_sid = in.readInt();
        this.channel_uid = in.readInt();
        this.real_status = in.readInt();
        this.kf_id = in.readInt();
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
