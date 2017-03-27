package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 印象标签列表
 * Created by Su on 2017/3/27.
 */

public class Label extends BaseData implements Parcelable {
    private String desc;  // 标签描述
    private long id;      // 标签id

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setDesc(jsonObject.optString("desc"));
        this.setId(jsonObject.optLong("id"));
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.desc);
        dest.writeLong(this.id);
    }

    public Label() {
    }

    protected Label(Parcel in) {
        this.desc = in.readString();
        this.id = in.readLong();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel source) {
            return new Label(source);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };
}
