package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 私密相册列表
 * Created by Su on 2017/3/27.
 */

public class SecretPhoto extends BaseData implements Parcelable{
    private String coverUrl; // 封面url
    private long id;         // 相片id
    private long time;       // 发布时间
    private int totalNum;    // 每组数量
    private int viewTimes;   // 看过次数

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setId(jsonObject.optLong("id"));
        this.setCoverUrl(jsonObject.optString("cover"));
        this.setTime(jsonObject.optLong("tm"));
        this.setTotalNum(jsonObject.optInt("total"));
        this.setViewTimes(jsonObject.optInt("view"));
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coverUrl);
        dest.writeLong(this.id);
        dest.writeLong(this.time);
        dest.writeInt(this.totalNum);
        dest.writeInt(this.viewTimes);
    }

    public SecretPhoto() {
    }

    protected SecretPhoto(Parcel in) {
        this.coverUrl = in.readString();
        this.id = in.readLong();
        this.time = in.readLong();
        this.totalNum = in.readInt();
        this.viewTimes = in.readInt();
    }

    public static final Parcelable.Creator<SecretPhoto> CREATOR = new Parcelable.Creator<SecretPhoto>() {
        @Override
        public SecretPhoto createFromParcel(Parcel source) {
            return new SecretPhoto(source);
        }

        @Override
        public SecretPhoto[] newArray(int size) {
            return new SecretPhoto[size];
        }
    };
}
