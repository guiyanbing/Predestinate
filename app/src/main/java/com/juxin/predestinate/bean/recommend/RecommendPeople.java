package com.juxin.predestinate.bean.recommend;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 推荐的人解析类
 * Created YAO on 2017/4/10.
 */

public class RecommendPeople extends BaseData {
    private long uid;//用户uid
    private boolean is_sayhi;//是否打过招呼
    private String tm;//推荐的时间戳

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean is_sayhi() {
        return is_sayhi;
    }

    public void setIs_sayhi(boolean is_sayhi) {
        this.is_sayhi = is_sayhi;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.uid = jsonObject.optLong("uid");
        this.is_sayhi = jsonObject.optBoolean("is_sayhi");
        this.tm = jsonObject.optString("tm");
    }
}
