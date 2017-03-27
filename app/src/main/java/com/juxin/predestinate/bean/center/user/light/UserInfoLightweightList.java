package com.juxin.predestinate.bean.center.user.light;

import android.text.TextUtils;

import com.juxin.predestinate.bean.net.BaseData;

import java.util.ArrayList;

/**
 * 简略个人资料列表
 * Created by Kind on 16/4/7.
 */
public class UserInfoLightweightList extends BaseData {

    private ArrayList<UserInfoLightweight> lightweightLists = null;

    @Override
    public void parseJson(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr))
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(getJsonObject(jsonStr).optJSONArray("list"), UserInfoLightweight.class);
    }

    public ArrayList<UserInfoLightweight> getUserInfos() {
        for (UserInfoLightweight userInfoLightweight : lightweightLists) {
            if (userInfoLightweight.getUid() == 0) {
                lightweightLists.remove(userInfoLightweight);
            }
        }
        return lightweightLists;
    }
}
