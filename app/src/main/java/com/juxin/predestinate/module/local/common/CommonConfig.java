package com.juxin.predestinate.module.local.common;

import com.juxin.predestinate.bean.config.PresentList;
import com.juxin.predestinate.bean.config.ReportList;
import com.juxin.predestinate.bean.config.VipAuthority;
import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 在线配置
 * Created by ZRP on 2017/4/14.
 */
public class CommonConfig extends BaseData {

    private PresentList presentList;
    private VipAuthority vipAuthority;
    private ReportList reportList;

    private String squareHead;          // 广场头部图片url

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        presentList = new PresentList();
        presentList.parseJson(jsonObject.optString("present"));

        this.setSquareHead(getJsonObject(jsonObject.optString("square")).optString("head"));

        vipAuthority = new VipAuthority();
        vipAuthority.parseJson(jsonObject.optString("vipauthority"));

        reportList = new ReportList();
        reportList.parseJson(jsonObject.optString("report_reason"));
    }

    public PresentList getPresentList() {
        return presentList;
    }

    public void setPresentList(PresentList presentList) {
        this.presentList = presentList;
    }

    public VipAuthority getVipAuthority() {
        return vipAuthority;
    }

    public void setVipAuthority(VipAuthority vipAuthority) {
        this.vipAuthority = vipAuthority;
    }

    public String getSquareHead() {
        return squareHead;
    }

    public void setSquareHead(String squareHead) {
        this.squareHead = squareHead;
    }
}
