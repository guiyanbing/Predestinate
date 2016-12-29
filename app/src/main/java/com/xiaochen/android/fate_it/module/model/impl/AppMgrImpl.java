package com.xiaochen.android.fate_it.module.model.impl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.juxin.library.enc.MD5;
import com.juxin.library.log.PSP;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.library.utils.ViewUtils;
import com.xiaochen.android.fate_it.BuildConfig;
import com.xiaochen.android.fate_it.module.application.APP;
import com.xiaochen.android.fate_it.module.application.FinalKey;
import com.xiaochen.android.fate_it.module.application.ServerTime;
import com.xiaochen.android.fate_it.module.model.mgr.AppMgr;

import java.util.List;

/**
 * 应用状态与手机硬件信息获取
 * Created by ZRP on 2016/12/29.
 */
public class AppMgrImpl implements AppMgr {

    private String packageName = "";    // 软件包名
    private int versionCode = 0;        // 软件版本号
    private String versionName = "";    // 软件版本名称

    private String imei = "";           // 移动设备国际识别码
    private String imsi = "";           // 移动用户识别码
    private String simo = "";           // SIM卡供应商
    private String mac = "";            // 网络mac地址

    @Override
    public void init() {
        try {
            PackageManager packageManager = APP.context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(APP.context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
            versionName = packInfo.versionName;
            packageName = packInfo.packageName;

            TelephonyManager mTm = (TelephonyManager) APP.context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = mTm.getDeviceId();
            imsi = mTm.getSubscriberId();
            simo = mTm.getSimOperator();

            mac = NetworkUtils.getMacAddress(APP.context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {

    }

    // ================渠道与包信息================

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public int getVerCode() {
        return versionCode;
    }

    @Override
    public String getVerName() {
        return versionName;
    }

    @Override
    public String getMainChannel() {
        return null;
    }

    @Override
    public String getSubChannel() {
        return null;
    }

    @Override
    public String getMainChannelID() {
        return null;
    }

    @Override
    public String getSubChannelID() {
        return null;
    }

    // ================手机硬件信息================

    @Override
    public String getAndroidOSVer() {
        return Build.VERSION.RELEASE;
    }

    @Override
    public String getPhoneMode() {
        return Build.MODEL;
    }

    @Override
    public String getPhoneManufacturer() {
        return Build.MANUFACTURER;
    }

    @Override
    public String getIMEI() {
        return imei;
    }

    @Override
    public boolean isEqualIMEI(String imeiString) {
        if (TextUtils.isEmpty(imeiString) || TextUtils.isEmpty(imei)) return false;
        return imei.equals(imeiString);
    }

    @Override
    public String getIMSI() {
        return imsi;
    }

    @Override
    public String getSimOperator() {
        return simo;
    }

    @Override
    public String getMAC() {
        return NetworkUtils.getMacAddress(APP.context);
    }

    @Override
    public int getDeviceID() {
        return MD5.getMD5("" + mac + imei);
    }

    // ================软件状态信息================

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public boolean isForeground() {
        ActivityManager activityManager = (ActivityManager) APP.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(APP.context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return true;
    }

    @Override
    public boolean isWiFi() {
        return NetworkUtils.isWifi(APP.context);
    }

    @Override
    public boolean isNetAvailable() {
        return NetworkUtils.isConnected(APP.context);
    }

    @Override
    public long getTime() {
        return ServerTime.getServerTime().getTimeInMillis() / 1000;
    }

    @Override
    public long getSecondTime() {
        return ServerTime.getServerTime().getTimeInMillis();
    }

    @Override
    public int getScreenWidth() {
        return ViewUtils.getScreenWidth((Activity) APP.getActivity());
    }

    @Override
    public int getScreenHeight() {
        return ViewUtils.getScreenHeight((Activity) APP.getActivity());
    }

    @Override
    public int getStatusBarHeight() {
        return ViewUtils.getStatusBarHeight(APP.context);
    }

    @Override
    public int appRunCount() {
        return PSP.getInstance().getInt(FinalKey.APP_RUN_COUNT, 1);
    }
}
