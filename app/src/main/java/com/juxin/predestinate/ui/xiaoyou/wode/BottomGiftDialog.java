package com.juxin.predestinate.ui.xiaoyou.wode;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.pagerecyeler.PageIndicatorView;
import com.juxin.predestinate.third.pagerecyeler.PageRecyclerView;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.xiaoyou.wode.adapter.GiftAdapterCallBack;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.GiftsList;
import com.juxin.predestinate.ui.xiaoyou.zanshi.view.GiftNumPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * 送礼物弹框
 * Created by zm on 2017/3/.
 */
public class BottomGiftDialog extends BaseDialogFragment implements View.OnClickListener,GiftNumPopup.OnSelectNumChangedListener,RequestComplete {

    private TextView txvAllStone,txvPay,txvNeedStone,txvSendNum,txvSend,txvLeft,txvRight;
    private LinearLayout  llSendNum;//礼物数量
    private PageRecyclerView rlvGift;//赠送礼物
    private CustomRecyclerView mCustomRecyclerView;
    private PageIndicatorView pageIndicatorView;
    private List<GiftsList.GiftInfo> arrGifts = new ArrayList();
    private int[] icons = new int[]{R.drawable.p1_gift02,R.drawable.p1_gift01,R.drawable.p1_gift06,R.drawable.p1_gift07,R.drawable.p1_gift05,R.drawable.p1_gift04,R.drawable.p1_gift03,R.drawable.p1_gift08,};
    private PageRecyclerView.PageAdapter mGiftAdapter;
    private GiftNumPopup mGiftNumPopup;
    private GiftAdapterCallBack mGiftAdapterCallBack;
    private long uid;//收礼物的uid
    private int position;

    public BottomGiftDialog() {
        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
        setGravity(Gravity.CENTER);
        setDialogSizeRatio(-2, -2);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.p1_bottom_gif_dialog);
        View contentView = getContentView();
        initView(contentView);
        return contentView;
    }

    private void initView(final View contentView) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.p1_bottom_gif_dialog, null);
        txvAllStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_allstone);
        txvNeedStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_needstone);
        txvSendNum = (TextView) contentView.findViewById(R.id.bottom_gif_txv_sendnum);
        llSendNum = (LinearLayout) contentView.findViewById(R.id.bottom_gif_ll_sendnum);
        txvLeft = (TextView)contentView.findViewById(R.id.bottom_gif_txv_left);
        txvRight = (TextView)contentView.findViewById(R.id.bottom_gif_txv_right);
        mCustomRecyclerView = (CustomRecyclerView) contentView.findViewById(R.id.bottom_gif_rlv_gif);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.bottom_gif_rlv_gif_indicator);
        findViewById(R.id.bottom_gif_view_blank).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_pay).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_send).setOnClickListener(this);
        initGifts();

        rlvGift = mCustomRecyclerView.getPageRecyclerView();
        rlvGift.setIndicator(pageIndicatorView);
        mCustomRecyclerView.showPageRecyclerView();

        //        rlvGift.setLayoutManager(layoutManager);

//        mGiftNumPopup = new GiftNumPopup(getContext());
//        mGiftNumPopup.setOnSelectNumChangedListener(this);
//        mGiftNumPopup.setOutsideTouchable(true);


        mGiftAdapterCallBack = new GiftAdapterCallBack(getContext(),this,arrGifts,mGiftAdapter);
        mGiftAdapter = rlvGift.new PageAdapter(arrGifts,mGiftAdapterCallBack);
        rlvGift.setPageSize(2,4);
        rlvGift.setPageMargin(30);
        rlvGift.setAdapter(mGiftAdapter);
        txvLeft.setText("<");
        txvRight.setText(">");
        txvAllStone.setText(ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum()+"");
        mCustomRecyclerView.showPageRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_gif_view_blank:
                dismiss();
                break;
            case R.id.bottom_gif_txv_pay:
                UIShow.showGoodsDiamondDialog(getContext());
                break;
            case R.id.bottom_gif_txv_send:
                //// TODO: 2017/3/31  赠送礼物逻辑
                ModuleMgr.getCommonMgr().sendGift(uid+"",arrGifts.get(position).getId()+"",this);
                PToast.showShort("赠送礼物");
                break;
        }
    }

    public void setToId(long to_id){
        this.uid = to_id;
    }

    private void initGifts(){
        arrGifts = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
    }

    @Override
    public void onSelectNumChanged(int num) {
        txvSendNum.setText(num+"");
        txvNeedStone.setText(num+"");
    }
    public void onSelectNumChanged(int num,int sum,int position) {
//        Log.e("TTTTTTTTTGG", num + "|||");
        this.position = position;
        txvSendNum.setText(num+"");
        txvNeedStone.setText(sum+"");
    }

    @Override
    public void onRequestComplete(HttpResponse response) {

    }
}
