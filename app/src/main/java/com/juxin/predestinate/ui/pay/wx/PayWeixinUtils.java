package com.juxin.predestinate.ui.pay.wx;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Kind on 2017/4/24.
 */

public class PayWeixinUtils {

    private IWXAPI api;
    public PayWeixinUtils(Context context) {
        api = WXAPIFactory.createWXAPI(context, "");
    }

}
