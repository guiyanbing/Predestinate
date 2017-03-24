package com.juxin.predestinate.module.logic.request;

import android.text.TextUtils;

import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.config.UrlParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求返回体统一处理
 * Created by ZRP on 2017/3/8.
 */
public class HttpResponse<T extends BaseData> extends BaseData {

    private UrlParam urlParam;
    private T data;                 //实际返回数据

    private String resultStr = "";      //原始返回串
    private String status = null;   //返回状态，ok表示成功返回数据。
    private BaseData baseData = null;   //返回的数据对象
    private JSONObject json = null;     //返回的数据对象，只有baseData没有实例化时，才有保存

    public HttpResponse(UrlParam urlParam) {
        this.urlParam = urlParam;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setOk() {
        this.status = "ok";
    }

    /**
     * 只有status的值为ok时，才返回true
     *
     * @return 网络请求结果
     */
    public boolean isOk() {
        if (TextUtils.isEmpty(status)) return false;
        return "ok".equals(status);
    }

    public void setError() {
        this.status = "error";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BaseData getBaseData() {
        return baseData;
    }

    public void setBaseData(BaseData baseData) {
        this.baseData = baseData;
    }

    public JSONObject getJson() {
        if (json == null) {
            return new JSONObject();
        }
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    /**
     * @return 原始json字符串
     */
    public String getResultStr() {
        return resultStr;
    }

    /**
     * @return 原始JSONObject
     */
    public JSONObject getResultJson() {
        try {
            return new JSONObject(resultStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    @Override
    public void parseJson(String jsonStr) {
        resultStr = jsonStr;
        JSONObject json = getJsonObject(jsonStr);

        status = json.optString("status");
        //------请求返回的数据体处理------
        baseData = null;

        if (urlParam != null) {
            baseData = urlParam.getBaseData();
        }

        if (baseData != null) {
            baseData.parseJson(json.optString("res"));
        } else {
            this.json = json.optJSONObject("res");
        }
    }
}
