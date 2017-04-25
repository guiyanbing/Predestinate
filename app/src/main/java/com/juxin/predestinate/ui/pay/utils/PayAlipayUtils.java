package com.juxin.predestinate.ui.pay.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.alipay.sdk.app.PayTask;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.ui.pay.PayListAct;

/**
 * Created by Kind on 2017/4/25.
 */

public class PayAlipayUtils {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Activity activity;

    public PayAlipayUtils(Activity activity) {
        super();
        this.activity = activity;
    }

    public void pay(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        PayListAct.bPayOkFlag = true;
                        MMToast.showShort("支付成功");
                        //更新信息
                      //  UpdateInfo updateInfo = new UpdateInfo(activity, UIHelper.PAYMENTACT);
                    //    updateInfo.updateInfo();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            MMToast.showShort("支付结果确认中");
                            //更新信息
                           // UpdateInfo updateInfo = new UpdateInfo(activity, UIHelper.PAYMENTACT);
                           // updateInfo.updateInfo();
                        } else {
                            MMToast.showShort("支付失败");

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    MMToast.showShort("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    };
}
