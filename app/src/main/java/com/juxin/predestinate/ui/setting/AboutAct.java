package com.juxin.predestinate.ui.setting;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;

import org.json.JSONObject;

/**
 * 关于页面
 */
public class AboutAct extends BaseActivity implements OnClickListener {

    private int iCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_activity_about);
        setBackView(getResources().getString(R.string.title_about));
        initView();
    }

    private void initView() {
        String email = getResources().getString(R.string.txt_email);
        TextView tv_email = (TextView) this.findViewById(R.id.txt_about_email);
        TextView tv_ver = (TextView) this.findViewById(R.id.txt_about_vers);

        View img_logo = this.findViewById(R.id.img_logo);
        img_logo.setOnClickListener(this);
        tv_ver.setText("V" + ModuleMgr.getAppMgr().getVerName());
        tv_email.setText(email);
        ((TextView) findViewById(R.id.tv_customerservice_phone)).setText("0731-1231124444");//TODO 获取客服手机
        findViewById(R.id.ll_open_qq_btn).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logo://彩蛋，点击进入用户搜索
                iCount++;
                if (iCount > 5) {
                    UIShow.showSearchTestActivity(AboutAct.this);
                }
                break;
            case R.id.ll_open_qq_btn://在线客服qq交流
                LoadingDialog.show(AboutAct.this);
                ModuleMgr.getCommonMgr().getCustomerserviceQQ(new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        LoadingDialog.closeLoadingDialog();
                        if (!response.isOk()) {
                            PToast.showShort(response.getMsg());
                            return;
                        }
                        JSONObject jsonObject = response.getResponseJson();
                        String qq = jsonObject.optString("qq");
                        UIShow.showQQService(AboutAct.this, qq);
                    }
                });
                break;
            default:
                break;
        }
    }
}