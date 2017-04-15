package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.ui.xiaoyou.adapter.GiftAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.GiftsList;

import java.util.ArrayList;
import java.util.List;

import static com.juxin.predestinate.R.id.bottom_gif_rlv_gif;

/**
 * 消息红包弹框
 * Created by zm on 2017/3/.
 */
public class BottomGiftDialog extends BaseDialogFragment implements View.OnClickListener {

    private TextView txvAllStone,txvPay,txvNeedStone,txvSendNum,txvSend;
    private LinearLayout  llSendNum;//礼物数量
    private RecyclerView rlvGift;//赠送礼物
    private List arrGifts = new ArrayList();
    private String[] strNames = new String[]{"棒棒糖","泰迪熊","巧克力","烟花","玫瑰","香水","钻戒","跑车",};
    private int[] intimacys = new int[]{10,18,28,66,98,198,520,9999};
    private int[] icons = new int[]{R.drawable.p1_gift02,R.drawable.p1_gift01,R.drawable.p1_gift06,R.drawable.p1_gift07,R.drawable.p1_gift05,R.drawable.p1_gift04,R.drawable.p1_gift03,R.drawable.p1_gift08,};
    private GiftAdapter mGiftAdapter;

    public BottomGiftDialog() {
        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
        setGravity(Gravity.CENTER);
        setDialogSizeRatio(-2, -2);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.p1_bottom_gif_dialog);
        View contentView = getContentView();
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {

        txvAllStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_allstone);
        txvNeedStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_needstone);
        txvSendNum = (TextView) contentView.findViewById(R.id.bottom_gif_txv_sendnum);
        llSendNum = (LinearLayout) contentView.findViewById(R.id.bottom_gif_ll_sendnum);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvGift = (RecyclerView) contentView.findViewById(bottom_gif_rlv_gif);
        rlvGift.setLayoutManager(layoutManager);

        contentView.findViewById(R.id.bottom_gif_txv_pay).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_send).setOnClickListener(this);
        mGiftAdapter = new GiftAdapter();
        rlvGift.setAdapter(mGiftAdapter);
        initGifts();
    }

    /**
     * 设置显示数据
     *
     * @param money 得到的钱
     */
    public void setData(double money) {
//        this.money = money;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_gif_txv_pay:
                //TODO 跳转充值
                break;
            case R.id.bottom_gif_txv_send:
                //// TODO: 2017/3/31  赠送礼物逻辑
                dismissAllowingStateLoss();
                PToast.showShort("赠送礼物");
                break;
        }
    }
    private void initGifts(){
        for (int i = 0;i<strNames.length;i++){
            GiftsList.GiftInfo info = new GiftsList.GiftInfo();
            info.setName(strNames[i]);
            info.setStone(intimacys[i]);
            info.setIntimacy(intimacys[i]);
            info.setIcon(icons[i]);
            arrGifts.add(info);
        }
        mGiftAdapter.setList(arrGifts);
        Log.e("TTTTTTBBB",mGiftAdapter.getList().size()+"||||");
    }
}
