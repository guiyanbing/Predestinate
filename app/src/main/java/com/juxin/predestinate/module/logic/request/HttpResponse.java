package com.juxin.predestinate.module.logic.request;

import com.juxin.predestinate.bean.net.BaseData;
import com.juxin.predestinate.module.logic.config.UrlParam;

/**
 * 请求返回体统一处理
 * Created by ZRP on 2017/3/8.
 */
public class HttpResponse<T extends BaseData> extends BaseData {

    private UrlParam urlParam;      //请求实例
    private String responseString;  //原字符串
    private T data;                 //实际返回数据

    public HttpResponse(UrlParam urlParam) {
        this.urlParam = urlParam;
    }

    public UrlParam getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(UrlParam urlParam) {
        this.urlParam = urlParam;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public void parseJson(String jsonStr) {
        this.setResponseString(jsonStr);
        //TODO 基础解析
    }
}
