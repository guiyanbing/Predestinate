package com.juxin.predestinate.module.local.chat.msgtype;

import org.json.JSONObject;

/**
 * 视频消息
 * Created by Kind on 2017/4/19.
 */

public class VideoMessage extends BaseMessage {

    private int videoID;//视频聊天ID，一次视频聊天过程的唯一标识
    private int videoTp;//请求类型，1邀请加入聊天，2同意加入  3拒绝或取消 4挂断（挂断可能会收到不止一次）
    private int videoMediaTp;//现在所有消息都会包含此字段 1视频, 2语音
    private int videoVcEscCode;//拒绝或取消 只在vc_tp=3 时生效 1未接通，对方无应答 2接收方拒绝 3发送方取消
    private long videoVcTalkTime;//聊天耗时 单位秒 只在  vc_tp=4挂断时有效
    private String vc_channel_key;

    @Override
    public BaseMessage parseJson(String jsonStr) {
        super.parseJson(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setType(object.optInt("mtp")); //消息类型
        this.setMsgDesc(object.optString("mct")); //消息内容
        this.setTime(object.optLong("mt")); //消息时间 int64
        this.setRu(object.optInt("ru"));
        parseVideoJson(object);
        return this;
    }

    @Override
    public String getJson(BaseMessage message) {
        return super.getJson(message);
    }

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }

    public int getVideoTp() {
        return videoTp;
    }

    public void setVideoTp(int videoTp) {
        this.videoTp = videoTp;
    }

    public int getVideoMediaTp() {
        return videoMediaTp;
    }

    public boolean isVideoMediaTp() {
        return videoMediaTp == 1;
    }

    public void setVideoMediaTp(int videoMediaTp) {
        this.videoMediaTp = videoMediaTp;
    }

    public int getVideoVcEscCode() {
        return videoVcEscCode;
    }

    public void setVideoVcEscCode(int videoVcEscCode) {
        this.videoVcEscCode = videoVcEscCode;
    }

    public long getVideoVcTalkTime() {
        return videoVcTalkTime;
    }

    public void setVideoVcTalkTime(long videoVcTalkTime) {
        this.videoVcTalkTime = videoVcTalkTime;
    }

    public String getVc_channel_key() {
        return vc_channel_key;
    }

    public void setVc_channel_key(String vc_channel_key) {
        this.vc_channel_key = vc_channel_key;
    }


    public VideoMessage(long id, String channelID, String whisperID, long sendID, long msgID, long cMsgID, long specialMsgID,
                       int type, int status, int fStatus, long time, String jsonStr) {
        super(id, channelID, whisperID, sendID, msgID, cMsgID, specialMsgID, type, status, fStatus, time, jsonStr);
        convertJSON(getJsonStr());
    }

    //私聊列表
    public VideoMessage(long id, String userID, String infoJson, int type, int kfID,
                       int status, int ru, long time, String content, int num) {
        super(id, userID, infoJson, type, kfID, status, ru, time, content, num);
        convertJSON(getJsonStr());
    }

    @Override
    public void convertJSON(String jsonStr) {
        super.convertJSON(jsonStr);
        JSONObject object = getJsonObject(jsonStr);
        this.setMsgDesc(object.optString("mct")); //消息内容
        parseVideoJson(object);
    }

    private void parseVideoJson(JSONObject object) {
        this.setVideoID(object.optInt("vc_id"));
        this.setVideoTp(object.optInt("vc_tp"));
        this.setVideoMediaTp(object.optInt("media_tp"));
        this.setVideoVcEscCode(object.optInt("vc_esc_code"));
        this.setVideoVcTalkTime(object.optLong("vc_talk_time"));
        this.setVc_channel_key(object.optString("vc_channel_key"));
    }
}
