package com.juxin.predestinate.module.logic.invoke;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.observe.MsgMgr;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * CMD操作统一处理
 * Created by ZRP on 2017/1/3.
 */
public class Invoker {

    private WebAppInterface appInterface = new WebAppInterface(App.context, null);
    private Object webView;

    /**
     * @return 获取持有的WebAppInterface实例
     */
    public WebAppInterface getWebAppInterface() {
        return appInterface;
    }

    //--------------------CMD处理start--------------------

    private static class SingletonHolder {
        public static Invoker instance = new Invoker();
    }

    public static Invoker getInstance() {
        return SingletonHolder.instance;
    }

    private Invoke invoke = new Invoke();

    /**
     * 根据command和data执行对应方法（处理在app内情况）
     *
     * @param appInterface WebAppInterface实例
     * @param cmd          CMD操作码
     * @param data         操作执行的json字符串数据
     */
    public void doInApp(WebAppInterface appInterface, final String cmd, final String data) {
        PLogger.d("---doInApp--->cmd：" + cmd + "，data：" + data +
                "，AppMgr.isForground()：" + ModuleMgr.getAppMgr().isForeground());

        this.appInterface = (appInterface == null ? new WebAppInterface(App.context, null) : appInterface);
        webView = appInterface.getWebView();

        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Method notifyCMDMethod = Invoke.class.getMethod(cmd, String.class);
                    notifyCMDMethod.invoke(invoke, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 本地调用执行JS逻辑
     *
     * @param cmd  JS cmd命令
     * @param data 传递给JS的数据
     */
    public void doInJS(String cmd, Map<String, Object> data) {
        Map<String, Object> cmdMap = new HashMap<>();
        cmdMap.put("jcmd", cmd);
        cmdMap.put("data", data == null ? new HashMap<>() : data);
        String url = "javascript:window.platform.appCommand('" + new Gson().toJson(cmdMap) + "')";
        doInJS(url);
    }

    /**
     * 本地调用执行JS逻辑
     *
     * @param callbackName   回调方法名
     * @param callbackID     回调id
     * @param responseString 调用传值
     */
    public void doInJS(String callbackName, String callbackID, String responseString) {
        doInJS("javascript:" + callbackName + "(\'" + callbackID + "\',\'" + responseString + "\')");
    }

    /**
     * 本地调用执行JS逻辑
     *
     * @param loadUrl 调用执行的命令
     */
    public void doInJS(String loadUrl) {
        if (webView != null) {
            if (webView instanceof com.tencent.smtt.sdk.WebView) {
                ((com.tencent.smtt.sdk.WebView) webView).loadUrl(loadUrl);
                PLogger.d("---tencent x5--->" + loadUrl);
            } else if (webView instanceof android.webkit.WebView) {
                ((android.webkit.WebView) webView).loadUrl(loadUrl);
                PLogger.d("---webkit--->" + loadUrl);
            }
        }
    }

    //--------------------CMD处理end--------------------

    public class Invoke {

    }
}
