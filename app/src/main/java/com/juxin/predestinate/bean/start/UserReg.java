package com.juxin.predestinate.bean.start;


import com.juxin.predestinate.module.logic.base.BaseData;

import org.json.JSONObject;

/**
 * 注册返回值
 * Created by Kind on 16/8/11.
 */
public class UserReg extends BaseData {

    private long province; //省编码
    private long city; //城市编码
    private String cookie; //cookie
    private long password; //密码
    private long uid;

    private boolean bind; // 三方账号绑定标识

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setProvince(jsonObject.optLong("province"));
        this.setCity(jsonObject.optLong("city"));
        this.setCookie(jsonObject.optString("cookie"));
        this.setPassword(jsonObject.optLong("password"));
        this.setUid(jsonObject.optLong("uid"));

        this.setBind(jsonObject.optBoolean("bind"));
    }

    public long getProvince() {
        return province;
    }

    public void setProvince(long province) {
        this.province = province;
    }

    public long getCity() {
        return city;
    }

    public void setCity(long city) {
        this.city = city;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }
}
