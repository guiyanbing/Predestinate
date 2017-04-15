package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * vip权限说明
 * Created by ZRP on 2017/4/14.
 */
public class VipAuthority extends BaseData {

    private String head;            // vip支付购买页面图片
    private List<String> male;      // vip男权限
    private List<String> female;    // vip女权限

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setHead(jsonObject.optString("head"));
        JSONArray maleArray = getJsonArray(jsonObject.optString("male"));
        JSONArray femaleArray = getJsonArray(jsonObject.optString("female"));
        male = new ArrayList<>();
        female = new ArrayList<>();
        for (int i = 0; maleArray != null && i < maleArray.length(); i++) {
            male.add(maleArray.optString(i));
        }
        for (int i = 0; femaleArray != null && i < femaleArray.length(); i++) {
            female.add(femaleArray.optString(i));
        }
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public List<String> getMale() {
        return male;
    }

    public void setMale(List<String> male) {
        this.male = male;
    }

    public List<String> getFemale() {
        return female;
    }

    public void setFemale(List<String> female) {
        this.female = female;
    }

    @Override
    public String toString() {
        return "VipAuthority{" +
                "head='" + head + '\'' +
                ", male=" + male +
                ", female=" + female +
                '}';
    }
}
