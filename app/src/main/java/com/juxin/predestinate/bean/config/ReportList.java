package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 举报项列表
 * Created by ZRP on 2017/4/14.
 */
public class ReportList extends BaseData {

    private List<Report> reportList = new ArrayList<>();

    @Override
    public void parseJson(String jsonStr) {
        this.setReportList((ArrayList<Report>) getBaseDataList(getJsonArray(jsonStr), Report.class));
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    @Override
    public String toString() {
        return "ReportList{" +
                "reportList=" + reportList +
                '}';
    }

    // ------------------------------静态内部类-------------------------------

    /**
     * 举报项
     */
    public static class Report extends BaseData {

        private int id;         // 举报项id
        private String desc;    // 举报项说明

        @Override
        public void parseJson(String jsonStr) {
            JSONObject jsonObject = getJsonObject(jsonStr);
            this.setId(jsonObject.optInt("id"));
            this.setDesc(jsonObject.optString("desc"));
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "Report{" +
                    "id=" + id +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
