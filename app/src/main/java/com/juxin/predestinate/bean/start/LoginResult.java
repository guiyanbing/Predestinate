package com.juxin.predestinate.bean.start;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 登录解析类
 * Created YAO on 2017/4/25.
 */

public class LoginResult extends BaseData {
    private String nickname;
    private long uid;
    private int miss_info;    // 判断是否缺失数据,缺失则继续跳转到用户注册
    private boolean validDetailInfo;
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getMiss_info() {
        return miss_info;
    }

    public void setMiss_info(int miss_info) {
        this.miss_info = miss_info;
    }

    public boolean isValidDetailInfo() {
        return miss_info != 1;
    }

    public void setValidDetailInfo(boolean validDetailInfo) {
        this.validDetailInfo = validDetailInfo;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonFirst = getJsonObject(jsonStr);
        JSONObject jsonNext = jsonFirst.optJSONObject("user_info");
        if (jsonNext != null) {
            this.nickname = jsonNext.optString("nickname");
            this.uid = jsonNext.optLong("uid");
            this.miss_info = jsonNext.optInt("miss_info");
            this.gender= jsonNext.optInt("gender");
        }

    }
}
