package com.juxin.library.request;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 全局统一的网络请求工具类：retrofit
 * Created by ZRP on 2016/9/8.
 */
public class Requester {

    private static final int CONNECT_TIMEOUT = 5;  //请求超时时间，单位：min
    private static final int RW_TIMEOUT = 5;       //读写超时时间，单位：min

    private static OkHttpClient.Builder builder;

    /**
     * 初始化OkHttpClient.Builder()
     */
    public static void initBuilder(final Context context, boolean isDebug) {
        builder = new OkHttpClient.Builder();
        if (isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            builder.addInterceptor(loggingInterceptor);
        }

        //配置缓存选项
        File cacheFile = new File(context.getCacheDir(), "responses");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);// 50 MiB
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
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES);
        builder.readTimeout(RW_TIMEOUT, TimeUnit.MINUTES);
        builder.writeTimeout(RW_TIMEOUT, TimeUnit.MINUTES);
        //错误重连
        builder.retryOnConnectionFailure(true);
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//此converter放在最后，因为不确定返回的数据是否为json
                .build();
        return retrofit.create(service);
    }
}
