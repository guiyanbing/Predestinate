package com.juxin.predestinate.ui.xiaoyou.wode.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 标签列表
 * Created by zm on 17/3/20.
 */
public class RedbagList extends BaseData {

    private List redbagLists;
    private String status;
    private double total;

    public boolean isOk() {
        if ("ok".equalsIgnoreCase(status))
            return true;
        else
            return false;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total/100f;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getRedbagLists() {
        return redbagLists;
    }

    @Override
    public void parseJson(String s) {
        this.setTotal(getJsonObject(s).optDouble("total"));
        redbagLists = getBaseDataList(getJsonObject(s).optJSONArray("result"), RedbagInfo.class);
    }

    public static class RedbagInfo extends BaseData {

        private long id;
        private double money;
        private String create_time;
        private int type;
        private int rank;

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            //            "id":"2",                            //红包id
            //                    "money":"300",                      //红包金额(分)
            //                    "create_time":"2016-11-04 10:14:23",// 获得时间
            //                    "type":"1",   //红包类型 1水果游戏红包(旧) 2打擂奖励/排行奖励 3聊天红包
            //                    //4 聊天排名奖励(旧) 5礼物折现  7.8好友邀请,9 视频/语音
            //                    "rank":0,                            //排名(水果 或 聊天)
            this.setId(jsonObject.optLong("id"));
            this.setMoney(jsonObject.optDouble("money"));
            this.setCreate_time(jsonObject.optString("create_time"));
            this.setType(jsonObject.optInt("type"));
            this.setRank(jsonObject.optInt("rank"));
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
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
