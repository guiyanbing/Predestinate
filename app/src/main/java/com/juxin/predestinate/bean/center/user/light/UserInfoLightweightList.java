package com.juxin.predestinate.bean.center.user.light;

import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 简略个人资料列表
 * Created by Kind on 16/4/7.
 */
public class UserInfoLightweightList extends BaseData {

    private ArrayList<UserInfoLightweight> lightweightLists = new ArrayList<>();

    @Override
    public void parseJson(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJson ---- jsonStr " + jsonStr);
            JSONArray jsonArray = getJsonObject(jsonStr).optJSONArray("list");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
        }
    }

    public ArrayList<UserInfoLightweight> getUserInfos() {
        Iterator<UserInfoLightweight> infos = lightweightLists.iterator();
        while (infos.hasNext()) {
            if (infos.next().getUid() == 0) {
                infos.remove();
            }
        }
        return lightweightLists;
    }
}
