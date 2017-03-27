package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;
import com.juxin.predestinate.bean.net.BaseData;

/**
 * 游戏信息列表
 * Created by Su on 2017/3/27.
 */

public class Game extends BaseData implements Parcelable{
    private String icon;  // 游戏图标
    private String name;  // 游戏名字

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setIcon(jsonObject.optString("icon"));
        this.setName(jsonObject.optString("name"));
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeString(this.name);
    }

    public Game() {
    }

    protected Game(Parcel in) {
        this.icon = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
