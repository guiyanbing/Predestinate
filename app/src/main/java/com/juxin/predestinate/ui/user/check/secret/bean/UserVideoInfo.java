package com.juxin.predestinate.ui.user.check.secret.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 私密视频列表
 * Created by sks on 2017/3/10.
 */
public class UserVideoInfo extends BaseData implements Parcelable {
    private int popNum;  // 人气值
    private List<VideoPreviewBean> videoList; // 视频列表

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.popNum = jsonObject.optInt("popnum");

        // 视频列表
        if (!jsonObject.isNull("list")) {
            this.videoList = (List<VideoPreviewBean>) getBaseDataList(jsonObject.optJSONArray("list"), VideoPreviewBean.class);
        }
    }

    public int getPopNum() {
        return popNum;
    }

    public List<VideoPreviewBean> getVideoList() {
        return videoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.popNum);
        dest.writeList(this.videoList);
    }

    public UserVideoInfo() {
    }

    protected UserVideoInfo(Parcel in) {
        this.popNum = in.readInt();
        this.videoList = new ArrayList<>();
        in.readList(this.videoList, VideoPreviewBean.class.getClassLoader());
    }

    public static final Creator<UserVideoInfo> CREATOR = new Creator<UserVideoInfo>() {
        @Override
        public UserVideoInfo createFromParcel(Parcel source) {
            return new UserVideoInfo(source);
        }

        @Override
        public UserVideoInfo[] newArray(int size) {
            return new UserVideoInfo[size];
        }
    };
}
