package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物列表，从在线配置中获取
 * Created by ZRP on 2017/4/14.
 */
public class PresentList extends BaseData {

    private List<Present> presentList = new ArrayList<>();

    @Override
    public void parseJson(String jsonStr) {
        this.setPresentList((ArrayList<Present>) getBaseDataList(getJsonArray(jsonStr), Present.class));
    }

    public List<Present> getPresentList() {
        return presentList;
    }

    public void setPresentList(List<Present> presentList) {
        this.presentList = presentList;
    }

    @Override
    public String toString() {
        return "PresentList{" +
                "presentList=" + presentList +
                '}';
    }
}
