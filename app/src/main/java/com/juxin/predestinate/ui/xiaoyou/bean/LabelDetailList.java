package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 指定标签详情列表
 * Created by zm on 17/3/20.
 */
public class LabelDetailList extends BaseData {

    private List arrFriend;

    public List getArrFriend() {
        return arrFriend;
    }

    @Override
    public void parseJson(String s) {
        arrFriend = getBaseDataList(getJsonObject(s).optJSONArray(""), LabelFriendInfo.class);
    }

    public static class LabelFriendInfo extends BaseData {
        private long uid;//用户ID
        private String avatar;//头像地址
        private String nickname;//昵称
        private int isVip;//是否为vip
        private int intimacy;//亲密度
        private int gender; // 性别 1男2女

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            //            this.setUid(jsonObject.optLong("uid"));
            //            this.setAvatar(jsonObject.optString("avatar"));
            //            this.setNickname(jsonObject.optString("nickname"));
            //            this.setGender(jsonObject.optInt("gender"));
            //            this.setScore(jsonObject.optLong("score"));
            //            this.setExp(jsonObject.optLong("exp"));
        }

        @Override
        public String toString() {
            return "RankList{" +
                    //                    "uid=" + uid +
                    //                    ", avatar=" + avatar +
                    //                    ", nickname=" + nickname +
                    //                    ", gender=" + gender +
                    //                    ", score=" + score +
                    //                    ", exp=" + exp +
                    '}';
        }
    }
}
