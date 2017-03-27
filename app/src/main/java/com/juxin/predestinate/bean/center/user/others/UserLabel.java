package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户标签信息
 * Created by Su on 2017/3/27.
 */

public class UserLabel extends BaseData implements Parcelable{
    private String alias;  // 用户别名
    private List<Label> labelList = new ArrayList<>();

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setAlias(jsonObject.optString("alias"));

        if (!jsonObject.isNull("tags")) {
            this.labelList = (List<Label>) getBaseDataList(jsonObject.optJSONArray("tags"), Label.class);
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Label> getLabelList() {
        return labelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alias);
        dest.writeTypedList(this.labelList);
    }

    public UserLabel() {
    }

    protected UserLabel(Parcel in) {
        this.alias = in.readString();
        this.labelList = in.createTypedArrayList(Label.CREATOR);
    }

    public static final Creator<UserLabel> CREATOR = new Creator<UserLabel>() {
        @Override
        public UserLabel createFromParcel(Parcel source) {
            return new UserLabel(source);
        }

        @Override
        public UserLabel[] newArray(int size) {
            return new UserLabel[size];
        }
    };
}
