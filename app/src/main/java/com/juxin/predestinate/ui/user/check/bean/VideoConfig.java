package com.juxin.predestinate.ui.user.check.bean;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 他人音视频开关配置
 * Created by Su on 2017/5/3.
 */

public class VideoConfig extends BaseData {
    private int videoChat;      // 视频开关 1：开启 0：关闭
    private int voiceChat;      // 音频开关 1：开启 0：关闭
    private int videoVertify;   // 视频认证 1：未开启 3：通过

    @Override
    public void parseJson(String jsonStr) {
        String jsonData = getJsonObject(jsonStr).optString("res");
        JSONObject jsonObject = getJsonObject(jsonData);

        this.videoChat = jsonObject.optInt("videochat");
        this.voiceChat = jsonObject.optInt("audiochat");
        this.videoVertify = jsonObject.optInt("videoverify");
    }

    public int getVideoChat() {
        return videoChat;
    }

    public int getVoiceChat() {
        return voiceChat;
    }

    public int getVideoVertify() {
        return videoVertify;
    }

    @Override
    public String toString() {
        return "VideoConfig{" +
                "videoChat=" + videoChat +
                ", voiceChat=" + voiceChat +
                ", videoVertify=" + videoVertify +
                '}';
    }
}
