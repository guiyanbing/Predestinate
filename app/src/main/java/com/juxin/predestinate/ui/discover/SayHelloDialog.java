package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/5/8.
 */

public class SayHelloDialog extends BaseDialogFragment implements View.OnClickListener {

    private List<UserInfoLightweight> data = new ArrayList<>();

    private ImageView iv_big;
    private ImageView iv_small1;
    private ImageView iv_small2;
    private ImageView iv_small3;
    private ImageView iv_small;

    public SayHelloDialog() {
        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
        setGravity(Gravity.CENTER);
        setDialogSizeRatio(0, 0);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_sayhello_dialog);
        initView();
        initData();
        return getContentView();
    }

    private void initData() {
        if (data.size() != 0) {
            ImageLoader.loadCenterCrop(getActivity(), data.get(0).getAvatar(), iv_small);
            ImageLoader.loadBlurImg(getActivity(), data.get(0).getAvatar(), 50, iv_big);
            ImageLoader.loadCenterCrop(getActivity(), data.get(1).getAvatar(), iv_small1);
            ImageLoader.loadCenterCrop(getActivity(), data.get(2).getAvatar(), iv_small2);
            ImageLoader.loadCenterCrop(getActivity(), data.get(3).getAvatar(), iv_small3);
        }
    }

    private void initView() {

        iv_big = (ImageView) findViewById(R.id.onkey_user_big);
        iv_small1 = (ImageView) findViewById(R.id.onkey_user_small1);
        iv_small2 = (ImageView) findViewById(R.id.onkey_user_small2);
        iv_small3 = (ImageView) findViewById(R.id.onkey_user_small3);
        iv_small = (ImageView) findViewById(R.id.onkey_small);
        findViewById(R.id.onkey_open_rl).setOnClickListener(this);

        TextView tv_btn_ok = (TextView) findViewById(R.id.onkey_open);
        tv_btn_ok.setText("一键打招呼");
        tv_btn_ok.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.onkey_open:
            case R.id.onkey_open_rl:
                batchsayhi_all();
                break;
        }
    }

    private void batchsayhi_all() {
        dismiss();
    }

    public void setData(List<UserInfoLightweight> data) {
        this.data = data;
    }
}
