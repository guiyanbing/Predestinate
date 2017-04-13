package com.juxin.predestinate.bean.center.user.light;

import android.os.Parcel;
import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.detail.UserBasic;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;

import org.json.JSONObject;

/**
 * 个人资料轻量级
 * Created by Kind on 16/3/22.
 */
public class UserInfoLightweight extends UserBasic {

    private long time;
    private String infoJson;    //存储json

    private String alias;       // 别名
    private boolean isVip;      // 是否vip
    private boolean isAuth;     // 是否认证
    private String signname;    // 签名

    public UserInfoLightweight() {
    }

    public UserInfoLightweight(long uid, String infoJson, long time) {
        this.uid = uid;
        this.infoJson = infoJson;
        this.time = time;
    }

    public void parseUserInfoLightweight(UserDetail userInfo) {
        if (userInfo == null) return;
    }

    /**
     * 新版本，新接口解析
     */
    @Override
    public void parseJson(String jsonResult) {
        if (TextUtils.isEmpty(jsonResult)) return;

        JSONObject jsonObject = getJsonObject(jsonResult);
        this.setAlias(jsonObject.optString("alias"));
        this.setVip(jsonObject.optBoolean("is_vip"));
        this.setAuth(jsonObject.optBoolean("is_auth"));
        this.setSignname(jsonObject.optString("signname"));
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.time);
        dest.writeString(this.infoJson);
        dest.writeString(this.alias);
        dest.writeByte(this.isVip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAuth ? (byte) 1 : (byte) 0);
        dest.writeString(this.signname);
    }

    protected UserInfoLightweight(Parcel in) {
        super(in);
        this.time = in.readLong();
        this.infoJson = in.readString();
        this.alias = in.readString();
        this.isVip = in.readByte() != 0;
        this.isAuth = in.readByte() != 0;
        this.signname = in.readString();
    }

    public static final Creator<UserInfoLightweight> CREATOR = new Creator<UserInfoLightweight>() {
        @Override
        public UserInfoLightweight createFromParcel(Parcel source) {
            return new UserInfoLightweight(source);
        }

        @Override
        public UserInfoLightweight[] newArray(int size) {
            return new UserInfoLightweight[size];
        }
    };

    @Override
    public String toString() {
        return "UserInfoLightweight{" +
                "time=" + time +
                ", infoJson='" + infoJson + '\'' +
                ", alias='" + alias + '\'' +
                ", isVip=" + isVip +
                ", isAuth=" + isAuth +
                ", signname='" + signname + '\'' +
                '}';
    }
}