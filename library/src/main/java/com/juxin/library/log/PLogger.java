package com.juxin.library.log;

import android.os.Looper;
import android.text.TextUtils;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

/**
 * 全局统一的日志打印工具类：comes from <pre>https://github.com/elvishew/XLog</pre>
 * Created by ZRP on 2016/9/8.
 */
public class PLogger {

    private static final int STACK = 4;

    public static void init(boolean isDebug) {
        XLog.init(isDebug ? LogLevel.ALL : LogLevel.NONE);
    }

    public static void v(String msg) {
        XLog.v(getLogEnvironment(STACK), msg);
    }

    public static void d(String msg) {
        XLog.d(getLogEnvironment(STACK), msg);
    }

    public static void i(String msg) {
        XLog.i(getLogEnvironment(STACK), msg);
    }

    public static void w(String msg) {
        XLog.w(getLogEnvironment(STACK), msg);
    }

    public static void e(String msg) {
        XLog.e(getLogEnvironment(STACK), msg);
    }

    public static void printObject(Object object) {
        XLog.d(getLogEnvironment(STACK), object);
    }

    public static void printJson(String jsonString) {
        XLog.json(jsonString);
    }

    public static void printXML(String xmlString) {
        XLog.xml(xmlString);
    }

    public static void printThrowable(Throwable throwable) {
        XLog.w("", throwable);
    }

    /**
     * 拼接打印当前log的环境信息，方便定位
     *
     * @param stack 回溯栈纵深
     * @return 当前调用日志打印的环境信息
     */
    private static String getLogEnvironment(int stack) {
        StringBuffer sb = new StringBuffer();

        sb.append(Thread.currentThread().getStackTrace()[stack - 1].getMethodName());
        sb.append("_");
        sb.append(getSimpleName(Thread.currentThread().getStackTrace()[stack].getClassName()));
        sb.append("_");
        sb.append(Thread.currentThread().getStackTrace()[stack].getMethodName());
        sb.append("_LineNumber[");
        sb.append(Thread.currentThread().getStackTrace()[stack].getLineNumber());
        sb.append("]_MainThreadName[");
        sb.append(Looper.getMainLooper().getThread().getName());
        sb.append("]_CurrentThreadName[");
        sb.append(Thread.currentThread().getName());
        sb.append("]\n%s");

        return sb.toString();
    }

    private static String getSimpleName(String name) {
        if (TextUtils.isEmpty(name)) return "";
        int dot = name.lastIndexOf('.');
        if (dot != -1) return name.substring(dot + 1);
        return name;
    }
}
