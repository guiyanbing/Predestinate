package com.juxin.predestinate.bean.center.user.detail;

import android.text.TextUtils;

import com.juxin.mumu.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料中的掠夺信息
 * Created by ZRP on 2017/1/5.
 */
public class UserRobbed extends BaseData implements Serializable {

    private int num;
    private List<String> avatars = new ArrayList<>();

    @Override
    public void parseJson(String s) {
        if(TextUtils.isEmpty(s)){
            return;
        }

        JSONObject jsonObject = getJsonObject(s);
        this.setNum(jsonObject.optInt("num"));
        JSONArray usersArray = jsonObject.optJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject rob = getJsonObject(usersArray.optString(i));
            avatars.add(rob.optString("avatar"));
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<String> avatars) {
        this.avatars = avatars;
    }

    @Override
    public String toString() {
        return "UserRobbed{" +
                "num=" + num +
                ", avatars=" + avatars +
                '}';
    }
}
