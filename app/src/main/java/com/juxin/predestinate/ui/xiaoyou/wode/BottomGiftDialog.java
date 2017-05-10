package com.juxin.predestinate.ui.xiaoyou.wode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
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
import com.juxin.predestinate.ui.xiaoyou.wode.bean.SendGiftResultInfo;
import com.juxin.predestinate.ui.xiaoyou.zanshi.view.GiftNumPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * 送礼物弹框
 * Created by zm on 2017/3/.
 */
public class BottomGiftDialog extends BaseDialogFragment implements View.OnClickListener,GiftNumPopup.OnSelectNumChangedListener,RequestComplete,TextWatcher {

    private TextView txvAllStone,txvPay,txvNeedStone,txvSend,txvLeft,txvRight;
    private EditText txvSendNum;
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
    private int position = -1;

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
        initGifts();
        for (int i = 0;i<arrGifts.size();i++){
            arrGifts.get(i).setIsShow(false);
        }
        initView(contentView);
        return contentView;
    }

    private void initView(final View contentView) {
        txvAllStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_allstone);
        txvNeedStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_needstone);
        txvSendNum = (EditText) contentView.findViewById(R.id.bottom_gif_txv_sendnum);
        llSendNum = (LinearLayout) contentView.findViewById(R.id.bottom_gif_ll_sendnum);
        txvLeft = (TextView)contentView.findViewById(R.id.bottom_gif_txv_left);
        txvRight = (TextView)contentView.findViewById(R.id.bottom_gif_txv_right);
        mCustomRecyclerView = (CustomRecyclerView) contentView.findViewById(R.id.bottom_gif_rlv_gif);
        pageIndicatorView = (PageIndicatorView) contentView.findViewById(R.id.bottom_gif_rlv_gif_indicator);
        findViewById(R.id.bottom_gif_view_blank).setOnClickListener(this);
        findViewById(R.id.bottom_gif_rl_top).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_pay).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_send).setOnClickListener(this);

        rlvGift = mCustomRecyclerView.getPageRecyclerView();
        rlvGift.setIndicator(pageIndicatorView);

        mGiftAdapterCallBack = new GiftAdapterCallBack(getContext(),this,arrGifts);
        mGiftAdapter = rlvGift.new PageAdapter(arrGifts,mGiftAdapterCallBack);
        mGiftAdapterCallBack.setPageAdapter(mGiftAdapter);
        rlvGift.setPageSize(2, 4);
        rlvGift.setPageMargin(30);
        rlvGift.setAdapter(mGiftAdapter);
        txvLeft.setText("<");
        txvRight.setText(">");
        txvAllStone.setText(ModuleMgr.getCenterMgr().getMyInfo().getDiamand() + "");
        mCustomRecyclerView.showPageRecyclerView();
        rlvGift.setAdapter(mGiftAdapter);
        txvSendNum.addTextChangedListener(this);
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
                int needStone = Integer.valueOf(txvNeedStone.getText().toString());
                if (needStone > ModuleMgr.getCenterMgr().getMyInfo().getDiamand()){
                    UIShow.showGoodsDiamondDialog(getContext());
                    return;
                }
                if (position == -1){
                    PToast.showShort(getContext().getString(R.string.please_select_a_gift));
                    return;
                }
                ModuleMgr.getCommonMgr().sendGift(uid+"",arrGifts.get(position).getId()+"",this);
                break;
            case R.id.bottom_gif_rl_top:
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
        this.position = position;
        txvSendNum.setText(num+"");
        txvSendNum.setSelection(txvSendNum.length());
        txvNeedStone.setText(sum+"");
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        SendGiftResultInfo info = new SendGiftResultInfo();
        info.parseJson(response.getResponseString());
        PToast.showShort(info.getMsg()+"");
    }
    private CharSequence temp;
    private int selectionStart;
    private int selectionEnd;
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        temp = charSequence;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            selectionStart = txvSendNum.getSelectionStart();
            selectionEnd = txvSendNum.getSelectionEnd();
            if (position > -1){
                int need = Integer.valueOf(editable.toString());
                txvNeedStone.setText(need*arrGifts.get(position).getMoney()+"");
            }
            if (temp.length() > 4) {
                editable.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionEnd;
                txvSendNum.setText(editable);
                txvSendNum.setSelection(tempSelection);//设置光标在最后
            }
        }catch (Exception e){
            PLogger.e("BottomGiftDialog---------"+e.toString());
        }
    }
}