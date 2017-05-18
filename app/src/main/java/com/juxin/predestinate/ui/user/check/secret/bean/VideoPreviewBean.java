package com.juxin.predestinate.ui.user.check.secret.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 视频详细信息
 */
public class VideoPreviewBean extends BaseData implements Parcelable {
    public String id;
    public String uid;
    /**
     * 视频地址
     */
    public String videoUrl;
    /**
     * 缩略图地址
     */
    public String previewUrl;
    /**
     * 观看次数
     */
    public int videoTimes;
    /**
     * 时长(秒)
     */
    public int duration;
    /**
     * 价格（钻石）
     */
    public int cost;
    /**
     * 礼物ID
     */
    public int giftId;
    /**
     * 礼物数量
     */
    public int giftNum;
    /**
     * 创建时间
     */
    public String createTime;
    /**
     * 是否可看  1 不可看 2 可看
     */
    public int open;

    @Override
    public void parseJson(String jsonStr) {
        JSONObject obj = getJsonObject(jsonStr);

        this.id = obj.optString("id");
        this.uid = obj.optString("uid");
        this.videoUrl = obj.optString("video");
        this.previewUrl = obj.optString("pic");
        this.duration = obj.optInt("duration");
        this.videoTimes = obj.optInt("viewtimes");
        this.cost = obj.optInt("cost");
        this.giftId = obj.optInt("giftid");
        this.giftNum = obj.optInt("giftnum");
        this.createTime = obj.optString("createTime");
        this.open = obj.optInt("open");
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public int getVideoTimes() {
        return videoTimes;
    }

    public int getDuration() {
        return duration;
    }

    public int getCost() {
        return cost;
    }

    public int getGiftId() {
        return giftId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getOpen() {
        return open;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.videoUrl);
        dest.writeString(this.previewUrl);
        dest.writeInt(this.videoTimes);
        dest.writeInt(this.duration);
        dest.writeInt(this.cost);
        dest.writeInt(this.giftId);
        dest.writeInt(this.giftNum);
        dest.writeString(this.createTime);
        dest.writeInt(this.open);
    }

    public VideoPreviewBean() {
    }

    protected VideoPreviewBean(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.videoUrl = in.readString();
        this.previewUrl = in.readString();
        this.videoTimes = in.readInt();
        this.duration = in.readInt();
        this.cost = in.readInt();
        this.giftId = in.readInt();
        this.giftNum = in.readInt();
        this.createTime = in.readString();
        this.open = in.readInt();
    }

    public static final Creator<VideoPreviewBean> CREATOR = new Creator<VideoPreviewBean>() {
        @Override
        public VideoPreviewBean createFromParcel(Parcel source) {
            return new VideoPreviewBean(source);
        }

        @Override
        public VideoPreviewBean[] newArray(int size) {
            return new VideoPreviewBean[size];
        }
    };
}
