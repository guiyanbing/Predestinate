package com.juxin.predestinate.bean.center.user.light;

import android.text.TextUtils;
import com.juxin.library.log.PLogger;
import com.juxin.predestinate.bean.net.BaseData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 简略个人资料列表
 * Created by Kind on 16/4/7.
 */
public class UserInfoLightweightList extends BaseData {

    private ArrayList<UserInfoLightweight> lightweightLists = new ArrayList<>();

    private int totalcnt;

    @Override
    public void parseJson(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJson ---- jsonStr " + jsonStr);
            String jsonData = getJsonObject(jsonStr).optString("res");
            JSONArray jsonArray = getJsonObject(jsonData).optJSONArray("list");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
        }
    }

    public void parseJsonFriends(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJsonFriends ---- jsonStr " + jsonStr);
            String jsonData = getJsonObject(jsonStr).optString("res");
            setTotalcnt(getJsonObject(jsonStr).optInt("totalcnt"));
            JSONArray jsonArray = getJsonObject(jsonData).optJSONArray("friends");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
        }
    }


    public void parseJsonSayhi(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJsonSayhi ---- jsonStr " + jsonStr);
            JSONArray jsonArray = getJsonObject(jsonStr).optJSONArray("content");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
        }
    }

    /**
     * 轻量级个人资料
     * @param jsonStr
     */
    public void parseJsonSummary(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            JSONArray jsonArray = getJsonObject(jsonStr).optJSONArray("content");
            UserInfoLightweight lightweight;
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                lightweight = new UserInfoLightweight();
                lightweight.parseJson(jsonObject);
                lightweightLists.add(lightweight);
            }
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

    public ArrayList<UserInfoLightweight> getLightweightLists() {
        return lightweightLists;
    }

    public void setLightweightLists(ArrayList<UserInfoLightweight> lightweightLists) {
        this.lightweightLists = lightweightLists;
    }

    public int getTotalcnt() {
        return totalcnt;
    }

    public void setTotalcnt(int totalcnt) {
        this.totalcnt = totalcnt;
    }
}
