package com.juxin.predestinate.module.logic.model.impl;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.module.local.login.LoginMgr;
import com.juxin.predestinate.module.logic.cache.PCache;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.model.mgr.HttpMgr;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestHelper;
import com.juxin.predestinate.module.logic.request.RequestParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求实现类
 * Created by ZRP on 2016/12/29.
 */
public class HttpMgrImpl implements HttpMgr {

    @Override
    public void init() {

    }

    @Override
    public void release() {

    }

    private HTCallBack reqPostNoCacheHttp(UrlParam urlParam, Map<String, Object> post_param, RequestComplete requestCallback) {
        return reqPostNoCacheHttp(urlParam, null, null, post_param, requestCallback);
    }

    /**
     * 加密，不缓存
     * @param urlParam
     * @param headerMap
     * @param get_param
     * @param post_param
     * @param requestCallback
     * @return
     */
    private HTCallBack reqPostNoCacheHttp(UrlParam urlParam, Map<String, String> headerMap,Map<String, Object> get_param,
                                   Map<String, Object> post_param, RequestComplete requestCallback) {
        return reqPostHttp(urlParam, headerMap, get_param, post_param, RequestParam.CacheType.CT_Cache_No, true, requestCallback);
    }


    private HTCallBack reqPostAndCacheHttp(UrlParam urlParam, Map<String, Object> post_param, RequestComplete requestCallback) {
        return reqPostAndCacheHttp(urlParam, null, null, post_param, requestCallback);
    }


    /**
     * 加密，缓存
     * @param urlParam
     * @param headerMap
     * @param get_param
     * @param post_param
     * @param requestCallback
     * @return
     */
    private HTCallBack reqPostAndCacheHttp(UrlParam urlParam, Map<String, String> headerMap,Map<String, Object> get_param,
                                          Map<String, Object> post_param, RequestComplete requestCallback) {
        return reqPostHttp(urlParam, headerMap, get_param, post_param, RequestParam.CacheType.CT_Cache_Params, true, requestCallback);
    }


    private HTCallBack reqGetNoCacheHttp(UrlParam urlParam, Map<String, Object> get_param, RequestComplete requestCallback) {
        return reqGetNoCacheHttp(urlParam, null, get_param, requestCallback);
    }

    /**
     * 加密，不缓存
     * @param urlParam
     * @param headerMap
     * @param get_param
     * @param requestCallback
     * @return
     */
    private HTCallBack reqGetNoCacheHttp(UrlParam urlParam, Map<String, String> headerMap, Map<String, Object> get_param, RequestComplete requestCallback) {
        return reqGetHttp(urlParam, headerMap, get_param, RequestParam.CacheType.CT_Cache_No, true, requestCallback);
    }

    private HTCallBack reqGetAndCacheHttp(UrlParam urlParam, Map<String, Object> get_param, RequestComplete requestCallback) {
        return reqGetAndCacheHttp(urlParam, null, get_param, requestCallback);
    }

    /**
     * 加密，缓存
     * @param urlParam
     * @param headerMap
     * @param get_param
     * @param requestCallback
     * @return
     */
    private HTCallBack reqGetAndCacheHttp(UrlParam urlParam, Map<String, String> headerMap, Map<String, Object> get_param, RequestComplete requestCallback) {
        return reqGetHttp(urlParam, headerMap, get_param, RequestParam.CacheType.CT_Cache_Params, true, requestCallback);
    }

    /**
     * get
     * @param urlParam
     * @param headerMap
     * @param get_param
     * @param cacheType
     * @param isEncrypt
     * @param requestCallback
     * @return
     */
    private HTCallBack reqGetHttp(UrlParam urlParam, Map<String, String> headerMap, Map<String, Object> get_param,
                               RequestParam.CacheType cacheType, boolean isEncrypt, RequestComplete requestCallback) {
        return reqHttp(urlParam, headerMap, null, get_param, null, cacheType, isEncrypt, requestCallback);
    }

    /**
     * post
     * @param urlParam
     * @param headerMap
     * @param get_param
     * @param post_param
     * @param cacheType
     * @param isEncrypt
     * @param requestCallback
     * @return
     */
    private HTCallBack reqPostHttp(UrlParam urlParam, Map<String, String> headerMap,Map<String, Object> get_param,
                                   Map<String, Object> post_param, RequestParam.CacheType cacheType, boolean isEncrypt, RequestComplete requestCallback) {
        return reqHttp(urlParam, headerMap, null, get_param, post_param, cacheType, isEncrypt, requestCallback);
    }

    private HTCallBack reqHttp(UrlParam urlParam, Map<String, String> headerMap, Map<String, File> file_param,
                                   Map<String, Object> get_param, Map<String, Object> post_param,
                                   RequestParam.CacheType cacheType, boolean isEncrypt, RequestComplete requestCallback) {
        RequestParam requestParam = new RequestParam();
        requestParam.setUrlParam(urlParam);
        requestParam.setHead_param(headerMap);
        requestParam.setFile_param(file_param);
        requestParam.setGet_param(get_param);
        requestParam.setPost_param(post_param);
        requestParam.setCacheType(cacheType);
        requestParam.setCacheType(cacheType);
        requestParam.setNeedEncrypt(isEncrypt);
        requestParam.setRequestCallback(requestCallback);
        return request(requestParam);
    }


    @Override
    public HTCallBack request(RequestParam requestParam) {
        //TODO 添加完整请求及基础解析
        final UrlParam urlParam = requestParam.getUrlParam();
        final Map<String, String> headerMap = requestParam.getHead_param();
        final Map<String, Object> get_param = requestParam.getGet_param();
        final Map<String, Object> post_param = requestParam.getPost_param();
        final Map<String, File> file_param = requestParam.getFile_param();
        final RequestComplete requestCallback = requestParam.getRequestCallback();
        final RequestParam.CacheType cacheType = requestParam.getCacheType();
        final boolean isEncrypt = requestParam.isNeedEncrypt();

        final String url = urlParam.getFinalUrl();// 获取url
        String cacheUrl = url;
        if (RequestParam.CacheType.CT_Cache_Params == cacheType) {//用Url和Post请求参数作为缓存的key
            if (post_param != null) {
                cacheUrl += new Gson().toJson(post_param);
            } else if (get_param != null) {
                cacheUrl += new Gson().toJson(get_param);
            }
        }
        if (TextUtils.isEmpty(url)) return new HTCallBack();

        // 判断登录,如果用户没有登录，并且该请求又需要登录的话，则不发出请求，且清除该url的缓存数据
        if (!LoginMgr.hasLogin) {
            if (urlParam.isNeedLogin()) {
                if (RequestParam.CacheType.CT_Cache_No != cacheType)
                    PCache.getInstance().deleteCache(cacheUrl);// 清除该url的缓存
                return new HTCallBack();
            }
        }
        PLogger.d(urlParam + "_url: " + url);

        final HttpResponse result = new HttpResponse(urlParam);
        //先从缓存中拿数据，如果有缓存，先行抛出缓存结果
        if (RequestParam.CacheType.CT_Cache_No != cacheType) {
            //获取缓存中的数据,如果不为null,则开始解析数据,并返回数据
            String cacheStr = PCache.getInstance().getCache(cacheUrl);
            if (cacheStr != null) {
                PLogger.d("response cache，request url：" + url + "\ncache String：" + cacheStr);
//                result.setOk();
//                result.setCache(true);//设置为cache数据
                result.parseJson(cacheStr);

                //缓存回调
                if (requestCallback != null) requestCallback.onRequestComplete(result);
            }
        }

        Map<String, String> requestHeaderMap = new HashMap<>();
        requestHeaderMap.put("Cookie", LoginMgr.cookie);
        if (headerMap != null) requestHeaderMap.putAll(headerMap);

        //然后正式发出请求，请求完成之后再次抛出请  求结果
        final String finalCacheUrl = cacheUrl;
        Call<ResponseBody> httpResultCall;
        httpResultCall = RequestHelper.getInstance().reqHttpCallUrl(
                requestHeaderMap, urlParam.getFinalUrl(), get_param, post_param, file_param, isEncrypt);
        httpResultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    PLogger.d("response fail，request url：" + url);
                    PLogger.printThrowable(e);
//                    result.setError();//设置失败
//                    result.setCache(false);
                    if (requestCallback != null) requestCallback.onRequestComplete(result);
                    return;
                }
                String resultString = sb.toString();
                PLogger.d("response OK，request url：" + url + "\nresponse：" + resultString);

//                result.setOk();//设置成功
//                result.setCache(false);//设置为cache数据
                result.parseJson(resultString);

                if (RequestParam.CacheType.CT_Cache_No != cacheType)
                    PCache.getInstance().cacheString(finalCacheUrl, resultString);//存储到缓存

                //如果有请求完成的回调实例的话，则进行回调
                if (requestCallback != null) requestCallback.onRequestComplete(result);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                PLogger.d("response fail，request url：" + url);
                PLogger.printThrowable(t);
//                result.setError();//设置失败
//                result.setCache(false);
                if (requestCallback != null) requestCallback.onRequestComplete(result);
            }

        });
        return new HTCallBack(httpResultCall);
    }

    @Override
    public HTCallBack download(String url, String filePath, DownloadListener downloadListener) {
        if (FileUtil.isExist(filePath)) {
            if (downloadListener != null) downloadListener.onSuccess(url, filePath);
        } else {
            return RequestHelper.getInstance().downloadFile(url, filePath, downloadListener);
        }
        return new HTCallBack();
    }
}
