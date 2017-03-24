package com.juxin.predestinate.module.logic.request;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * retrofit请求接口封装
 * Created by ZRP on 2016/9/19.
 */
interface RequestAPI {

    // =============get请求============

    @GET
    Call<ResponseBody> executeGetCall(@HeaderMap Map<String, String> headerMap, @Url String url, @QueryMap Map<String, Object> maps);

    @GET
    Call<ResponseBody> executeGetCall(@HeaderMap Map<String, String> headerMap, @Url String url);

    // =============post请求============

    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST
    Call<ResponseBody> executePostCall(@HeaderMap Map<String, String> headerMap, @Url String url, @Body RequestBody str);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> executePostCall(@HeaderMap Map<String, String> headerMap, @Url String url, @FieldMap Map<String, Object> maps);

    @POST
    Call<ResponseBody> executePostCall(@HeaderMap Map<String, String> headerMap, @Url String url);

    // =============文件请求============

    @POST
    Call<ResponseBody> executePostCallUploadCall(@HeaderMap Map<String, String> headerMap, @Url String url, @Body MultipartBody multipartBody);
}