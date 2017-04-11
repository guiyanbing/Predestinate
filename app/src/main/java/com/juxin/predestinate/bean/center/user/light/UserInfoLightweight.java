package com.juxin.predestinate.bean.center.user.light;

import android.os.Parcel;
import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.detail.UserBasic;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;

import org.json.JSONObject;

/**
 * 个人资料轻量级
 * Created by Kind on 16/3/22.
 */
public class UserInfoLightweight extends UserBasic {
//    {
//        "res": {
//        "list": [
//        {
//            "avatar": "http://image1.yuanfenba.net/uploads/oss/photo/20161108/1478595301708993772.png",
//                "avatar_status": 1,
//                "birthday": "1988-09-09",
//                "city": 110108,
//                "distance": "16",
//                "gender": 2,
//                "height": 166,
//                "is_month": false,
//                "is_online": false,
//                "is_say_hello": false,
//                "is_vip": false,
//                "kf_id": 0,
//                "last_online": 0,
//                "nickname": "小友客服",
//                "nickname_status": 0,
//                "province": 110000,
//                "uid": 9999
//        }
//        ]
//    },
//        "status": "ok",
//            "tm": 1481856586
//    }
    private long time;
    private String infoJson;//存储json


    private boolean isVip;      // 是否是VIP
    private boolean isAuth;      // 是否是认证用户

    public UserInfoLightweight() {
    }

    public UserInfoLightweight(long uid, String infoJson, long time) {
        this.uid = uid;
        this.infoJson = infoJson;
        this.time = time;
    }

    public void parseUserInfoLightweight(UserDetail userInfo) {
        if (userInfo == null)
            return;
    }

    /**
     * 新版本，新接口解析
     */
    @Override
    public void parseJson(String jsonResult) {
        if (TextUtils.isEmpty(jsonResult)) {
            return;
        }

        JSONObject jsonObject = getJsonObject(jsonResult);
        if (!jsonObject.has("is_vip")) {
            setVip(jsonObject.optBoolean("is_vip"));
        }
        if (jsonObject.has("is_auth")) {
            setAuth(jsonObject.optBoolean("is_auth"));
        }

    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.isVip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAuth ? (byte) 1 : (byte) 0);
    }



    protected UserInfoLightweight(Parcel in) {
        super(in);
        this.isVip = in.readByte() != 0;
        this.isAuth = in.readByte() != 0;
    }

    public static final Creator<UserInfoLightweight> CREATOR = new Creator<UserInfoLightweight>() {
        @Override
        public UserInfoLightweight createFromParcel(Parcel source) {
            return new UserInfoLightweight(source);
        }

        @Override
        public UserInfoLightweight[] newArray(int size) {
            return new UserInfoLightweight[size];
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }
}