package com.juxin.predestinate.bean.center.user;

import android.os.Parcel;

import org.json.JSONObject;

/**
 * 用户详细信息
 */
public class UserDetail extends UserInfo {

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);
        super.parseJson(s);

        if (!jsonObject.isNull("describe")) {

        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public UserDetail() {
    }

    protected UserDetail(Parcel in) {
        super(in);
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
