package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.net.BaseData;

/**
 * Created by zm on 2017/3/28
 */
public abstract class BaseFriendInfo extends BaseData {

    protected String nickname = "#";//昵称
    protected String sortKey;
    protected UserInfoLightweight mUserInfoLightweight;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public UserInfoLightweight getUserInfoLightweight() {
        return mUserInfoLightweight;
    }

    public void setUserInfoLightweight(UserInfoLightweight userInfoLightweight) {
        mUserInfoLightweight = userInfoLightweight;
    }
}
