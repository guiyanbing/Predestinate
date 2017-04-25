package com.juxin.predestinate.bean.start;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 手机验证结果解析
 * Created YAO on 2017/4/25.
 */

public class PhoneVerifyResult extends BaseData {
   private int errno;//1账号已绑定2手机已验证过

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonFirst = getJsonObject(jsonStr);
        this.errno= jsonFirst.optInt("errNo");

    }
}
