package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 礼物信息列表
 * Created by Su on 2017/3/27.
 */

public class Prize extends BaseData implements Parcelable{
    private String icon;  // 礼物图标
    private int num;      // 礼物数量

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setIcon(jsonObject.optString("icon"));
        this.setNum(jsonObject.optInt("num"));
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeInt(this.num);
    }

    public Prize() {
    }

    protected Prize(Parcel in) {
        this.icon = in.readString();
        this.num = in.readInt();
    }

    public static final Creator<Prize> CREATOR = new Creator<Prize>() {
        @Override
        public Prize createFromParcel(Parcel source) {
            return new Prize(source);
        }

        @Override
        public Prize[] newArray(int size) {
            return new Prize[size];
        }
    };
}
