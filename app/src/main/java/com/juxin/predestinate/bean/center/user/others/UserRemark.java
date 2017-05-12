package com.juxin.predestinate.bean.center.user.others;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 用户设置信息
 * Created by Su on 2017/5/12.
 */
public class UserRemark extends BaseData{
    private String remarkName;  // 备注名称

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);

        this.remarkName = jsonObject.optString("remarkname");
    }

    public String getRemarkName() {
        return remarkName;
    }
}
