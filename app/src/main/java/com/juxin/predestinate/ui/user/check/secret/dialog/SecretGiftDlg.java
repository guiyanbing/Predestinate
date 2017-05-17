package com.juxin.predestinate.ui.user.check.secret.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.util.CenterConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查看视频、相册赠送礼物弹框
 * Created by Su on 2017/5/17.
 */

public class SecretGiftDlg extends BaseActivity implements View.OnClickListener {

    private TextView tv_gift, tv_gift_diamonds;
    private ImageView iv_gift;

    private UserProfile userProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_user_secret_gift_dlg);

        initView();
    }

    private void initView() {
        userProfile = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
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
//                if () {
//                    unlock();
//                    return;
//                }
                UIShow.showSecretDiamondDlg(this);
                break;

            case R.id.btn_close:
                finish();
                break;
        }
    }

    /**
     * 解锁视频
     */
    private void unlock() {
        LoadingDialog.show(this, getString(R.string.user_secret_media_unlock));
        ModuleMgr.getCenterMgr().reqUnlockVideo(userProfile.getUid(), 0, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    PToast.showShort(getString(R.string.user_secret_media_lock_suc));
                    try {
                        JSONObject jsonObject = new JSONObject(response.getResponseString());
                        ModuleMgr.getCenterMgr().getMyInfo().setDiamondsSum(jsonObject.optInt("diamand"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                PToast.showShort(getString(R.string.user_secret_media_lock_fail));
                finish();
            }
        });
    }
}
