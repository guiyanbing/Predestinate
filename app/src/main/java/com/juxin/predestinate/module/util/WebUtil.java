package com.juxin.predestinate.module.util;

import com.juxin.predestinate.module.logic.application.App;

/**
 * html及url拼接工具类
 * Created by ZRP on 2017/5/3.
 */
public class WebUtil {

    /**
     * 拼接url参数，控制刷新时间及清晰度
     *
     * @param url 需要拼接的url
     * @return 拼接完成的url
     */
    public static String jointUrl(String url) {
        return url + "?resolution=" + (PerformanceHelper.isHighPerformance(App.context) ? "2" : "1")
                + "&v=" + System.currentTimeMillis();
    }
}
