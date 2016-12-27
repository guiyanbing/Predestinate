package com.xiaochen.android.fate_it.module.invoke;

import com.juxin.library.log.PLogger;
import com.xiaochen.android.fate_it.module.application.APP;
import com.xiaochen.android.fate_it.utils.UIShow;

import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * 应用内跳转
 * Created by ZRP on 2016/12/9.
 */
public class Invoker {

    private static Invoke invoke = new Invoke();

    /**
     * 根据command和data执行对应方法（处理在app内情况）
     *
     * @param cmd  CMD操作码
     * @param data 操作执行的json字符串数据
     */
    public static void doInApp(final String cmd, final String data) {
        PLogger.d("doInApp: --------->cmd：" + cmd + "，data：" + data);
        try {
            Method notifyCMDMethod = Invoke.class.getMethod(cmd, String.class, long.class, boolean.class);
            notifyCMDMethod.invoke(invoke, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //--------------------CMD处理end--------------------

    private static class Invoke {

        // 打开一个网页
        public void invoke_open_web(String data) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String url = jsonObject.optString("url");
                UIShow.showWebActivity(APP.getActivity(), url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
