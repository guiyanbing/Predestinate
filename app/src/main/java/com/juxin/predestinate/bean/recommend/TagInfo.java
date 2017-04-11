package com.juxin.predestinate.bean.recommend;


import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 标签信息(用于筛选推荐的人)
 * Created YAO on 2017/4/6.
 */

public class TagInfo  extends BaseData implements Parcelable {

    private String tagName;//标签名字
    private int tagID;//标签id
    private int position;
    private int tagType = 0;//tag类型 0印象tag，1地区，2年龄

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.tagName=jsonObject.optString("name");
        this.tagID= jsonObject.optInt("id");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tagName);
        dest.writeInt(this.tagID);
        dest.writeInt(this.position);
        dest.writeInt(this.tagType);
    }

    public TagInfo() {
    }

    protected TagInfo(Parcel in) {
        this.tagName = in.readString();
        this.tagID = in.readInt();
        this.position = in.readInt();
        this.tagType = in.readInt();
    }

    public static final Creator<TagInfo> CREATOR = new Creator<TagInfo>() {
        @Override
        public TagInfo createFromParcel(Parcel source) {
            return new TagInfo(source);
        }

        @Override
        public TagInfo[] newArray(int size) {
            return new TagInfo[size];
        }
    };
}
