package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * Created by Su on 2017/6/23.
 */

public class UserChatInfo extends BaseData implements Parcelable{

    private int status;
    private String imgUrl;
    private String videoUrl;
    private int videoPrice;
    private int audioPrice;
    private int videoChat;
    private int audioChat;

    @Override
    public void parseJson(String jsonStr) {
        JSONObject object = getJsonObject(jsonStr);

        this.videoChat = object.optInt("videochat");
        this.audioChat = object.optInt("audiochat");

        if (!object.isNull("videoverify")){
            String json = object.optString("videoverify");
            JSONObject obj = getJsonObject(json);
            this.status = obj.optInt("status");
            this.imgUrl = obj.optString("imgurl");
            this.videoUrl = obj.optString("videourl");
            this.videoPrice = obj.optInt("videoprice");
            this.audioPrice = obj.optInt("audioprice");
        }
    }

    public int getStatus() {
        return status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getVideoPrice() {
        return videoPrice;
    }

    public int getAudioPrice() {
        return audioPrice;
    }

    public int getVideoChat() {
        return videoChat;
    }

    public int getAudioChat() {
        return audioChat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.imgUrl);
        dest.writeString(this.videoUrl);
        dest.writeInt(this.videoPrice);
        dest.writeInt(this.audioPrice);
        dest.writeInt(this.videoChat);
        dest.writeInt(this.audioChat);
    }

    public UserChatInfo() {
    }

    protected UserChatInfo(Parcel in) {
        this.status = in.readInt();
        this.imgUrl = in.readString();
        this.videoUrl = in.readString();
        this.videoPrice = in.readInt();
        this.audioPrice = in.readInt();
        this.videoChat = in.readInt();
        this.audioChat = in.readInt();
    }

    public static final Creator<UserChatInfo> CREATOR = new Creator<UserChatInfo>() {
        @Override
        public UserChatInfo createFromParcel(Parcel source) {
            return new UserChatInfo(source);
        }

        @Override
        public UserChatInfo[] newArray(int size) {
            return new UserChatInfo[size];
        }
    };
}
