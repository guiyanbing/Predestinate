package com.juxin.predestinate.ui.plaza;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;

import static com.juxin.predestinate.module.logic.tips.TipsBarMsg.Square_Page;

/**
 * 广场
 * Created by Kind on 2017/3/20.
 */

public class PlazaFragment extends BaseFragment {
    private LinearLayout viewGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.plaza_fragment);
        setTopView();
        setNotice();
        return getContentView();
    }


    private void setTopView() {
        setTitle(getString(R.string.plaza_title));
        setTitleRightImg(R.drawable.r1_reg_camera, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort("打开发布");
            }
        });
    }

    private void setNotice() {
        viewGroup = (LinearLayout) findViewById(R.id.plaza_title_notice);
        ModuleMgr.getTipsBarMgr().attach(Square_Page, viewGroup, null);
    }
}
