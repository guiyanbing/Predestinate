package com.juxin.predestinate.bean.center.user.light;

import android.os.Parcel;
import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.detail.UserBasic;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.config.AreaConfig;

import org.json.JSONObject;

/**
 * 个人资料轻量级
 * Created by Kind on 16/3/22.
 */
public class UserInfoLightweight extends UserBasic {

    private boolean isOk = false;
    private long time;
    private String infoJson;    //存储json

    private int channel_uid;
    private int group;
    private boolean isOnline;
    private int photoNum;


    private String remark;      // 备注名
    private boolean isVip;      // 是否vip
    private boolean isAuth;     // 是否认证
    private String signname;    // 签名

    private String distance; //距离
    private boolean video_available = false; //可否视频
    private boolean audio_available = false; //可否语音
    private boolean video_busy = false; //是否通话中
    private boolean isSayHello = false; //是否打过招呼

    private int heartNum = 0; //亲密度

    private int kf_id = 0; //不为0就是机器人
    private int top = 0;//排行榜排名

    /**
     * 0 没上榜 1土豪榜 2魅力榜
     * 暂时 不用该字段 用用户性别进行判断
     */
    private int topType = 0;

    private String last_onLine;

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
        this.setRemark(userInfo.getRemark());
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
        this.setRemark(jsonObject.optString("remark"));
        this.setAuth(jsonObject.optBoolean("is_auth"));
        this.setSignname(jsonObject.optString("signname"));
        this.setDistance(jsonObject.optString("distance"));
        this.setVideo_busy(jsonObject.optInt("video_busy") == 1);
        if (!jsonObject.isNull("video_available")) {
            this.setVideo_available(jsonObject.optInt("video_available") == 1);
        } else {
            this.setVideo_available(jsonObject.optInt("videochat") == 1);
        }
        if (!jsonObject.isNull("audio_available")) {
            this.setAudio_available(jsonObject.optInt("audio_available") == 1);
        } else {
            this.setAudio_available(jsonObject.optInt("audiochat") == 1);
        }

        this.setSayHello(jsonObject.optBoolean("isSayHello"));
        this.setHeartNum(jsonObject.optInt("diamond"));
        this.setKf_id(jsonObject.optInt("kf_id"));
        this.setTop(jsonObject.optInt("top"));
        this.setTopType(jsonObject.optInt("toptype"));
        this.setPhotoNum(jsonObject.optInt("photoNum"));
        this.setGroup(jsonObject.optInt("group"));
        this.setOnline(jsonObject.optBoolean("is_online"));
        this.setLast_onLine(jsonObject.optString("last_online"));
    }

    public void parseJson(JSONObject jsonObject) {
        if (jsonObject == null) return;

        this.setOk(true);
        this.setInfoJson(jsonObject.toString());

        this.setUid(jsonObject.optLong("uid"));
        this.setAvatar(jsonObject.optString("avatar"));
        this.setAvatar_status(jsonObject.optInt("avatarstatus"));
        this.setGender(jsonObject.optInt("gender"));
        this.setNickname(jsonObject.optString("nickname"));
        this.setSignname(jsonObject.optString("about"));
        this.setAge(jsonObject.optInt("age"));
        this.setRemark(jsonObject.optString("remark"));


        this.setHeight(jsonObject.optInt("height"));
        this.setKf_id(jsonObject.optInt("kf_id"));
        this.setTop(jsonObject.optInt("top"));
        this.setTopType(jsonObject.optInt("toptype"));
        int pid = jsonObject.optInt("province");
        int cit = jsonObject.optInt("city");
        this.setScity(cit);
        this.setSprovince(pid);
        this.setProvince(AreaConfig.getInstance().getProvinceByID(pid));
        this.setCity(AreaConfig.getInstance().getCityByID(cit));
        this.setProvinceName(AreaConfig.getInstance().getProvinceNameByID(pid));
        this.setCityName(AreaConfig.getInstance().getCityNameByID(cit));
        this.setChannel_uid(jsonObject.optInt("channel_uid"));
        this.setGroup(jsonObject.optInt("group"));
        this.setOnline(jsonObject.optBoolean("isOnline"));

        if (!jsonObject.isNull("video_available")) {
            this.setVideo_available(jsonObject.optInt("video_available") == 1);
        } else {
            this.setVideo_available(jsonObject.optInt("videochat") == 1);
        }
        if (!jsonObject.isNull("audio_available")) {
            this.setAudio_available(jsonObject.optInt("audio_available") == 1);
        } else {
            this.setAudio_available(jsonObject.optInt("audiochat") == 1);
        }

    }


    public void parseUserInfoLightweight(long userID, String infoJson, long time) {
        this.setUid(userID);
        this.setTime(time);
        parseJson(infoJson);
    }

    /**
     * 展示名称
     */
    public String getShowName() {
        return TextUtils.isEmpty(remark) ? getNickname() : remark;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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


    public int getChannel_uid() {
        return channel_uid;
    }

    public void setChannel_uid(int channel_uid) {
        this.channel_uid = channel_uid;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getLast_onLine() {
        return last_onLine;
    }

    public void setLast_onLine(String last_onLine) {
        this.last_onLine = last_onLine;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.time);
        dest.writeString(this.infoJson);
        dest.writeString(this.remark);
        dest.writeByte(this.isVip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAuth ? (byte) 1 : (byte) 0);
        dest.writeString(this.signname);
    }

    protected UserInfoLightweight(Parcel in) {
        super(in);
        this.time = in.readLong();
        this.infoJson = in.readString();
        this.remark = in.readString();
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
                ", remark='" + remark + '\'' +
                ", isVip=" + isVip +
                ", isAuth=" + isAuth +
                ", signname='" + signname + '\'' +
                '}';
    }

}