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

    /**
     * 仅用于获取好友列表的时候的好友总数 其他时候不用
     */
    private int totalcnt;

    /**
     * ref 如果是 true 并且请求的如果非第一页
     * 那么返回来的就是第一页 应该把之前的数据都清掉
     * 把返回的数据作为第一页
     */
    private boolean isRef = false;

    /**
     * 正常解析用户简略信息
     *
     * @param jsonStr
     */
    @Override
    public void parseJson(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJson ---- jsonStr " + jsonStr);
            String jsonData = getJsonObject(jsonStr).optString("res");
            JSONObject object = getJsonObject(jsonData);
            JSONArray jsonArray = object.optJSONArray("list");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
            if (object.has("ref")) {
                setRef(object.optInt("ref") == 1);
            }
        }
    }

    /**
     * 仅用于解析好友列表
     *
     * @param jsonStr
     */
    public void parseJsonFriends(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJsonFriends ---- jsonStr " + jsonStr);
            String jsonData = getJsonObject(jsonStr).optString("res");
            setTotalcnt(getJsonObject(jsonStr).optInt("totalcnt"));
            JSONArray jsonArray = getJsonObject(jsonData).optJSONArray("friends");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
        }
    }

    /**
     * 仅用于解析一键打招呼的用户列表
     *
     * @param jsonStr
     */
    public void parseJsonSayhi(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            PLogger.d("UserInfoLightweightList parseJsonSayhi ---- jsonStr " + jsonStr);
            JSONArray jsonArray = getJsonObject(jsonStr).optJSONArray("content");
            this.lightweightLists = (ArrayList<UserInfoLightweight>) getBaseDataList(jsonArray, UserInfoLightweight.class);
        }
    }

    /**
     * 轻量级个人资料
     *
     * @param jsonObject
     */
    public void parseJsonSummary(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isNull("res")) return;
        JSONArray jsonArray = jsonObject.optJSONObject("res").optJSONArray("content");
        UserInfoLightweight lightweight;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject temp = jsonArray.optJSONObject(i);
            lightweight = new UserInfoLightweight();
            lightweight.parseJson(temp);
            lightweightLists.add(lightweight);
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


    public boolean isRef() {
        return isRef;
    }

    public void setRef(boolean ref) {
        isRef = ref;
    }
}
