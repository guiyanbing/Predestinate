package com.xiaochen.android.fate_it.module.request;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.request.FileCallback;
import com.juxin.library.request.Requester;
import com.xiaochen.android.fate_it.module.application.APP;
import com.xiaochen.android.fate_it.module.application.Constant;
import com.xiaochen.android.fate_it.module.application.ModuleMgr;
import com.xiaochen.android.fate_it.utils.Url_Enc;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;

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
    private Gson gson = new Gson();

    /**
     * 初始化网络请求
     */
    public void init() {
        Requester.initBuilder(APP.context, ModuleMgr.getAppMgr().isDebug());
        requestAPI = Requester.getRequestAPI(Constant.HOST_URL, RequestAPI.class);
    }

    /**
     * 设置请求cookie
     *
     * @param cookie cookie
     */
    public void initCookie(String cookie) {
        Requester.initCookie(APP.context, ModuleMgr.getAppMgr().isDebug(), cookie);
        requestAPI = Requester.getRequestAPI(Constant.HOST_URL, RequestAPI.class);
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
        return requestAPI.executePostCallUploadCall(url, builder.build());
    }

    /**
     * 网络请求
     *
     * @param url
     * @param get_param   get数据
     * @param post_param  post数据
     * @param file_params 文件数据
     * @param isEncrypt   是否加密
     * @return Call
     */
    public Call<ResponseBody> reqHttpCallUrl(String url, Map<String, Object> get_param, Map<String, Object> post_param,
                                             Map<String, File> file_params, boolean isEncrypt) {
        if (isEncrypt) {//是否加密 加密
            if (file_params == null && post_param != null) {//post加密。文件上传使用的为post，故同时判断两者
                String json = gson.toJson(post_param);
                PLogger.d(json);
                url = Url_Enc.appendJsonUrl(url, get_param, json);
            } else {//其余请求为get请求
                if (get_param != null) PLogger.d(get_param.toString());
                url = Url_Enc.appendUrl(url, get_param, null);
            }
        }

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
            return requestAPI.executePostCallUploadCall(url, builder.build());
        } else if (post_param != null) {//post请求
            PLogger.d("---request--->带参数的post请求：" + url);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(post_param));
            return requestAPI.executePostCall(url, body);
        } else if (get_param != null) {//带请求参数的get请求
            PLogger.d("---request--->带参数的get请求：" + url);
            return requestAPI.executeGetCall(url, get_param);
        } else {//无请求参数的post/get请求
            PLogger.d("---request--->无参数的get请求：" + url);
            return requestAPI.executeGetCall(url);
        }
    }

    /**
     * 测试用的 没有太多意义
     *
     * @param url
     * @param get_param
     * @param post_param
     * @param file_params
     * @param isEncrypt
     * @return
     */
    public Observable<ResponseBody> reqHttpObservableUrl(String url, Map<String, Object> get_param, Map<String, Object> post_param,
                                                         Map<String, File> file_params, boolean isEncrypt) {
        if (isEncrypt) {//是否加密 加密
            if (file_params == null && post_param != null) {
                String json = gson.toJson(post_param);
                PLogger.d(json);
                url = Url_Enc.appendJsonUrl(url, get_param, json);
            } else {
                url = Url_Enc.appendUrl(url, get_param, null);
            }
        }

        if (file_params != null) {//post请求，上传文件
            MultipartBody.Builder builder = new MultipartBody.Builder();
            for (Map.Entry<String, File> entry : file_params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), entry.getValue()));
            }
            if (post_param != null) {//如果文件上传时有post参数，就将post参数添加到请求参数中
                builder.addPart(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(post_param)));
            }
            builder.setType(MultipartBody.FORM);
            return requestAPI.executePostCallUpload(url, builder.build());
        } else if (post_param != null) {//post请求
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(post_param));
            return requestAPI.executePost(url, body);
        } else {//get请求
            return requestAPI.executeGet(url);
        }
    }

    /**
     * 下载文件
     *
     * @param url      下载地址
     * @param filePath 文件存储路径
     * @param listener 下载监听
     */
    public void downloadFile(String url, String filePath, DownloadListener listener) {
        Call<ResponseBody> responseBodyCall = requestAPI.executeGetCall(url);
        responseBodyCall.enqueue(new FileCallback(url, filePath, listener));
    }
}
