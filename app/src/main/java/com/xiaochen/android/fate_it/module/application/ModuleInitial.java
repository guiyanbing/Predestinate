package com.xiaochen.android.fate_it.module.application;

import android.content.Context;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.xiaochen.android.fate_it.BuildConfig;
import com.xiaochen.android.fate_it.module.login.LoginMgr;
import com.xiaochen.android.fate_it.module.request.RequestHelper;

/**
 * 模块初始化工具
 * Created by ZRP on 2016/12/27.
 */
public class ModuleInitial {

    private Context context;

    public ModuleInitial(Context context) {
        this.context = context;
    }

    public void initial() {
        PLogger.init(BuildConfig.DEBUG);    //初始化日志打印
        PToast.init(context);               //初始化toast提示
        PSP.getInstance().init(context);    //初始化sharedPreferences存储
        RequestHelper.getInstance().initCookie(context, LoginMgr.cookie);    //初始化网络请求
    }
}
