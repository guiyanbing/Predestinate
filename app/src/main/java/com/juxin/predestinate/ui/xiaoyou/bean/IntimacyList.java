package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签列表
 * Created by zm on 17/3/20.
 */
public class IntimacyList extends BaseData {

    private List arr_intimacys;

    public List getArr_labels() {
        return arr_intimacys;
    }

    @Override
    public void parseJson(String s) {
        arr_intimacys = getBaseDataList(getJsonObject(s).optJSONArray("list"), IntimacyInfo.class);
    }

    public static class IntimacyInfo extends BaseData {

        private int level;
        private String labelName;
        private int experience;
        private int num;
        private List<SimpleFriendsList.SimpleFriendInfo> arr_friends = new ArrayList<>();

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setLevel(jsonObject.optInt("level"));
            this.setLabelName(jsonObject.optString("tip"));
            this.setExperience(jsonObject.optInt("experience"));
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<SimpleFriendsList.SimpleFriendInfo> getArr_friends() {
            return arr_friends;
        }

        public void setArr_friends(List<SimpleFriendsList.SimpleFriendInfo> arr_friends) {
            this.arr_friends = arr_friends;
        }
        public void addFriend(SimpleFriendsList.SimpleFriendInfo friend) {
            this.arr_friends.add(friend);
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
