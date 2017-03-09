package com.juxin.predestinate.module.logic.request;

import com.juxin.predestinate.module.logic.config.UrlParam;

import java.io.File;
import java.util.Map;

/**
 * 请求时参数
 * Created by ZRP on 2017/3/9.
 */
public class RequestParam {

    public enum CacheType {
        CT_Cache_No,        //不缓存
        CT_Cache_Url,       //用无参数的Url作为缓存的key
        CT_Cache_Params;    //用Url和请求参数作为缓存的key
    }

    public enum RequestType {
        POST, GET;
    }

    private RequestType requestType;                        //请求方式
    private UrlParam urlParam;                              //请求枚举
    private Map<String, String> head_param;                 //请求头map
    private Map<String, Object> get_param;                  //get请求参数
    private Map<String, Object> post_param;                 //post请求参数
    private Map<String, File> file_param;                   //文件上传参数
    private RequestComplete requestCallback;                //请求回调
    private CacheType cacheType = CacheType.CT_Cache_No;    //缓存类型：默认不缓存
    private boolean needEncrypt = false;                    //是否加密：默认不加密

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public UrlParam getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(UrlParam urlParam) {
        this.urlParam = urlParam;
    }

    public Map<String, String> getHead_param() {
        return head_param;
    }

    public void setHead_param(Map<String, String> head_param) {
        this.head_param = head_param;
    }

    public Map<String, Object> getGet_param() {
        return get_param;
    }

    public void setGet_param(Map<String, Object> get_param) {
        this.get_param = get_param;
    }

    public Map<String, Object> getPost_param() {
        return post_param;
    }

    public void setPost_param(Map<String, Object> post_param) {
        this.post_param = post_param;
    }

    public Map<String, File> getFile_param() {
        return file_param;
    }

    public void setFile_param(Map<String, File> file_param) {
        this.file_param = file_param;
    }

    public RequestComplete getRequestCallback() {
        return requestCallback;
    }

    public void setRequestCallback(RequestComplete requestCallback) {
        this.requestCallback = requestCallback;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public boolean isNeedEncrypt() {
        return needEncrypt;
    }

    public void setNeedEncrypt(boolean needEncrypt) {
        this.needEncrypt = needEncrypt;
    }
}
