package com.juxin.predestinate.module.logic.request;

import android.content.Context;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.request.FileCallback;
import com.juxin.library.request.Requester;
import com.juxin.library.utils.JniUtil;
import com.juxin.predestinate.BuildConfig;
import com.juxin.predestinate.module.logic.config.Hosts;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.UrlEnc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 网络请求管理类，基于Request对retrofit的二次封装
 * Created by ZRP on 2016/9/19.
 */
public class RequestHelper {

    private volatile static RequestHelper instance = null;

    private RequestHelper() {
    }

    public static RequestHelper getInstance() {
        if (instance == null) {
            synchronized (RequestHelper.class) {
                if (instance == null) instance = new RequestHelper();
            }
        }
        return instance;
    }

    // =====================初始化=========================

    private RequestAPI requestAPI;

    /**
     * 初始化网络请求
     */
    public void init(Context context) {
        Requester.initBuilder(context, BuildConfig.DEBUG);
        requestAPI = Requester.getRequestAPI(Hosts.HOST_URL, RequestAPI.class);
    }

    /**
     * 根据重新指定的url上传文件 zrp
     *
     * @param url     完整url
     * @param fileMap 文件map
     * @return Call
     */
    public Call<ResponseBody> reqUploadCall(String url, Map<String, File> fileMap) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (fileMap != null)
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody.create(MediaType.parse("image/jpeg"), entry.getValue()));
            }
        builder.setType(MultipartBody.FORM);
        return requestAPI.executePostCallUploadCall(new HashMap<String, String>(), url, builder.build());
    }

    /**
     * 网络请求
     *
     * @param headerMap     请求headerMap
     * @param url           请求Url
     * @param get_param     get数据
     * @param post_param    post数据
     * @param jsonParam     json数据
     * @param file_params   文件数据
     * @param isEncrypt     是否加密
     * @param isJsonRequest 是否为application/json格式提交的post数据
     * @return Call
     */
    public Call<ResponseBody> reqHttpCallUrl(Map<String, String> headerMap, String url,
                                             Map<String, Object> get_param, Map<String, Object> post_param, String jsonParam,
                                             Map<String, File> file_params, boolean isEncrypt, boolean isJsonRequest) {
        url = UrlEnc.appendUrl(url, get_param, post_param, isEncrypt);
        if (headerMap == null) headerMap = new HashMap<>();

        if (file_params != null && file_params.size() > 0) {//post请求，上传文件
            PLogger.d("---request--->文件上传post请求：" + url);
            MultipartBody.Builder builder = new MultipartBody.Builder();
            for (Map.Entry<String, File> entry : file_params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), entry.getValue()));
            }
            if (post_param != null) {//如果文件上传时有post参数，就将post参数添加到请求参数中, form表单形式上传参数
                for (Map.Entry<String, Object> entry : post_param.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            builder.setType(MultipartBody.FORM);
            return requestAPI.executePostCallUploadCall(headerMap, url, builder.build());
        } else if (post_param != null) {//post请求
            PLogger.d("---request--->带参数的post请求：" + url);
            if (isJsonRequest) {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        isEncrypt ? JniUtil.GetEncryptString(JsonUtil.mapToJSON(post_param).toString()) : JsonUtil.mapToJSON(post_param).toString());
                return requestAPI.executePostCall(headerMap, url, body);
            } else {
                return requestAPI.executePostCall(headerMap, url, post_param);
            }
        } else if (!TextUtils.isEmpty(jsonParam)) {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    isEncrypt ? JniUtil.GetEncryptString(jsonParam) : jsonParam);
            return requestAPI.executePostCall(headerMap, url, body);
        } else {//无请求参数的post/get请求[get请求参数已经在hash的时候拼接，故无需再次拼接]
            PLogger.d("---request--->带参数的get请求：" + url);
            return requestAPI.executeGetCall(headerMap, url);
        }
    }

    /**
     * 下载文件
     *
     * @param url      下载地址
     * @param filePath 文件存储路径
     * @param listener 下载监听
     */
    public HTCallBack downloadFile(String url, String filePath, DownloadListener listener) {
        Call<ResponseBody> responseBodyCall = requestAPI.executeGetCall(new HashMap<String, String>(), url);
        responseBodyCall.enqueue(new FileCallback(url, filePath, listener));
        return new HTCallBack(responseBodyCall);
    }
}
