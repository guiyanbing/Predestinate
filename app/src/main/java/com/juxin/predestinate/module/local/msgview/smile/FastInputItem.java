package com.juxin.predestinate.module.local.msgview.smile;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/4/17.
 */

public class FastInputItem extends BaseData {

    private String key = null;
    private List<Integer> ids = null;

    @Override
    public void parseJson(String s) {
        JSONObject json = getJsonObject(s);

        key = json.optString("key");

        JSONArray jsonArray = getJsonArray(json.optString("list"));
        ids = new ArrayList();

        for (int i = 0; i < jsonArray.length(); ++i) {
            ids.add(jsonArray.optInt(i));
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
