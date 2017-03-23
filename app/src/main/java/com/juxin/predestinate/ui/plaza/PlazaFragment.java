package com.juxin.predestinate.ui.plaza;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;

/**
 * 广场
 * Created by Kind on 2017/3/20.
 */

public class PlazaFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.plaza_fragment);

        return getContentView();
    }

}
