package com.juxin.predestinate.bean.my;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 创建日期：2017/6/25
 * 描述:
 * 作者:lc
 */
public class ChatInfo extends BaseData {
    private int diamond;                //我的钻石余额
    private int ycoin;                  //我的Y币余额
    private String status;

    private OtherInfo otherInfo = new OtherInfo();

    @Override
    public void parseJson(String jsonStr) {
        JSONObject job = getJsonObject(jsonStr);
        this.setStatus(job.optString("status"));

        String resStr = job.optString("res");
        JSONObject resJob = getJsonObject(resStr);

        this.setDiamond(resJob.optInt("diamond"));
        this.setYcoin(resJob.optInt("ycoin"));

        this.otherInfo.parseJson(resJob.optJSONObject("otherInfo").toString());
    }

    public boolean isOk() {
        if ("ok".equalsIgnoreCase(status))
            return true;
        else
            return false;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getYcoin() {
        return ycoin;
    }

    public void setYcoin(int ycoin) {
        this.ycoin = ycoin;
    }

    public OtherInfo getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(OtherInfo otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class OtherInfo extends BaseData {
        private int age;
        private String avatar;
        private int avatarstatus;
        private long channel_uid;
        private int city;
        private int gender;
        private int group;
        private int height;
        private boolean is_online;
        private long kf_id;
        private String nickname;
        private int photonum;
        private int province;
        private String remark;
        private int top;
        private String last_online;         //最近上线状态
        private long uid;
        private int net_tp;                 //用户上网方式（2017-06-20）Wifi 1 4G 2 3G/2G 3 其它 4
        private String phone_info;          //用户设备描述

        private VideoConfig videoConfig = new VideoConfig();
        @Override
        public void parseJson(String jsonStr) {
            JSONObject job = getJsonObject(jsonStr);
            this.setAge(job.optInt("age"));
            this.setAvatar(job.optString("avatar"));
            this.setAvatarstatus(job.optInt("avatarstatus"));
            this.setChannel_uid(job.optLong("channel_uid"));
            this.setCity(job.optInt("city"));
            this.setGender(job.optInt("gender"));
            this.setGroup(job.optInt("group"));
            this.setHeight(job.optInt("height"));
            this.setIs_online(job.optBoolean("is_online"));
            this.setKf_id(job.optLong("kf_id"));
            this.setNickname(job.optString("nickname"));
            this.setPhotonum(job.optInt("photonum"));
            this.setProvince(job.optInt("province"));
            this.setRemark(job.optString("remark"));
            this.setTop(job.optInt("top"));
            this.setLast_online(job.optString("last_online"));
            this.setUid(job.optLong("uid"));
            this.setNet_tp(job.optInt("net_tp"));
            this.setPhone_info(job.optString("phone_info"));
            this.videoConfig.parseJson(job.optJSONObject("videochatconfig").toString());
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getAvatarstatus() {
            return avatarstatus;
        }

        public void setAvatarstatus(int avatarstatus) {
            this.avatarstatus = avatarstatus;
        }

        public long getChannel_uid() {
            return channel_uid;
        }

        public void setChannel_uid(long channel_uid) {
            this.channel_uid = channel_uid;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public boolean is_online() {
            return is_online;
        }

        public void setIs_online(boolean is_online) {
            this.is_online = is_online;
        }

        public long getKf_id() {
            return kf_id;
        }

        public void setKf_id(long kf_id) {
            this.kf_id = kf_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getPhotonum() {
            return photonum;
        }

        public void setPhotonum(int photonum) {
            this.photonum = photonum;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public String getLast_online() {
            return last_online;
        }

        public void setLast_online(String last_online) {
            this.last_online = last_online;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public int getNet_tp() {
            return net_tp;
        }

        public void setNet_tp(int net_tp) {
            this.net_tp = net_tp;
        }

        public String getPhone_info() {
            return phone_info;
        }

        public void setPhone_info(String phone_info) {
            this.phone_info = phone_info;
        }

        public VideoConfig getVideoConfig() {
            return videoConfig;
        }

        public void setVideoConfig(VideoConfig videoConfig) {
            this.videoConfig = videoConfig;
        }

        public static class VideoConfig extends BaseData {
            private int videoChat;      // 视频开关 1：开启 0：关闭
            private int voiceChat;      // 音频开关 1：开启 0：关闭
            private int videoVertify;   // 视频认证 1：未通过 3：通过
            private int videoPrice;     // 视频价格
            private int audioPrice;     // 音频价格

            @Override
            public void parseJson(String str) {
                JSONObject jsonObject = getJsonObject(str);

                this.videoChat = jsonObject.optInt("videochat");
                this.voiceChat = jsonObject.optInt("audiochat");
                this.videoVertify = jsonObject.optInt("videoverify");
                this.videoPrice = jsonObject.optInt("videoprice");
                this.audioPrice = jsonObject.optInt("audioprice");
            }

            /**
             * 视频验证是否通过
             */
            public boolean isVerifyVideo() {
                return videoVertify == 3;
            }

            /**
             * 是否可视频
             */
            public boolean isVideoChat() {
                return videoChat == 1;
            }

            /**
             * 是否可音频
             */
            public boolean isVoiceChat() {
                return voiceChat == 1;
            }

            public int getVideoChat() {
                return videoChat;
            }

            public void setVideoChat(int videoChat) {
                this.videoChat = videoChat;
            }

            public int getVoiceChat() {
                return voiceChat;
            }

            public void setVoiceChat(int voiceChat) {
                this.voiceChat = voiceChat;
            }

            public int getVideoVertify() {
                return videoVertify;
            }

            public void setVideoVertify(int videoVertify) {
                this.videoVertify = videoVertify;
            }

            public int getVideoPrice() {
                return videoPrice;
            }

            public void setVideoPrice(int videoPrice) {
                this.videoPrice = videoPrice;
            }

            public int getAudioPrice() {
                return audioPrice;
            }

            public void setAudioPrice(int audioPrice) {
                this.audioPrice = audioPrice;
            }
        }
    }
}
