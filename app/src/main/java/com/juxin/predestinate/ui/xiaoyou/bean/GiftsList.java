package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 礼物列表
 * Created by zm on 17/3/20.
 */
public class GiftsList extends BaseData {

    private List arr_gifts;

    public List getArr_labels() {
        return arr_gifts;
    }

    @Override
    public void parseJson(String s) {
        arr_gifts = getBaseDataList(getJsonObject(s).optJSONArray("list"), GiftInfo.class);
    }

    public static class GiftInfo extends BaseData {

        private String name;
        private int intimacy;
        private double stone;
        private int icon;

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setName(jsonObject.optString("name"));
            this.setIntimacy(jsonObject.optInt("intimacy"));
            this.setStone(jsonObject.optDouble("stone"));
            this.setStone(jsonObject.optInt("icon"));
//            if (!jsonObject.isNull("list")) {
//                JSONArray jsonArray = jsonObject.optJSONArray("list");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    this.list.add(jsonArray.optLong(i));
//                }
//            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIntimacy() {
            return intimacy;
        }

        public void setIntimacy(int intimacy) {
            this.intimacy = intimacy;
        }

        public double getStone() {
            return stone;
        }

        public void setStone(double stone) {
            this.stone = stone;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
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
