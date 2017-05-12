package com.juxin.predestinate.ui.user.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;


/**
 * 提现成功
 * Created by LC on 2016/11/7.
 */

public class WithDrawSuccessAct extends BaseActivity implements View.OnClickListener {

    private Button btnSuccess;
    private TextView tvServiceQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_bind_card_success_act);
        initView();
    }

    private void initView() {
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.withdraw_succeed));
        btnSuccess = (Button) findViewById(R.id.btn_success);
        tvServiceQQ = (TextView) findViewById(R.id.tv_service_qq);

//        UIShow.showQQServer(this);
//        if (!TextUtils.isEmpty(QQ_Utils.getServiceQQ())) {
//            tvServiceQQ.setText(QQ_Utils.getServiceQQ());
//        }

        btnSuccess.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_success:
                this.finish();
                break;

            default:
                break;
        }
    }
}
