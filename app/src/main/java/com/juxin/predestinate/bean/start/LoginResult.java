package com.juxin.predestinate.bean.start;

import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import org.json.JSONObject;

/**
 * 登录解析类
 * Created YAO on 2017/4/25.
 */
public class LoginResult extends BaseData {

    private long uid;
    private String nickname;
    private String avatar;
    private int avatar_status;
    private int gender;
    private int group;
    private int ycoin;

    private int miss_info;// 判断是否缺失数据,缺失则继续跳转到用户注册

    /**
     * @return 判断用户是否缺失信息
     */
    public boolean isValidDetailInfo() {
        return miss_info != 1;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonFirst = getJsonObject(jsonStr);
        JSONObject jsonNext = jsonFirst.optJSONObject("user_info");
        if (jsonNext != null) {
            this.setUid(jsonNext.optLong("uid"));
            this.setNickname(jsonNext.optString("nickname"));
            this.setAvatar(jsonNext.optString("avatar"));
            this.setAvatar_status(jsonNext.optInt("avatar_status"));
            this.setGender(jsonNext.optInt("gender"));
            this.setGroup(jsonNext.optInt("group"));
            this.setYcoin(jsonNext.optInt("ycoin"));
            this.setMiss_info(jsonNext.optInt("miss_info"));
        }
        setUserInfo();
    }

    /**
     * 登录成功之后根据返回信息重设个人资料
     */
    public void setUserInfo() {
        UserDetail myInfo = ModuleMgr.getCenterMgr().getMyInfo();
        myInfo.setUid(getUid());
        myInfo.setNickname(getNickname());
        myInfo.setAvatar(getAvatar());
        myInfo.setAvatar_status(getAvatar_status());
        myInfo.setGender(getGender());
        myInfo.setGroup(getGroup());
        myInfo.setYcoin(getYcoin());
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAvatar_status() {
        return avatar_status;
    }

    public void setAvatar_status(int avatar_status) {
        this.avatar_status = avatar_status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getYcoin() {
        return ycoin;
    }

    public void setYcoin(int ycoin) {
        this.ycoin = ycoin;
    }

    public int getMiss_info() {
        return miss_info;
    }

    public void setMiss_info(int miss_info) {
        this.miss_info = miss_info;
    }
}
