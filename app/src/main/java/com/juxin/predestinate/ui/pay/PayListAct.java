package com.juxin.predestinate.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.config.PayTypeList;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import java.util.List;

/**
 * 支付选择方式
 * Created by Kind on 2017/4/19.
 */

public class PayListAct extends BaseActivity implements View.OnClickListener {

    public static boolean bPayOkFlag;

    private PayGood payGood;
    private BasePayPannel payAlipayPannel, payWXPannel, payPhonecardPannel;

    private TextView paylist_help_txt;
    private boolean help_txt = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_paylistact);
        setBackView(R.id.base_title_back, "支付订单");

        initView();
    }

    private void initView() {
        payGood = (PayGood) getIntent().getSerializableExtra("payGood");
        if (payGood == null) {
            PToast.showShort(R.string.request_error);
            this.finish();
            return;
        }

        TextView paylist_title = (TextView) findViewById(R.id.paylist_title);

        paylist_title.setText(Html.fromHtml("订单信息：" + payGood.getPay_name() + " " + "<font color='#fd6c8e'>" + payGood.getPay_money() + "</font>" + " 元"));

        LinearLayout pay_listView = (LinearLayout) findViewById(R.id.paytype_list);
        payAlipayPannel = new PayAlipayPannel(this, payGood);
        payWXPannel = new PayWXPannel(this, payGood);
        payPhonecardPannel = new PayPhoneCardPannel(this, payGood);

        PayTypeList payTypeList = ModuleMgr.getCommonMgr().getCommonConfig().getPayTypeList();
        List<PayTypeList.PayType> payTypes = payTypeList == null ? null : payTypeList.getPayTypes();
        if (null != payTypes && payTypes.size() > 0) {
            for (PayTypeList.PayType temp : payTypes) {

                if (GoodsConstant.PAY_TYPE_WECHAT_NAME.equals(temp.getName())) { // 微信支付
                    pay_listView.addView(payWXPannel.getContentView());
                } else if (GoodsConstant.PAY_TYPE_ALIPAY_NAME.equals(temp.getName())) {// 支付宝支付
                    pay_listView.addView(payAlipayPannel.getContentView());
                } else if (GoodsConstant.PAY_TYPE_OTHER_NAME.equals(temp.getName())) { // 其他支付

                }
            }
        } else {
            pay_listView.addView(payAlipayPannel.getContentView());
            pay_listView.addView(payWXPannel.getContentView());
        }

        findViewById(R.id.paylist_qq).setOnClickListener(this);
        paylist_help_txt = (TextView) findViewById(R.id.paylist_help_txt);
        findViewById(R.id.paylist_help).setOnClickListener(this);

        pay_listView.addView(payPhonecardPannel.getContentView());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.paylist_qq: {
                UIShow.showPrivateChatAct(this, MailSpecialID.customerService.getSpecialID(), "");
            }
            case R.id.paylist_help: {
                if (help_txt) {
                    paylist_help_txt.setVisibility(View.VISIBLE);
                    help_txt = false;
                } else {
                    paylist_help_txt.setVisibility(View.GONE);
                    help_txt = true;
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //通知刷个人资料  在
        MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
    }

    public void result() {
        setResult(Constant.BACK_PAYLISTACT);
        this.finish();
    }

}
