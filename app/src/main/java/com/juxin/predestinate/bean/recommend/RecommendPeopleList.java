package com.juxin.predestinate.bean.recommend;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐的人list
 * Created YAO on 2017/4/10.
 */

public class RecommendPeopleList extends BaseData {
    private List<RecommendPeople> recommendPeopleList = new ArrayList<RecommendPeople>();

    public List<RecommendPeople> getRecommendPeopleList() {
        return recommendPeopleList == null ? new ArrayList<RecommendPeople>() : recommendPeopleList;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        JSONArray jsonArray = jsonObject.optJSONArray("list");
        if (jsonArray == null) return;
        RecommendPeople recommendPeople;
        for (int i = 0; i < jsonArray.length(); i++) {
            recommendPeople = new RecommendPeople();
            recommendPeople.parseJson(jsonArray.optString(i));
            recommendPeopleList.add(recommendPeople);
        }
    }
}
