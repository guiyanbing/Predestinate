package com.juxin.predestinate.module.logic.model.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.juxin.library.log.PSP;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.library.utils.ViewUtils;
import com.juxin.predestinate.BuildConfig;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.config.ServerTime;
import com.juxin.predestinate.module.logic.model.mgr.AppMgr;
import com.juxin.predestinate.module.util.PkgHelper;

import java.util.Iterator;
import java.util.List;

/**
 * 应用状态与手机硬件信息获取
 * Created by ZRP on 2016/12/29.
 */
public class AppMgrImpl implements AppMgr {

    private String packageName = "";    // 软件包名
    private String signature = "";      // 软件签名
    private int versionCode = 0;        // 软件版本号
    private String versionName = "";    // 软件版本名称

    private String imei = "";           // 移动设备国际识别码
    private String imsi = "";           // 移动用户识别码
    private String simo = "";           // SIM卡供应商
    private String mac = "";            // 网络mac地址

    private String UMChannel = "";      // 友盟渠道名称
    private String mainChannelID = "";  // 应用id，即主渠道号
    private String subChannelID = "";   // 子渠道号

    @SuppressLint("HardwareIds")
    @Override
    public void init() {
        try {
            PackageManager packageManager = App.context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(App.context.getPackageName(), 0);
            // 获取软件基本信息
            versionCode = packInfo.versionCode;
            versionName = packInfo.versionName;
            packageName = packInfo.packageName;

            // 获取硬件信息
            TelephonyManager mTm = (TelephonyManager) App.context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = mTm.getDeviceId();
            imsi = mTm.getSubscriberId();
            simo = mTm.getSimOperator();
            mac = NetworkUtils.getMacAddress(App.context);

            UMChannel = PkgHelper.getMarket(App.context, App.context.getResources().getString(R.string.app_name) + "_1000_1000");
            mainChannelID = PkgHelper.getMainChannel(App.context, "1000"); //appInfo.metaData.getInt("sUID");
            subChannelID = PkgHelper.getSubChannel(App.context, "1000"); //appInfo.metaData.getInt("sID");

            // 获取签名
            List<PackageInfo> apps = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
            Iterator<PackageInfo> iterator = apps.iterator();
            while (iterator.hasNext()) {
                PackageInfo packageinfo = iterator.next();
                String thisName = packageinfo.packageName;
                if (thisName.equals(getPackageName())) {
                    signature = packageinfo.signatures[0].toCharsString();
                }
            }
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
    public String getSignature() {
        return signature;
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
    public String getUMChannel() {
        return UMChannel;
    }

    @Override
    public String getMainChannelID() {
        return mainChannelID;
    }

    @Override
    public String getSubChannelID() {
        return subChannelID;
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
        return imeiString.equals(imei);
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
        return NetworkUtils.getMacAddress(App.context);
    }

    @Override
    public String getDeviceID() {
        return String.valueOf(EncryptUtil.getHashCode(mac + imei));
    }

    // ================软件状态信息================

    @Override
    public String getProcessName(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
        if (runningApps == null) return "";

        for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
            if (processInfo.pid == pid) return processInfo.processName;
        }
        return "";
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public boolean isForeground() {
        ActivityManager activityManager = (ActivityManager) App.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(App.context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return true;
    }

    @Override
    public boolean isWiFi() {
        return NetworkUtils.isWifi(App.context);
    }

    @Override
    public boolean isNetAvailable() {
        return NetworkUtils.isConnected(App.context);
    }

    @Override
    public long getTime() {
        return ServerTime.getServerTime().getTimeInMillis();
    }

    @Override
    public long getSecondTime() {
        return ServerTime.getServerTime().getTimeInMillis() / 1000;
    }

    @Override
    public int getScreenWidth() {
        return ViewUtils.getScreenWidth((Activity) App.getActivity());
    }

    @Override
    public int getScreenHeight() {
        return ViewUtils.getScreenHeight((Activity) App.getActivity());
    }

    @Override
    public int getStatusBarHeight() {
        return ViewUtils.getStatusBarHeight(App.context);
    }

    @Override
    public int appRunCount() {
        return PSP.getInstance().getInt(FinalKey.APP_RUN_COUNT, 1);
    }
}
