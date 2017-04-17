package com.juxin.predestinate.ui.xiaoyou.zanshi.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.GiftNumAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.GiftsNumInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息红包弹框
 * Created by zm on 2017/3/.
 */
public class GiftNumPopup extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<GiftsNumInfo> arrGiftNumInfos = new ArrayList<>();
    private GiftNumAdapter mGiftNumAdapter;
    private int[] nums = new int[]{1314,520,188,66,30,10,1};
    private String[] morals = new String[]{"一生一世","我爱你","要抱抱","一切顺利","想你","十全十美","一心一意"};
    private OnSelectNumChangedListener mOnSelectNumChangedListener;

    public GiftNumPopup(Context context) {
        this(context, null);
    }

    public GiftNumPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context);
        setBackgroundDrawable(null);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.p1_bottom_gif_num_pop, null);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.bottom_gif_num_pop_rlv);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mGiftNumAdapter = new GiftNumAdapter(this);
        mRecyclerView.setAdapter(mGiftNumAdapter);
        initGifts();
        this.setContentView(contentView);

        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
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
        for (int i = 0;i<morals.length;i++){
            GiftsNumInfo info = new GiftsNumInfo();
            info.setNum(nums[i]);
            info.setMoral(morals[i]);
            arrGiftNumInfos.add(info);
        }
        mGiftNumAdapter.setList(arrGiftNumInfos);
    }

    public void setOnSelectNumChangedListener(OnSelectNumChangedListener onSelectNumChangedListener){
        this.mOnSelectNumChangedListener = onSelectNumChangedListener;
        mGiftNumAdapter.setOnSelectNumChangedListener(mOnSelectNumChangedListener);
    }

    public interface OnSelectNumChangedListener{
        void onSelectNumChanged(int num);
    }
}