package com.juxin.predestinate.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.base.BaseFragment;

/**
 * 我的
 * Created by Kind on 2017/3/20.
 */

public class UserFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.user_fragment);

        return getContentView();
    }
}
