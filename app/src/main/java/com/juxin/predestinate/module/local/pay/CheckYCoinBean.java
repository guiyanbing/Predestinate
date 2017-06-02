package com.juxin.predestinate.module.local.pay;

import com.juxin.predestinate.bean.net.BaseData;
import org.json.JSONObject;

/**
 * Created by Kind on 2017/6/2.
 */

public class CheckYCoinBean extends BaseData {

    private boolean isOk = false;
    private String msg;
    private int y;
    private String touid;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        String status = jsonObject.optString("status");
        if ("success".equals(status)) {
            this.setOk(true);
            this.setY(jsonObject.optInt("y"));
            this.setTouid(jsonObject.optString("touid"));
        }
    }
}
