package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

/**
 * 用户视频
 * Created by Su on 2017/5/24.
 */

public class UserVideo extends BaseData implements Parcelable{
    @Override
    public void parseJson(String jsonStr) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public UserVideo() {
    }

    protected UserVideo(Parcel in) {
    }

    public static final Creator<UserVideo> CREATOR = new Creator<UserVideo>() {
        @Override
        public UserVideo createFromParcel(Parcel source) {
            return new UserVideo(source);
        }

        @Override
        public UserVideo[] newArray(int size) {
            return new UserVideo[size];
        }
    };
}
