package com.juxin.predestinate.bean.center.user.light;

import android.text.TextUtils;

import com.juxin.predestinate.bean.center.user.detail.UserBasic;

import org.json.JSONObject;

/**
 * 个人资料轻量级
 * Created by Kind on 16/3/22.
 */
public class UserInfoLightweight extends UserBasic {
//    {
//        "res": {
//        "list": [
//        {
//            "avatar": "http://image1.yuanfenba.net/uploads/oss/photo/20161108/1478595301708993772.png",
//                "avatar_status": 1,
//                "birthday": "1988-09-09",
//                "city": 110108,
//                "distance": "16",
//                "gender": 2,
//                "height": 166,
//                "is_month": false,
//                "is_online": false,
//                "is_say_hello": false,
//                "is_vip": false,
//                "kf_id": 0,
//                "last_online": 0,
//                "nickname": "小友客服",
//                "nickname_status": 0,
//                "province": 110000,
//                "uid": 9999
//        }
//        ]
//    },
//        "status": "ok",
//            "tm": 1481856586
//    }

    /**
     * 新版本，新接口解析
     */
    @Override
    public void parseJson(String jsonResult) {
        if (TextUtils.isEmpty(jsonResult)) {
            return;
        }

        JSONObject jsonObject = getJsonObject(jsonResult);
    }
}