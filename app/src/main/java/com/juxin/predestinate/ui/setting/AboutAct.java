package com.juxin.predestinate.ui.setting;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;


/**
 * 关于页面
 */
public class AboutAct extends BaseActivity implements OnClickListener{

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
        TextView txtemail = (TextView) this.findViewById(R.id.txt_about_email);
        TextView txtVers = (TextView) this.findViewById(R.id.txt_about_vers);

        View imageview1 = this.findViewById(R.id.imageview1);
        imageview1.setOnClickListener(this);
        txtVers.setText("V" + ModuleMgr.getAppMgr().getVerName());
        txtemail.setText(Html.fromHtml(getResources().getString(R.string.txt_email_desc)+"<font color='#666666'>" + email + "</font>"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview1://彩蛋，点击进入用户搜索
                iCount++;
//                if (iCount > 5) {
//                    UIHelper.showSearchActivity(AboutAct.this);
//                }
                break;
        }
    }
}