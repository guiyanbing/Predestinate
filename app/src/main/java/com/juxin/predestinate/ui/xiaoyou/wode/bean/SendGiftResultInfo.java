package com.juxin.predestinate.ui.xiaoyou.wode.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 礼物列表
 * Created by zm on 17/3/20.
 */
public class SendGiftResultInfo extends BaseData {

    private int diamand;
    private String info1;
    private String info2;
    private String msg;

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        //json串解析
        this.setDiamand(jsonObject.optInt("diamand"));
        this.setInfo1(jsonObject.optString("info1"));
        this.setInfo2(jsonObject.optString("info2"));
        this.setMsg(jsonObject.optString("msg"));
    }

    public int getDiamand() {
        return diamand;
    }

    public void setDiamand(int diamand) {
        this.diamand = diamand;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
