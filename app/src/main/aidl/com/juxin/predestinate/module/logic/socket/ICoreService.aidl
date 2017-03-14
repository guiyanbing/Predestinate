// ISocketService.aidl
package com.juxin.predestinate.module.logic.socket;

// Declare any non-default types here with import statements
import com.juxin.predestinate.module.logic.socket.ICSCallback;

interface ICoreService {

    /**
    * 设置回调。
    */
    void setCallback(ICSCallback iCSCallback);

    /**
    * 登录。
    */
    void login(long uid, String token);

    /**
    * 退出登录。
    */
    void logout();

    /**
    * 设置GPS坐标，经纬度坐标。
    */
    void setLocationGPS(double longitude, double latitude);

    /**
    * 设置系统类型。操作系统:0 其它；1 苹果；2 小米
    */
    void setSystemInfo(int systemInfo);
}
