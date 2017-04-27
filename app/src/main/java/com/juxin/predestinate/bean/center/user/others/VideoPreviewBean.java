package com.juxin.predestinate.bean.center.user.others;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频缩略图
 */
public class VideoPreviewBean {
    public String id;
    public String uid;
    /**
     * 视频地址
     */
    public String videoUrl;
    /**
     *  缩略图地址
     */
    public String previewUrl;
    /**
     *  观看次数
     */
    public int videoTimes;
    /**
     *  时长(秒)
     */
    public int duration;
    /**
     * 价格（钻石）
     */
    public int cost;
    /**
     *  礼物ID
     */
    public int giftId;
    /**
     *  礼物数量
     */
    public int giftNum;
    /**
     * 创建时间
     */
    public String createTime;
    /**
     *  是否可看  1 不可看 2 可看
     */
    public int open;

    public static List<VideoPreviewBean> parseFromJSonArray(String jsonArray){
        List<VideoPreviewBean> videoList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonArray);
            if(array != null){
                int size = array.length();
                for (int i = 0; i < size; i++){
                    JSONObject obj = array.getJSONObject(i);
                    VideoPreviewBean bean = new VideoPreviewBean();
                    bean.id = obj.optString("id");
                    bean.uid = obj.optString("uid");
                    bean.videoUrl = obj.optString("video");
                    bean.previewUrl = obj.optString("pic");
                    bean.duration = obj.optInt("duration");
                    bean.videoTimes = obj.optInt("viewtimes");
                    bean.cost = obj.optInt("cost");
                    bean.giftId = obj.optInt("giftid");
                    bean.giftNum = obj.optInt("giftnum");
                    bean.createTime = obj.optString("createTime");
                    bean.open = obj.optInt("open");
                    videoList.add(bean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoList;
    }
}
