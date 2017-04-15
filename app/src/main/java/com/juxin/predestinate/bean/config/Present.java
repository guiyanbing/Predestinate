package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 礼物信息
 * Created by ZRP on 2017/4/14.
 */
public class Present extends BaseData {

    private int id;         // 礼物id
    private String name;    // 名称
    private String icon;    // 图片url
    private int price;      // 价格
    private int intimate;   // 亲密值

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setId(jsonObject.optInt("id"));
        this.setName(jsonObject.optString("name"));
        this.setIcon(jsonObject.optString("icon"));
        this.setPrice(jsonObject.optInt("price"));
        this.setIntimate(jsonObject.optInt("intimate"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIntimate() {
        return intimate;
    }

    public void setIntimate(int intimate) {
        this.intimate = intimate;
    }

    @Override
    public String toString() {
        return "Present{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", price=" + price +
                ", intimate=" + intimate +
                '}';
    }
}
