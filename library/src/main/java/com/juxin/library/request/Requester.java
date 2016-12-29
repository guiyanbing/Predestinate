package com.juxin.library.request;

import android.content.Context;

import com.juxin.library.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 全局统一的网络请求工具类：retrofit
 * Created by ZRP on 2016/9/8.
 */
public class Requester {

    private static final int CONNECT_TIMEOUT = 20;  //请求超时时间，单位：s
    private static final int RW_TIMEOUT = 20;       //读写超时时间，单位：s

    private static OkHttpClient.Builder builder;

    /**
     * 初始化OkHttpClient.Builder()
     */
    public static void initBuilder(final Context context, boolean isDebug) {
        builder = new OkHttpClient.Builder();
        if (isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        //配置缓存选项
        File cacheFile = new File(context.getCacheDir(), "responses");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);// 50 MiB
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtils.isConnected(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtils.isConnected(context)) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置文件下载监听
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        //设置超时
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(RW_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(RW_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
    }

    /**
     * 初始化cookie
     */
    public static void initCookie(Context context, boolean isDebug, final String cookie) {
        if (builder == null) initBuilder(context, isDebug);
        //添加请求cookie
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request authorised = request.newBuilder()
                        .header("Cookie", cookie)
                        .build();
                return chain.proceed(authorised);
            }
        });
    }

    /**
     * 构造retrofit请求api
     *
     * @param host    host地址
     * @param service retrofit请求api
     * @param <T>     retrofit请求api
     * @return retrofit请求api
     */
    public static <T> T getRequestAPI(String host, Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//此converter放在最后，因为不确定返回的数据是否为json
                .build();
        return retrofit.create(service);
    }
}
