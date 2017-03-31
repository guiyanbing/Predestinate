package com.juxin.predestinate.module.local.msgview.smile;

import com.juxin.mumu.bean.net.BaseData;

import org.json.JSONObject;

/**
 * Created by Kind on 2017/3/31.
 */

public class GiftItem extends BaseData {

    private int gid;
    private String name;
    private String info;
    private String img;
    private String res;
    private int price;
    private int earn;
    private int level;


    @Override
    public void parseJson(String s) {
        JSONObject json = getJsonObject(s);

        gid = json.optInt("gid");
        name = json.optString("n");
        info = json.optString("info");
        img = json.optString("img");
        res = json.optString("res");
        price = json.optInt("price");
        earn = json.optInt("earn");
        level = json.optInt("level");
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEarn() {
        return earn;
    }

    public void setEarn(int earn) {
        this.earn = earn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}

