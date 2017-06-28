package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.bean.my.SendGiftResultInfo;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.local.statistics.StatisticsDiscovery;
import com.juxin.predestinate.module.local.statistics.StatisticsMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.module.util.my.GiftHelper;
import com.juxin.predestinate.ui.user.my.adapter.GiftAdapter;
import com.juxin.predestinate.ui.user.my.adapter.GiftViewPagerAdapter;
import com.juxin.predestinate.ui.user.my.view.CustomViewPager;
import com.juxin.predestinate.ui.user.my.view.GiftPopView;
import com.juxin.predestinate.ui.user.my.view.PageIndicatorView;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 送礼物弹框
 * Created by zm on 2017/3/.
 */
public class BottomGiftDialog extends BaseDialogFragment implements View.OnClickListener, RequestComplete, TextWatcher, ViewPager.OnPageChangeListener, GiftHelper.OnRequestGiftListCallback, GiftPopView.OnNumSelectedChangedListener {

    private TextView txvAllStone, txvPay, txvNeedStone, txvSend, txvLeft, txvRight;
    private TextView txvSendNum;
    private RelativeLayout llSendNum;//礼物数量
    private PageIndicatorView pageIndicatorView;
    private List<GiftsList.GiftInfo> arrGifts = new ArrayList();
    private long uid;//收礼物的uid
    private String channel_uid;  // 统计用
    private int position = -1;
    private int pageCount;
    private List<GridView> mLists;
    private int curPage = 0;//当前页
    private CustomViewPager vpViews;
    private GiftViewPagerAdapter gvpAdapter;
    private int oldPosition = -1;
    private int sum = 0;
    private int onePageCount = 0;
    private GiftPopView gpvPop;
    private int num;
    private Context mContext;

    private int fromTag = Constant.OPEN_FROM_INFO;

    public BottomGiftDialog() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(-2, -2);
        setCancelable(true);
    }

    public void setCtx(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.p1_bottom_gif_dialog);
        View contentView = getContentView();
        onePageCount = ((ModuleMgr.getAppMgr().getScreenWidth() - UIUtil.dp2px(17)) / UIUtil.dp2px(95)) * 2;//根据屏幕宽度计算列数
        initGifts();
        reSetData();
        initView(contentView);
        return contentView;
    }

    //初始化UI
    private void initView(final View contentView) {
        txvAllStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_allstone);
        txvNeedStone = (TextView) contentView.findViewById(R.id.bottom_gif_txv_needstone);
        txvSendNum = (TextView) contentView.findViewById(R.id.bottom_gif_txv_sendnum);
        llSendNum = (RelativeLayout) contentView.findViewById(R.id.bottom_gif_ll_sendnum);
        txvLeft = (TextView) contentView.findViewById(R.id.bottom_gif_txv_left);
        txvRight = (TextView) contentView.findViewById(R.id.bottom_gif_txv_right);
        pageIndicatorView = (PageIndicatorView) contentView.findViewById(R.id.bottom_gif_rlv_gif_indicator);
        vpViews = (CustomViewPager) contentView.findViewById(R.id.bottom_gift_vp_view);
        gpvPop = (GiftPopView) contentView.findViewById(R.id.bottom_gif_gpv);
        vpViews.setRow(2);

        //设置dot的size和背景
        pageIndicatorView.setSelectDot(12, 8, R.drawable.f1_dot_select);//选中
        pageIndicatorView.setUnselectDot(8, 8, R.drawable.f1_dot_unselect);//未选中

        findViewById(R.id.bottom_gif_view_blank).setOnClickListener(this);
        findViewById(R.id.bottom_gif_rl_top).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_pay).setOnClickListener(this);
        contentView.findViewById(R.id.bottom_gif_txv_send).setOnClickListener(clickListener);
        txvLeft.setText("<");
        txvRight.setText(">");
        txvAllStone.setText(ModuleMgr.getCenterMgr().getMyInfo().getDiamand() + "");
        txvSendNum.addTextChangedListener(this);
        txvSendNum.setOnClickListener(this);
        gpvPop.setOnNumSelectedChanged(this);
        initGridView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_gif_view_blank://点击空白消失
                dismiss();
                break;
            case R.id.bottom_gif_txv_pay://跳转充值页面
                if (getFromTag() == Constant.OPEN_FROM_HOT) { //在热门界面打开
                    StatisticsDiscovery.onClickGiftPay(uid);
                } else if (getFromTag() == Constant.OPEN_FROM_CHAT_FRAME) {
                    Statistics.userBehavior(SendPoint.chatframe_tool_gift_pay, uid);
                }
                dismiss();
                UIShow.showGoodsDiamondDialogAndTag(getContext(), getFromTag(), uid, channel_uid);
                break;
            case R.id.bottom_gif_txv_sendnum:
                gpvPop.setVisibility(View.VISIBLE);
                break;
            case R.id.bottom_gif_rl_top:
                gpvPop.setVisibility(View.GONE);
                break;
        }
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.bottom_gif_txv_send://发送礼物按钮逻辑
                    int needStone = Integer.valueOf(txvNeedStone.getText().toString());
                    if (needStone > ModuleMgr.getCenterMgr().getMyInfo().getDiamand()) {
                        dismiss();
                        UIShow.showGoodsDiamondDialog(getContext(), needStone - ModuleMgr.getCenterMgr().getMyInfo().getDiamand(),
                                getFromTag(), uid, channel_uid);
                        return;
                    }
                    if (position == -1) {//为选择礼物
                        PToast.showShort(getContext().getString(R.string.please_select_a_gift));
                        return;
                    }
                    StatisticsMessage.chatGiveGift(uid, arrGifts.get(position).getId(), arrGifts.get(position).getMoney());

                    //统计
                    if (getFromTag() == Constant.OPEN_FROM_HOT) {
                        StatisticsDiscovery.onGiveGift(uid, arrGifts.get(position).getId(), arrGifts.get(position).getMoney());
                    }
                    ModuleMgr.getChatMgr().sendGiftMsg("", uid + "", arrGifts.get(position).getId(), num, 1);

                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    public void setToId(long to_id, String channel_uid) {
        this.uid = to_id;//设置接收礼物方的uid
        this.channel_uid = channel_uid;
    }

    /**
     * 初始化礼物列表
     */
    private void initGifts() {
        arrGifts = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
        if (arrGifts.size() <= 0) {
            ModuleMgr.getCommonMgr().requestGiftList(this);
        }
    }

    private void initData() {
        //设置ViewPager监听
        vpViews.addOnPageChangeListener(this);
        gvpAdapter = new GiftViewPagerAdapter(getContext(), mLists);
        vpViews.setAdapter(gvpAdapter);
        pageIndicatorView.initIndicator(pageCount);
        if (pageCount > 1)
            pageIndicatorView.setSelectedPage(0);
    }

    private void initGridView() {
        pageCount = (int) Math.ceil(arrGifts.size() / (float) onePageCount);//计算总页数
        //分页数据处理
        mLists = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            final GridView gv = new GridView(getContext());
            final GiftAdapter gvGift = new GiftAdapter(getContext(), arrGifts, i, onePageCount);
            gv.setAdapter(gvGift);
            gv.setGravity(Gravity.CENTER);
            gv.setClickable(true);
            gv.setFocusable(false);
            gv.setNumColumns(onePageCount / 2);
            gv.setSelector(R.color.transparent);
            gv.setFadingEdgeLength(0);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int select = curPage * onePageCount + position;
                    if (select < arrGifts.size()) {
                        reSetData();
                        arrGifts.get(select).setIsSelect(true);//设置为选中状态
                        if (oldPosition == select) {
                            sum++;
                        } else {
                            sum = 1;
                        }
                        oldPosition = select;//记录前一个position的位置
                        BottomGiftDialog.this.position = oldPosition;
                        onSelectNumChanged(sum, sum * arrGifts.get(select).getMoney(), select);
                        for (int i = 0; i < mLists.size(); i++) {
                            ((GiftAdapter) mLists.get(i).getAdapter()).notifyDataSetChanged();
                        }
                    }
                }
            });
            mLists.add(gv);
        }
        initData();
    }

    /**
     * 将礼物的选择状态置为false
     */
    private void reSetData() {
        for (int j = 0; j < arrGifts.size(); j++) {
            arrGifts.get(j).setIsSelect(false);
        }
    }

    /**
     * 选择礼物时用于更新界面的方法
     *
     * @param num      礼物数量
     * @param sum      礼物所需的钻石数量
     * @param position 被选中礼物的位置
     */
    public void onSelectNumChanged(int num, int sum, int position) {
        this.position = position;
        this.num = num;
        txvSendNum.setText(num + "");
//        txvSendNum.setSelection(txvSendNum.length());
        txvNeedStone.setText(sum + "");
        gpvPop.setVisibility(View.GONE);
    }

    @Override
    public void onNumSelectedChanged(int num) {
        this.num = num;
        txvSendNum.setText(num + "");
        if (position > -1) {
            int sum = num * arrGifts.get(position).getMoney();
            txvNeedStone.setText(sum + "");
        }
        gpvPop.setVisibility(View.GONE);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        SendGiftResultInfo info = new SendGiftResultInfo();
        info.parseJson(response.getResponseString());
        ModuleMgr.getCenterMgr().getMyInfo().setDiamand(info.getDiamand());
        ModuleMgr.getChatMgr().sendGiftMsg(null, uid + "", arrGifts.get(position).getId(), num, 0);
        PToast.showShort(info.getMsg() + "");
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
            if (position > -1) {
                int need = Integer.valueOf(editable.toString());
                sum = need;
                txvNeedStone.setText(need * arrGifts.get(position).getMoney() + "");
            }
            if (temp.length() > 4) {
                editable.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionEnd;
                txvSendNum.setText(editable);
//                txvSendNum.setSelection(tempSelection);//设置光标在最后
            }
        } catch (Exception e) {
            txvNeedStone.setText(0 + "");
            PLogger.e("BottomGiftDialog---------" + e.toString());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curPage = position;
        pageIndicatorView.setSelectedPage(curPage);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRequestGiftListCallback(boolean isOk) {
        if (isOk) {
            arrGifts = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
            initGridView();
        }
    }

    public int getFromTag() {
        return fromTag;
    }

    public void setFromTag(int fromTag) {
        this.fromTag = fromTag;
    }
}