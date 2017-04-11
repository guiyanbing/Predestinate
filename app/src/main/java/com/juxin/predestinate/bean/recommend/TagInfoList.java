package com.juxin.predestinate.bean.recommend;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统标签list
 * Created YAO on 2017/4/10.
 */

public class TagInfoList extends BaseData implements Parcelable {
    private List<TagInfo> tagInfos = new ArrayList<>();

    public List<TagInfo> getTagInfos() {
        return tagInfos == null ? new ArrayList<TagInfo>() : tagInfos;
    }

    public void setTagInfos(List<TagInfo> tagInfos) {
        this.tagInfos = tagInfos;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        JSONArray jsonArray = jsonObject.optJSONArray("list");
        if (jsonArray == null) return;
        TagInfo tagInfo;
        for (int i = 0; i < jsonArray.length(); i++) {
            tagInfo = new TagInfo();
            tagInfo.parseJson(jsonArray.optString(i));
            tagInfos.add(tagInfo);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.tagInfos);
    }

    public TagInfoList() {
    }

    protected TagInfoList(Parcel in) {
        this.tagInfos = in.createTypedArrayList(TagInfo.CREATOR);
    }

    public static final Creator<TagInfoList> CREATOR = new Creator<TagInfoList>() {
        @Override
        public TagInfoList createFromParcel(Parcel source) {
            return new TagInfoList(source);
        }

        @Override
        public TagInfoList[] newArray(int size) {
            return new TagInfoList[size];
        }
    };
}
