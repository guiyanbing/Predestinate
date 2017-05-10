package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.List;

/**
 * 在线配置
 * Created by ZRP on 2017/4/14.
 */
public class CommonConfig extends BaseData {

    private String entrance_url;    //暂时为书城地址
    private String extra_url;       //暂时为广场地址
    private int minmoney;           //最小提现金额（分）

    private String push_url;        //活动页面地址
    private int pushshow;           //是否进行活动推送的展示
    private double pushrate;        //活动弹框展示宽高比

    private List<Diamond> diamondList;  //钻石配比列表

    /**
     * @return 是否展示活动弹窗
     */
    public boolean canPushShow() {
        return pushshow == 1;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setEntrance_url(jsonObject.optString("entrance_url"));
        this.setExtra_url(jsonObject.optString("extra_url"));
        this.setMinmoney(jsonObject.optInt("mmoney"));

        this.setPush_url(jsonObject.optString("push_url"));
        this.setPushshow(jsonObject.optInt("pushshow"));
        this.setPushrate(jsonObject.optDouble("pushrate"));

        this.setDiamondList((List<Diamond>) getBaseDataList(getJsonArray(jsonObject.optString("diamand")), Diamond.class));
    }

    public String getEntrance_url() {
        return entrance_url;
    }

    public void setEntrance_url(String entrance_url) {
        this.entrance_url = entrance_url;
    }

    public String getExtra_url() {
        return extra_url;
    }

    public void setExtra_url(String extra_url) {
        this.extra_url = extra_url;
    }

    public int getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(int minmoney) {
        this.minmoney = minmoney;
    }

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }

    public int getPushshow() {
        return pushshow;
    }

    public void setPushshow(int pushshow) {
        this.pushshow = pushshow;
    }

    public double getPushrate() {
        return pushrate;
    }

    public void setPushrate(double pushrate) {
        this.pushrate = pushrate;
    }

    public List<Diamond> getDiamondList() {
        return diamondList;
    }

    public void setDiamondList(List<Diamond> diamondList) {
        this.diamondList = diamondList;
    }

    @Override
    public String toString() {
        return "CommonConfig{" +
                "entrance_url='" + entrance_url + '\'' +
                ", extra_url='" + extra_url + '\'' +
                ", minmoney=" + minmoney +
                ", push_url='" + push_url + '\'' +
                ", pushshow=" + pushshow +
                ", pushrate=" + pushrate +
                ", diamondList=" + diamondList +
                '}';
    }
}
