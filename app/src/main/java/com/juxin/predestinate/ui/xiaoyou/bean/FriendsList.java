package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 小友首页最近互动列表
 * Created by zm on 17/3/20.
 */
public class FriendsList extends BaseData {
    private List arr_frends;

    public List getArr_frends() {
        return arr_frends;
    }

    @Override
    public void parseJson(String s) {
        arr_frends = getBaseDataList(getJsonObject(s).optJSONArray("list"), FriendInfo.class);
    }

    public static class FriendInfo extends BaseFriendInfo {

        private long uid;//用户ID
        private String avatar;//头像地址
        private String describe;//信息描述
        private int isVip;//是否为vip
        private int isHavaDynamic;//是否有动态
        private int isUpdatePhoto;//是否有图片动态
        private int intimacy;//24 小时亲密度  (男性用户)
        private double income;//24 小时收益	 (女性用户)
        private int gender; // 性别 1男2女
        private int type = 0;//type=0为亲密互动信息，type=1为head条目信息
        private int icon;//图片资源

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setUid(jsonObject.optLong("uid"));
            this.setIntimacy(jsonObject.optInt("intimate"));
            this.setIncome(jsonObject.optDouble("income"));
            //待定
            this.setAvatar(jsonObject.optString("avatar"));
            this.setGender(jsonObject.optInt("gender"));
            //            this.setUid(jsonObject.optLong("uid"));
            //            this.setAvatar(jsonObject.optString("avatar"));
            //            this.setNickname(jsonObject.optString("nickname"));
            //            this.setGender(jsonObject.optInt("gender"));
            //            this.setScore(jsonObject.optLong("score"));
            //            this.setExp(jsonObject.optLong("exp"));
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getIsVip() {
            return isVip;
        }

        public void setIsVip(int isVip) {
            this.isVip = isVip;
        }

        public int getIsHavaDynamic() {
            return isHavaDynamic;
        }

        public void setIsHavaDynamic(int isHavaDynamic) {
            this.isHavaDynamic = isHavaDynamic;
        }

        public int getIsUpdatePhoto() {
            return isUpdatePhoto;
        }

        public void setIsUpdatePhoto(int isUpdatePhoto) {
            this.isUpdatePhoto = isUpdatePhoto;
        }

        public int getIntimacy() {
            return intimacy;
        }

        public void setIntimacy(int intimacy) {
            this.intimacy = intimacy;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        @Override
        public String toString() {
            return "RankList{" +
                    "uid=" + uid +
                    ", avatar=" + avatar +
                    ", nickname=" + nickname +
                    ", gender=" + gender +
                    //                    ", score=" + score +
                    //                    ", exp=" + exp +
                    '}';
        }
    }
}