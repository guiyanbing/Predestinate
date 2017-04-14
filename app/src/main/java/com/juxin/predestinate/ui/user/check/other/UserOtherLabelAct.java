package com.juxin.predestinate.ui.user.check.other;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.FlowLayout;

import java.util.List;

/**
 * 他人标签
 * Created by Su on 2017/4/13.
 */

public class UserOtherLabelAct extends BaseActivity implements View.OnClickListener {

    private List<String> labels; // 标签
    private FlowLayout mFlowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_other_label_act);

        initTitle();
        initView();
    }

    private void initTitle() {
        setBackView();
        setTitle(getString(R.string.user_other_label));
        setTitleRight(getString(R.string.user_info_save), this);
    }

    private void initView() {
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);

        initLabels();
    }

    /**
     * 印象标签
     */
    private void initLabels() {
        mFlowLayout.removeAllViews();
        if (labels != null) labels.clear();

        labels = ModuleMgr.getCenterMgr().getMyInfo().getImpressions();

        labels.add("标签1");
        labels.add("标签2");
        labels.add("标签3");
        labels.add("标签4");
        labels.add("标签5");
        labels.add("标签6");
        for (int i = 0; i < labels.size(); i++) {

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.common_label_layout, null);
            LinearLayout labelLayout = (LinearLayout) view.findViewById(R.id.label);
            labelLayout.setBackgroundResource(R.drawable.label_white_bg);

            TextView label_text = (TextView) view.findViewById(R.id.label_text);
            label_text.setText(labels.get(i));
            mFlowLayout.addView(view);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.base_title_right_txt:  // 保存
                break;
        }

    }
}
