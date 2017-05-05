package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 普通消息
 * Created by Kind on 2017/4/18.
 */

public class CommonMessage extends BaseMessage{

    private String voiceUrl;
    private int voiceLen;
    private long voiceUserid;

    private String videoUrl;
    private int videoLen;
    private long videoSize;
    private String videoThumb;
    private int videoWidth;
    private int videoHeight;

    public CommonMessage() {
        super();
    }

    /**
     * 文本消息
     * @param whisperID
     * @param content
     */
    public CommonMessage(String channelID, String whisperID, String content) {
        super(channelID, whisperID);
        this.setContent(content);
        this.setType(BaseMessageType.common.getMsgType());
    }

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);

        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        if(!object.isNull("voice")){
            JSONObject voiceJSON = object.optJSONObject("voice");
            this.setVoiceUrl(voiceJSON.optString("url"));
            this.setVoiceLen(voiceJSON.optInt("len"));
            this.setVoiceUserid(voiceJSON.optLong("voice_userid"));
        }

        if(!object.isNull("video")){
            JSONObject videoJSON = object.optJSONObject("video");
            this.setVideoUrl(videoJSON.optString("url"));
            this.setVideoLen(videoJSON.optInt("len"));
            this.setVideoSize(videoJSON.optInt("size"));
            this.setVideoThumb(videoJSON.optString("thumb"));
            this.setVideoWidth(videoJSON.optInt("width"));
            this.setVideoHeight(videoJSON.optInt("height"));
        }
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public int getVoiceLen() {
        return voiceLen;
    }

    public void setVoiceLen(int voiceLen) {
        this.voiceLen = voiceLen;
    }

    public long getVoiceUserid() {
        return voiceUserid;
    }

    public void setVoiceUserid(long voiceUserid) {
        this.voiceUserid = voiceUserid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getVideoLen() {
        return videoLen;
    }

    public void setVideoLen(int videoLen) {
        this.videoLen = videoLen;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }
}
