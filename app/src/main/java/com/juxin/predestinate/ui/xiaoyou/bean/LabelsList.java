package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 标签列表
 * Created by zm on 17/3/20.
 */
public class LabelsList extends BaseData {

    private List arr_labels;

    public List getArr_labels() {
        return arr_labels;
    }

    @Override
    public void parseJson(String s) {
        //        this.setPageInfo(getPageInfo(getJsonObject(s), RankInfor.class));
        //        RanksList = (ArrayList) getBaseDataList(getJsonObject(s).optJSONArray("players"), RankInfor.class);
        arr_labels = getBaseDataList(getJsonObject(s).optJSONArray(""), LabelInfo.class);
    }

    public static class LabelInfo extends BaseData {
        private long id;
        private String labelName;
        private int num;

        //        private long uid;//用户ID
        //        private String avatar;//头像地址
        //        private String nickname;//昵称
        //        private int isVip;//是否为vip
        //        private int isHavaDynamic;//是否有动态
        //        private int isUpdatePhoto;//是否有图片动态
        //        private int intimacy;//亲密度
        //        private int gender; // 性别 1男2女

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setId(jsonObject.optLong("id"));
            //            this.setUid(jsonObject.optLong("uid"));
            //            this.setAvatar(jsonObject.optString("avatar"));
            //            this.setNickname(jsonObject.optString("nickname"));
            //            this.setGender(jsonObject.optInt("gender"));
            //            this.setScore(jsonObject.optLong("score"));
            //            this.setExp(jsonObject.optLong("exp"));
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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
