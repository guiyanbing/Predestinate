package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;

import com.juxin.predestinate.bean.center.user.others.Game;
import com.juxin.predestinate.bean.center.user.others.SecretMedia;
import com.juxin.predestinate.bean.center.user.others.UserLabel;
import com.juxin.predestinate.bean.center.user.others.UserPrize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详细信息
 */
public class UserDetail extends UserInfo {
    private List<Game> gamesInfo = new ArrayList<>();
    private List<SecretMedia> secretPhotos = new ArrayList<>();
    private List<SecretMedia> secretVideos = new ArrayList<>();
    private UserPrize userPrize = new UserPrize();

    // 自己
    private List<String> impressions = new ArrayList<>();
    private List<String> photoCovers = new ArrayList<>();
    private List<String> videoCovers = new ArrayList<>();

    // 他人
    private UserLabel userLabel = new UserLabel();

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);
        super.parseJson(s);

        // 游戏列表
        if (!jsonObject.isNull("game")) {
            this.gamesInfo = (List<Game>) getBaseDataList(jsonObject.optJSONArray("game"), Game.class);
        }

        // 私密相册列表
        if (!jsonObject.isNull("photo")) {
            this.secretPhotos = (List<SecretMedia>) getBaseDataList(jsonObject.optJSONArray("photo"), SecretMedia.class);
        }

        // 私密视频列表
        if (!jsonObject.isNull("video")) {
            this.secretVideos = (List<SecretMedia>) getBaseDataList(jsonObject.optJSONArray("video"), SecretMedia.class);
        }

        // 礼物信息
        if (!jsonObject.isNull("prize")) {
            String prize = jsonObject.optString("prize");
            this.userPrize.parseJson(prize);
        }

        // 印象标签
        if (!jsonObject.isNull("remarks")) {
            String remarks = jsonObject.optString("remarks");
            this.userLabel.parseJson(remarks);
        }

        // ========================= 自己独有 ==============================
        // 最新三个印象说明
        if (!jsonObject.isNull("impressions")) {
            this.impressions = getStringList(jsonObject.optJSONArray("impressions"));
        }

        // 最新四组相册封面
        if (!jsonObject.isNull("photos")) {
            this.photoCovers = getStringList(jsonObject.optJSONArray("photos"));
        }

        // 最新四组视频封面
        if (!jsonObject.isNull("videos")) {
            this.photoCovers = getStringList(jsonObject.optJSONArray("videos"));
        }
    }

    public List<String> getImpressions() {
        return impressions;
    }

    public List<String> getPhotoCovers() {
        return photoCovers;
    }

    public List<String> getVideoCovers() {
        return videoCovers;
    }

    public List<Game> getGamesInfo() {
        return gamesInfo;
    }

    public List<SecretMedia> getSecretPhotos() {
        return secretPhotos;
    }

    public List<SecretMedia> getSecretVideos() {
        return secretVideos;
    }

    public UserPrize getUserPrize() {
        return userPrize;
    }

    public UserLabel getUserLabel() {
        return userLabel;
    }

    // 字符串数组解析
    private List<String> getStringList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
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
