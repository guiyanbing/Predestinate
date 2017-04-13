package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 小友全部好友列表
 * Created by zm on 17/3/20.
 */
public class FriendsDetailList extends BaseData {
    private List arr_frends;

    public List getArr_frends() {
        return arr_frends;
    }

    @Override
    public void parseJson(String s) {
        arr_frends = getBaseDataList(getJsonObject(s).optJSONArray("list"), FriendDetailInfo.class);
    }

    public FriendDetailInfo getDetailInfo(Long uid){
        if (arr_frends != null){
            int size = arr_frends.size();
            for (int i = 0;i<size;i++){
                FriendDetailInfo info = (FriendDetailInfo)arr_frends.get(i);
                if (info.getUid() == uid){
                    return info;
                }
            }
        }
        return null;
    }

    public static class FriendDetailInfo extends BaseFriendInfo {

        private long uid;//用户ID
        private String avatar;//头像地址
        private String alias;//别名
        private int age;//年龄
        private int gender; // 性别 0-女，1-男
        private String province;//省份
        private String city;//城市
        private boolean isVip;//是否为vip
        private boolean is_auth;//是否认证
        private String signname;//签名


        private String describe;//信息描述
        private int isHavaDynamic;//是否有动态
        private int isUpdatePhoto;//是否有图片动态
        private int intimacy;//24 小时亲密度  (男性用户)
        private double income;//24 小时收益	 (女性用户)
        private int type = 0;//type=0为亲密互动信息，type=1为head条目信息
        private int icon;//图片资源

        @Override
        public void parseJson(String s) {

            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setUid(jsonObject.optLong("uid"));
            this.setAvatar(jsonObject.optString("avatar"));
            this.setNickname(jsonObject.optString("nickname"));
            this.setAlias(jsonObject.optString("alias"));
            this.setAge(jsonObject.optInt("age"));
            this.setGender(jsonObject.optInt("gender"));
            this.setProvince(jsonObject.optString("province"));
            this.setCity(jsonObject.optString("city"));
            this.setIsVip(jsonObject.optBoolean("isVip"));
            this.setIs_auth(jsonObject.optBoolean("is_auth"));
            this.setSignname(jsonObject.optString("signname"));
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

        public boolean getIsVip() {
            return isVip;
        }

        public void setIsVip(boolean isVip) {
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

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public boolean is_auth() {
            return is_auth;
        }

        public void setIs_auth(boolean is_auth) {
            this.is_auth = is_auth;
        }

        public String getSignname() {
            return signname;
        }

        public void setSignname(String signname) {
            this.signname = signname;
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