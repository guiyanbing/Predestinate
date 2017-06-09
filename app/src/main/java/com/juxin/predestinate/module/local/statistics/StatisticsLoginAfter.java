package com.juxin.predestinate.module.local.statistics;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IQQ on 2017/6/9.
 */

public class StatisticsLoginAfter {
    //    公共组件-登录后引导
    //    登录后引导->一键打招呼,登录后引导(男用户)
    public static void sayHi(List<UserInfoLightweight> data) {
        long[] sayhis = new long[data != null ?data.size():0];
        for (int i = 0; i < sayhis.length; i++) {
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
}
