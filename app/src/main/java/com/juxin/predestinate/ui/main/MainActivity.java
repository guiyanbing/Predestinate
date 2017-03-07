package com.juxin.predestinate.ui.main;

import android.os.Bundle;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.base.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.test.TestActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIShow.show(MainActivity.this, TestActivity.class);
            }
        });
    }
}
