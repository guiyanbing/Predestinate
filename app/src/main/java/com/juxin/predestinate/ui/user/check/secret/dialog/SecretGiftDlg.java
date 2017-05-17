package com.juxin.predestinate.ui.user.check.secret.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 查看视频、相册赠送礼物弹框
 * Created by Su on 2017/5/17.
 */

public class SecretGiftDlg extends BaseActivity implements View.OnClickListener {

    private TextView tv_gift, tv_gift_diamonds;
    private ImageView iv_gift;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_user_secret_gift_dlg);

        initView();
    }

    private void initView() {
        tv_gift = (TextView) findViewById(R.id.tv_gift_count);
        iv_gift = (ImageView) findViewById(R.id.iv_gift_pic);
        tv_gift_diamonds = (TextView) findViewById(R.id.tv_gift_diamonds);

        findViewById(R.id.tv_send).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_send:
                // TODO 判断钻石数量，足则请求解锁接口，不足弹充值弹框

                UIShow.showSecretDiamondDlg(this);
                break;

            case R.id.btn_close:
                finish();
                break;
        }
    }
}
