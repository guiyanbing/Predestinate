package com.juxin.predestinate.module.local.statistics;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IQQ on 2017/6/9.
 */

public class StatisticsPayAndLoginAfter {
    //    公共组件-登录后引导
    //    登录后引导->一键打招呼,登录后引导(男用户)
    public static void sayHi(List<UserInfoLightweight> data) {
        long[] sayhis = new long[4];
        for (int i = 0; i < 4; i++) {
            sayhis[i] = data.get(i).getUid();
        }
        Map<String, Object> parms = new HashMap<>();
        parms.put("to_uid", sayhis);
        Statistics.userBehavior(SendPoint.login_guide_onekeysayhello, parms);
    }

    //登录后引导->如何赚钱,登录后引导(女用户)(普通点击)
    public static void moneyHelp() {
        Statistics.userBehavior(SendPoint.login_guide_moneyhelp);
    }

    //我的->我的Y币->充值页面->充值咨询按钮(普通点击)(网页暂时不加)
    public static void payZixun(){
        Statistics.userBehavior(SendPoint.pay_zixun);
    }
}
