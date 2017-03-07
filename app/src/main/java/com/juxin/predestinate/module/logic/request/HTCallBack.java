package com.juxin.predestinate.module.logic.request;

import retrofit2.Call;

/**
 * 二次封装的请求实例，代替原始请求实例，以便替换
 * Created by ZRP on 2016/9/26.
 */
public class HTCallBack {

    private Call call = null;

    public HTCallBack() {
        super();
    }

    public HTCallBack(Call call) {
        this.call = call;
    }

    /**
     * 取消请求
     */
    public void cancel() {
        if (call != null && !call.isCanceled()) call.cancel();
    }
}