package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 小友首页最近互动列表
 * Created by zm on 17/3/23.
 */
public class SimpleFriendsList extends BaseData {

    private List arr_frends;

    public List getArr_frends() {
        return arr_frends;
    }

    @Override
    public void parseJson(String s) {
        //        this.setPageInfo(getPageInfo(getJsonObject(s), RankInfor.class));
        //        RanksList = (ArrayList) getBaseDataList(getJsonObject(s).optJSONArray("players"), RankInfor.class);
        arr_frends = getBaseDataList(getJsonObject(s).optJSONArray(""), SimpleFriendInfo.class);
        //        this.setSelf_rank(getJsonObject(s).optLong("self_rank"));
    }

    public static class SimpleFriendInfo extends BaseFriendInfo {
        private long uid;//用户ID
        private String avatar;//头像地址
        private boolean isCheck;//选中状态 为true：选中 ；为false：未选中
        private String sortKey;

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setUid(jsonObject.optLong("uid"));
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

        public boolean isCheck() {
            return isCheck;
        }

        public void setIsCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public String getSortKey() {
            return sortKey;
        }

        public void setSortKey(String sortKey) {
            this.sortKey = sortKey;
        }

        @Override
        public String toString() {
            return "RankList{" +
                    "uid=" + uid +
                    ", avatar=" + avatar +
                    ", nickname=" + nickname +
                    //                    ", gender=" + gender +
                    //                    ", score=" + score +
                    //                    ", exp=" + exp +
                    '}';
        }
    }
}