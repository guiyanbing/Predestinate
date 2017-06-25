package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;

import com.juxin.library.log.PSP;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详细信息
 */
public class UserDetail extends UserInfo {

    private List<UserPhoto> userPhotos = new ArrayList<>();
    private List<UserVideo> userVideos = new ArrayList<>();
    private UserChatInfo chatInfo = new UserChatInfo();
    private int voice = 1;          //1为开启语音，0为关闭
    private int videopopularity;    // 私密视频人气值

    private int unlock_ycoin;//控制Y币开关
    private int unlock_vip;//控制VIP开关

    @Override
    public void parseJson(String s) {
        String jsonData = getJsonObject(s).optString("res");
        JSONObject jsonObject = getJsonObject(jsonData);

        // 详细信息
        if (!jsonObject.isNull("userDetail")) {
            String json = jsonObject.optString("userDetail");
            super.parseJson(json);
        }

        // 用户相册
        if (!jsonObject.isNull("myPhoto")) {
            this.userPhotos = (List<UserPhoto>) getBaseDataList(jsonObject.optJSONArray("myPhoto"), UserPhoto.class);
        }

        // 开启语音状态
        if (!jsonObject.isNull("voice")) {
            this.voice = jsonObject.optInt("voice");
        }

        // 音视频状态
        if(!jsonObject.isNull("videochatconfig")) {
            this.chatInfo.parseJson(jsonObject.optString("videochatconfig"));
        }

        // -------- 他人 -----
        // 视频列表
        if (!jsonObject.isNull("videolist")) {
            this.userVideos = (List<UserVideo>) getBaseDataList(jsonObject.optJSONArray("videolist"), UserVideo.class);
        }

        // 视频人气值
        if (!jsonObject.isNull("videopopularity")){
            this.videopopularity = jsonObject.optInt("videopopularity");
        }

        if (!jsonObject.isNull("unlock_ycoin")){
            this.setUnlock_ycoin(jsonObject.optInt("unlock_ycoin"));
        }

        if (!jsonObject.isNull("unlock_vip")){
            this.setUnlock_vip(jsonObject.optInt("unlock_vip"));
        }
    }

    public UserChatInfo getChatInfo() {
        return chatInfo;
    }

    public List<UserVideo> getUserVideos() {
        return userVideos;
    }

    public List<UserPhoto> getUserPhotos() {
        return userPhotos;
    }

    public int getVoice() {
        return voice;
    }

    public int getDiamondsSum() {
        return PSP.getInstance().getInt("diamondsSum" + uid, 0);
    }

    public void setDiamondsSum(int diamondsSum) {
        PSP.getInstance().put("diamondsSum" + uid, diamondsSum);
    }

    public int getVideopopularity() {
        return videopopularity;
    }


    public int getUnlock_ycoin() {
        return unlock_ycoin;
    }

    public boolean isUnlock_ycoin() {
        return unlock_ycoin == 1;
    }

    public void setUnlock_ycoin(int unlock_ycoin) {
        this.unlock_ycoin = unlock_ycoin;
    }

    public int getUnlock_vip() {
        return unlock_vip;
    }

    public boolean isUnlock_vip() {
        return unlock_vip == 1;
    }

    public void setUnlock_vip(int unlock_vip) {
        this.unlock_vip = unlock_vip;
    }

    public UserDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.userPhotos);
        dest.writeTypedList(this.userVideos);
        dest.writeParcelable(this.chatInfo, flags);
        dest.writeInt(this.voice);
        dest.writeInt(this.videopopularity);
        dest.writeInt(this.unlock_ycoin);
        dest.writeInt(this.unlock_vip);
    }

    protected UserDetail(Parcel in) {
        super(in);
        this.userPhotos = in.createTypedArrayList(UserPhoto.CREATOR);
        this.userVideos = in.createTypedArrayList(UserVideo.CREATOR);
        this.chatInfo = in.readParcelable(UserChatInfo.class.getClassLoader());
        this.voice = in.readInt();
        this.videopopularity = in.readInt();
        this.unlock_ycoin = in.readInt();
        this.unlock_vip = in.readInt();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel source) {
            return new UserDetail(source);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };
}
