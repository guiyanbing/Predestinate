package com.xiaochen.android.fate_it.module.application;

import com.juxin.library.utils.TypeConvertUtil;

import java.util.Calendar;

/**
 * 服务器时间管理类
 * Created by zijunna on 15/3/2.
 */
public class ServerTime {

    /**
     * 服务器与本地时间的时差
     */
    private static long SERVER_SC;

    /**
     * 设置服务器时间
     *
     * @param sys_tm 服务器时间戳
     */
    public static void setServerTime(long sys_tm) {
        if (sys_tm <= 0) {
            return;
        }
        Calendar localCalendar = Calendar.getInstance();
        if (String.valueOf(sys_tm).length() > 10) {
            SERVER_SC = TypeConvertUtil.toLong(String.valueOf(sys_tm).substring(0, 10), System.currentTimeMillis() / 1000);
        } else {
            SERVER_SC = sys_tm - (localCalendar.getTimeInMillis() / 1000);
        }
    }

    /**
     * 获取服务器时间
     *
     * @return 根据服务器返回时间矫正后的Calendar对象
     */
    public static Calendar getServerTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + SERVER_SC * 1000);
        return calendar;
    }
}
