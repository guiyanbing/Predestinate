package com.xiaochen.android.fate_it.module.request;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * retrofit请求接口封装
 * Created by ZRP on 2016/9/19.
 */
interface RequestAPI {

    // =============get请求============

    @GET
    Observable<ResponseBody> executeGet(@Url String url, @QueryMap Map<String, Object> maps);

    @GET
    Observable<ResponseBody> executeGet(@Url String url);

    @GET
    Call<ResponseBody> executeGetCall(@Url String url, @QueryMap Map<String, Object> maps);

    @GET
    Call<ResponseBody> executeGetCall(@Url String url);

    // =============post请求============

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> executePost(@Url String url, @FieldMap Map<String, Object> maps);

    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST
    Observable<ResponseBody> executePost(@Url String url, @Body RequestBody body);

    @POST
    Observable<ResponseBody> executePost(@Url String url);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> executePostCall(@Url String url, @FieldMap Map<String, Object> maps);

    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST
    Call<ResponseBody> executePostCall(@Url String url, @Body RequestBody str);

    @POST
    Call<ResponseBody> executePostCall(@Url String url);

    // =============文件请求============
    @POST
    Observable<ResponseBody> executePostCallUpload(@Url String url, @Body MultipartBody multipartBody);

    @POST
    Call<ResponseBody> executePostCallUploadCall(@Url String url, @Body MultipartBody multipartBody);
}