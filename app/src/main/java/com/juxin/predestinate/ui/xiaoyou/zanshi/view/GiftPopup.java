package com.juxin.predestinate.ui.xiaoyou.zanshi.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.adapter.GiftAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.GiftsList;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息红包弹框
 * Created by zm on 2017/3/.
 */
public class GiftPopup extends PopupWindow implements View.OnClickListener {

    private TextView txvAllStone,txvPay,txvNeedStone,txvSendNum,txvSend,txvLeft,txvRight;
    private LinearLayout  llSendNum;//礼物数量
    private RecyclerView rlvGift;//赠送礼物
    private List arrGifts = new ArrayList();
    private String[] strNames = new String[]{"棒棒糖","泰迪熊","巧克力","烟花","玫瑰","香水","钻戒","跑车",};
    private int[] intimacys = new int[]{10,18,28,66,98,198,520,9999};
    private int[] icons = new int[]{R.drawable.p1_gift02,R.drawable.p1_gift01,R.drawable.p1_gift06,R.drawable.p1_gift07,R.drawable.p1_gift05,R.drawable.p1_gift04,R.drawable.p1_gift03,R.drawable.p1_gift08,};
    private GiftAdapter mGiftAdapter;
    private Context mContext;

    public GiftPopup(Context context) {
        this(context, null);
    }

    public GiftPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context);
        setBackgroundDrawable(null);
    }

    //    public GiftPopup() {
//        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
//        setGravity(Gravity.CENTER);
//        setDialogSizeRatio(-2, -2);
//        setCancelable(false);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        setContentView(R.layout.p1_bottom_gif_dialog);
//        View contentView = getContentView();
//        initView(contentView);
//        return contentView;
//    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.p1_bottom_gif_dialog, null);
        txvAllStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_allstone);
        txvNeedStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_needstone);
        txvSendNum = (TextView) contentView.findViewById(R.id.bottom_gif_txv_sendnum);
        llSendNum = (LinearLayout) contentView.findViewById(R.id.bottom_gif_ll_sendnum);
        txvLeft = (TextView)contentView.findViewById(R.id.bottom_gif_txv_left);
        txvRight = (TextView)contentView.findViewById(R.id.bottom_gif_txv_right);
        GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvGift = (RecyclerView) contentView.findViewById(R.id.bottom_gif_rlv_gif);
        rlvGift.setLayoutManager(layoutManager);

        contentView.findViewById(R.id.bottom_gif_txv_pay).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_send).setOnClickListener(this);
        mGiftAdapter = new GiftAdapter();
        rlvGift.setAdapter(mGiftAdapter);
        initGifts();
        this.setContentView(contentView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        txvLeft.setText("<");
        txvRight.setText(">");
        //txvAllStone.setText(ModuleMgr.getCenterMgr().getMyInfo().getMoney()+"");
        mGiftAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {

            }
        });
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
                UIShow.showGoodsDiamondAct(mContext);
                break;
            case R.id.bottom_gif_txv_send:
                //// TODO: 2017/3/31  赠送礼物逻辑
                this.dismiss();
                PToast.showShort("赠送礼物");
                break;
        }
    }

    public void show(){
        this.showAtLocation(getContentView(), Gravity.BOTTOM,0,0);
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
//        Log.e("TTTTTTBBB",mGiftAdapter.getList().size()+"||||");
    }
}
