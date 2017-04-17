package com.juxin.predestinate.ui.xiaoyou.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
        arr_labels = getBaseDataList(getJsonObject(s).optJSONArray("list"), LabelInfo.class);
    }

    public static class LabelInfo extends BaseData {

        private long id;
        private String labelName;
        private int num;
        private List<Long> list = new ArrayList<>();

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setId(jsonObject.optLong("id"));
            this.setLabelName(jsonObject.optString("desc"));
            if (!jsonObject.isNull("list")) {
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    this.list.add(jsonArray.optLong(i));
                }
            }
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

        public List<Long> getList() {
            return list;
        }

        public void setList(List<Long> list) {
            this.list = list;
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
