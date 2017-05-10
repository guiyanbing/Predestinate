package com.juxin.predestinate.ui.user.fragment.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户Y币状态信息
 * Created by Su on 2017/5/9.
 */

public class YCoin {
    private String status;
    private String msg;
    private int y;
    private String touid;

    public void parseJson(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            this.status = jsonObject.optString("status");
            if ("success".equals(status)) {
                this.setY(jsonObject.optInt("y"));
                this.setTouid(jsonObject.optString("touid"));
            } else {
                this.setMsg(jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
