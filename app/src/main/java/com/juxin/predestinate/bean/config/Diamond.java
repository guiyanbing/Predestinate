package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 钻石在线配置
 * Created by ZRP on 2017/4/18.
 */
public class Diamond extends BaseData {

    private int num;        //钻石数
    private int cost;       //价格
    private int pid;        //产品ID
    private String name;    //产品名称

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setNum(jsonObject.optInt("num"));
        this.setCost(jsonObject.optInt("cost"));
        this.setPid(jsonObject.optInt("pid"));
        this.setName(jsonObject.optString("name"));
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Diamond{" +
                "num=" + num +
                ", cost=" + cost +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                '}';
    }
}
