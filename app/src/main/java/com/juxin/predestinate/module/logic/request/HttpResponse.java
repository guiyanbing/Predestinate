package com.juxin.predestinate.module.logic.request;

import com.juxin.predestinate.module.logic.base.BaseData;
import com.juxin.predestinate.module.logic.config.UrlParam;

/**
 * 请求返回体统一处理
 * Created by ZRP on 2017/3/8.
 */
public class HttpResponse<T extends BaseData> extends BaseData {

    private UrlParam urlParam;
    private T data;                 //实际返回数据

    public HttpResponse(UrlParam urlParam) {
        this.urlParam = urlParam;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public void parseJson(String jsonStr) {
        //TODO 基础解析
    }
}
