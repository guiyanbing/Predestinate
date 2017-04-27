package com.juxin.predestinate.bean.center.user.others;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sks on 2017/3/10.
 */

public class UserVideoInfo {
    /**
     * 人气值
     */
    public int popNum;

    public ArrayList<VideoPreviewBean> videoList;

    public static UserVideoInfo parseFromJSon(String jsonString){
        UserVideoInfo info = new UserVideoInfo();
        info.videoList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);
            info.popNum = obj.optInt("popnum");
            info.videoList.addAll(VideoPreviewBean.parseFromJSonArray(obj.optString("list")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }
}
