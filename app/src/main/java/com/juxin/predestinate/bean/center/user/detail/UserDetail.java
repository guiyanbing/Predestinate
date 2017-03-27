package com.juxin.predestinate.bean.center.user.detail;

import android.os.Parcel;

import com.juxin.predestinate.bean.center.user.others.Game;
import com.juxin.predestinate.bean.center.user.others.SecretPhoto;
import com.juxin.predestinate.bean.center.user.others.SecretVideo;
import com.juxin.predestinate.bean.center.user.others.UserLabel;
import com.juxin.predestinate.bean.center.user.others.UserPrize;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详细信息
 */
public class UserDetail extends UserInfo {
    private List<Game> gamesInfo = new ArrayList<>();
    private List<SecretPhoto> secretPhotos = new ArrayList<>();
    private List<SecretVideo> secretVideos = new ArrayList<>();
    private UserPrize userPrize = new UserPrize();
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
            this.secretPhotos = (List<SecretPhoto>) getBaseDataList(jsonObject.optJSONArray("photo"), SecretPhoto.class);
        }

        // 私密视频列表
        if (!jsonObject.isNull("video")) {
            this.secretVideos = (List<SecretVideo>) getBaseDataList(jsonObject.optJSONArray("video"), SecretVideo.class);
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

        // 存储个人信息到本地
        ModuleMgr.getCenterMgr().setMyInfo(s);
    }

    public List<Game> getGamesInfo() {
        return gamesInfo;
    }

    public List<SecretPhoto> getSecretPhotos() {
        return secretPhotos;
    }

    public List<SecretVideo> getSecretVideos() {
        return secretVideos;
    }

    public UserPrize getUserPrize() {
        return userPrize;
    }

    public UserLabel getUserLabel() {
        return userLabel;
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
