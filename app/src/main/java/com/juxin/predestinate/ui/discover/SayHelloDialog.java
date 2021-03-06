package com.juxin.predestinate.ui.discover;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.statistics.StatisticsUser;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.UIUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhang on 2017/5/8.
 */

public class SayHelloDialog extends BaseDialogFragment implements View.OnClickListener {

    private static final int SAY_HELLO_MSG_WHAT = 100;

    private List<UserInfoLightweight> data = new ArrayList<>();  //外部不准调用该函数 此函数会动态改变长度
    private List<UserInfoLightweight> tmp = new ArrayList<>(); //外部调用、回调调用 用此函数

    private ImageView iv_big;
    private ImageView iv_small1;
    private ImageView iv_small2;
    private ImageView iv_small3;
    private ImageView iv_small;

    private boolean isShowing = false;

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
        setShowing(true);
        return getContentView();
    }

    private void initData() {
        if (data.size() != 0) {
            ImageLoader.loadBlur(getActivity(), data.get(0).getAvatar(), iv_big);
            ImageLoader.loadCircleAvatar(getActivity(), data.get(0).getAvatar(), iv_small, UIUtil.dip2px(getContext(), 2));
            ImageLoader.loadAvatar(getActivity(), data.get(1).getAvatar(), iv_small1);
            ImageLoader.loadAvatar(getActivity(), data.get(2).getAvatar(), iv_small2);
            ImageLoader.loadAvatar(getActivity(), data.get(3).getAvatar(), iv_small3);
        } else {
            ImageLoader.loadCircleAvatar(getActivity(), R.drawable.default_head, iv_small, UIUtil.dip2px(getContext(), 2));
            ImageLoader.loadAvatar(getActivity(), R.drawable.default_head, iv_small1);
            ImageLoader.loadAvatar(getActivity(), R.drawable.default_head, iv_small2);
            ImageLoader.loadAvatar(getActivity(), R.drawable.default_head, iv_small3);
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
        LoadingDialog.show(getActivity());
        handler.sendEmptyMessageDelayed(SAY_HELLO_MSG_WHAT, 200);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAY_HELLO_MSG_WHAT:
                    StatisticsUser.dailyOneKeySayHi(tmp);
                    toSayHello();
                    break;
            }
        }
    };

    private void toSayHello() {
        if (data.size() != 0) {
            Iterator<UserInfoLightweight> infos = data.iterator();
            while (infos.hasNext()) {
                UserInfoLightweight infoLightweight = infos.next();
                ModuleMgr.getChatMgr().sendSayHelloMsg(String.valueOf(infoLightweight.getUid()), getString(R.string.say_hello_txt),
                        infoLightweight.getKf_id(),
                        ModuleMgr.getCenterMgr().isRobot(infoLightweight.getKf_id()) ?
                                Constant.SAY_HELLO_TYPE_ROBOT : Constant.SAY_HELLO_TYPE_SIMPLE, null);
                infos.remove();
                handler.sendEmptyMessage(SAY_HELLO_MSG_WHAT);
                break;
            }
        } else {
            ModuleMgr.getCommonMgr().saveDateState(ModuleMgr.getCommonMgr().getSayHelloKey());
            MsgMgr.getInstance().sendMsg(MsgType.MT_Say_Hello_Notice, tmp);

            LoadingDialog.closeLoadingDialog();
            dismiss();
        }
    }

    public void setData(List<UserInfoLightweight> data) {
        this.data = data;
        this.tmp.addAll(data);
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        setShowing(false);
    }
}
