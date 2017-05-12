package com.juxin.predestinate.bean.my;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 提现记录
 * Created by zm on 17/3/20.
 */
public class WithdrawList extends BaseData {

    private List redbagLists;
    private double total;//红包总额
    private String status;

    public boolean isOk() {
        if ("ok".equalsIgnoreCase(status))
            return true;
        else
            return false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getRedbagLists() {
        return redbagLists;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total/100f;
    }

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);
        if (jsonObject.has("total"))
            this.setTotal(jsonObject.optDouble("total"));
        redbagLists = getBaseDataList(jsonObject.optJSONArray("result"), WithdrawInfo.class);
    }

    public static class WithdrawInfo extends BaseData {

        private long id;//申请id
        private double money;//申请金额(分)
        private String create_time;// 申请时间
        private int status;//提现状态 1 未处理 2 已处理 3 打回修改信息

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setId(jsonObject.optLong("id"));
            this.setMoney(jsonObject.optDouble("money"));
            this.setCreate_time(jsonObject.optString("create_time"));
            this.setStatus(jsonObject.optInt("status"));
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money/100f;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
