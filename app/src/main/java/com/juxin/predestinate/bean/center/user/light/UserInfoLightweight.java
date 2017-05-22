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
    private boolean isOk;


    private String alias;       // 别名
    private boolean isVip;      // 是否vip
    private boolean isAuth;     // 是否认证
    private String signname;    // 签名

    private String distance; //距离
    private boolean video_available = false;
    private boolean audio_available = false;
    private boolean video_busy = false;
    private boolean isSayHello = false;

    private int heartNum = 0;

    private int kf_id = 0; //不为0就是机器人
    private int top = 0;//排行榜排名

    /**
     * 0 没上榜 1土豪榜 2魅力榜
     * 暂时 不用该字段 用用户性别进行判断
     */
    private int topType = 0;

    public UserInfoLightweight() {
    }

    public UserInfoLightweight(long uid, String infoJson, long time) {
        this.uid = uid;
        this.infoJson = infoJson;
        this.time = time;
    }

    public void parseUserInfoLightweight(UserDetail userInfo) {
        if (userInfo == null) return;
        this.setUid(userInfo.getUid());
        this.setNickname(userInfo.getNickname());
        this.setGender(userInfo.getGender());
        this.setAvatar(userInfo.getAvatar());
    }

    /**
     * 新版本，新接口解析
     */
    @Override
    public void parseJson(String jsonResult) {
        super.parseJson(jsonResult);
        if (TextUtils.isEmpty(jsonResult)) return;
        this.setInfoJson(jsonResult);
        JSONObject jsonObject = getJsonObject(jsonResult);
        this.setAlias(jsonObject.optString("alias"));
        this.setVip(jsonObject.optBoolean("is_vip"));
        this.setAuth(jsonObject.optBoolean("is_auth"));
        this.setSignname(jsonObject.optString("signname"));
        this.setDistance(jsonObject.optString("distance"));
        this.setVideo_busy(jsonObject.optInt("video_busy") == 1);
        this.setVideo_available(jsonObject.optInt("video_available") == 1);
        this.setAudio_available(jsonObject.optInt("audio_available") == 1);
        this.setSayHello(jsonObject.optBoolean("isSayHello"));
        this.setHeartNum(jsonObject.optInt("heartnum"));
        this.setKf_id(jsonObject.optInt("kf_id"));
        this.setTop(jsonObject.optInt("top"));
        this.setTopType(jsonObject.optInt("toptype"));
    }

    public void parseJson(JSONObject jsonObject) {
        if(jsonObject == null || jsonObject.isNull("result")) return;

        setOk("success".equals(jsonObject.optString("result")));
        if(isOk()) return;

        JSONObject jsonResult = jsonObject.optJSONObject("content");
        this.setInfoJson(jsonResult.toString());

        this.setUid(jsonResult.optLong("uid"));
        this.setAvatar(jsonResult.optString("avatar"));
        this.setAvatar_status(jsonResult.optInt("avatarstatus"));
        this.setGender(jsonResult.optInt("gender"));
        this.setNickname(jsonResult.optString("nickname"));
        this.setVip(jsonObject.optBoolean("is_vip"));
        this.setSignname(jsonObject.optString("sign"));



        this.setUsername(jsonResult.optString("username"));




        this.setAge(jsonResult.optInt("age"));
        this.setBirthday(jsonResult.optString("birthday"));
        this.setHeight(jsonResult.optInt("height"));


        this.setAlias(jsonObject.optString("alias"));

        this.setAuth(jsonObject.optBoolean("is_auth"));

        this.setDistance(jsonObject.optString("distance"));
        this.setVideo_busy(jsonObject.optInt("video_busy") == 1);
        this.setVideo_available(jsonObject.optInt("video_available") == 1);
        this.setAudio_available(jsonObject.optInt("audio_available") == 1);
        this.setSayHello(jsonObject.optBoolean("isSayHello"));
        this.setHeartNum(jsonObject.optInt("heartnum"));
        this.setKf_id(jsonObject.optInt("kf_id"));
        this.setTop(jsonObject.optInt("top"));
        this.setTopType(jsonObject.optInt("toptype"));
    }


    public void parseUserInfoLightweight(long userID, String infoJson, long time) {
        this.setUid(userID);
        this.setTime(time);
        parseJson(infoJson);
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isVideo_available() {
        return video_available;
    }

    public void setVideo_available(boolean video_available) {
        this.video_available = video_available;
    }

    public boolean isAudio_available() {
        return audio_available;
    }

    public void setAudio_available(boolean audio_available) {
        this.audio_available = audio_available;
    }

    public boolean isVideo_busy() {
        return video_busy;
    }

    public void setVideo_busy(boolean video_busy) {
        this.video_busy = video_busy;
    }

    public boolean isSayHello() {
        return isSayHello;
    }

    public void setSayHello(boolean sayHello) {
        isSayHello = sayHello;
    }

    public int getHeartNum() {
        return heartNum;
    }

    public void setHeartNum(int heartNum) {
        this.heartNum = heartNum;
    }

    public int getKf_id() {
        return kf_id;
    }

    public void setKf_id(int kf_id) {
        this.kf_id = kf_id;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getTopType() {
        return topType;
    }


    public boolean isToper() {
        return getTop() != 0;
    }

    public void setTopType(int topType) {
        this.topType = topType;
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