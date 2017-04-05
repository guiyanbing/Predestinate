package com.juxin.predestinate.module.logic.request;

import android.text.TextUtils;

import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.util.JsonUtil;

import org.json.JSONObject;

/**
 * 请求返回体统一处理
 * Created by ZRP on 2017/3/8.
 */
public class HttpResponse extends BaseData {

    private UrlParam urlParam;                  //请求信息
    private String responseString = "";         //原始返回串
    private String status = null;               //返回状态，ok表示成功返回数据。
    private BaseData baseData = null;           //返回的数据对象
    private String msg = null;                  //返回的提示消息
    private boolean cache = false;              //当前数据是否来自于缓存

    public HttpResponse(UrlParam urlParam) {
        this.urlParam = urlParam;
    }

    public UrlParam getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(UrlParam urlParam) {
        this.urlParam = urlParam;
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

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    /**
     * @return 原始JSONObject
     */
    public JSONObject getResponseJson() {
        return JsonUtil.getJsonObject(responseString);
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public void parseJson(String jsonStr) {
        responseString = jsonStr;
        JSONObject json = getJsonObject(jsonStr);

        status = json.optString("status");
        msg = json.optString("msg");
        //------请求返回的数据体处理------
        baseData = null;

        if (urlParam != null) {
            baseData = urlParam.getBaseData();
        }
        if (baseData != null) {
            baseData.parseJson(json.optString("res"));
        }
    }
}
