package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sks on 2017/3/10.
 */

public class UserVideoInfo implements Parcelable{
    /**
     * 人气值
     */
    public int popNum;

    public ArrayList<VideoPreviewBean> videoList;

    public static UserVideoInfo parseFromJSon(String jsonString){
        UserVideoInfo info = new UserVideoInfo();
        info.videoList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);
            info.popNum = obj.optInt("popnum");
            info.videoList.addAll(VideoPreviewBean.parseFromJSonArray(obj.optString("list")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
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
        this.videoList = new ArrayList<VideoPreviewBean>();
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
