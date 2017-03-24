package com.juxin.predestinate.bean;



import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 用户登录后返回值
 * Created by XY on 17/3/23
 */
public class UserLogin extends BaseData {
    private String cookie;
//TODO 待接入userdetail
//    private UserDetail userDetail = new UserDetail();

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);
        this.setCookie(jsonObject.optString("cookie"));

        //用户资料
        if (!jsonObject.isNull("userinfo")) {
            String userinfo = jsonObject.optString("userinfo");
//            this.userDetail.parseJson(userinfo);
        }

    }


    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

//    public UserDetail getUserDetail() {
//        return userDetail;
//    }

}
