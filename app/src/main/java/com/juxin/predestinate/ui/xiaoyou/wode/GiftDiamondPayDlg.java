package com.juxin.predestinate.ui.xiaoyou.wode;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.xiaoyou.wode.adapter.GiftGridviewSmallAdapter;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.GiftsList;

import java.util.ArrayList;
import java.util.List;

public class GiftDiamondPayDlg extends Dialog implements View.OnClickListener, RequestComplete {
    private Context mContext;
    private View btn_diamond10, btn_diamond60, btn_diamond100, btn_diamond300, btn_diamond500, btn_diamond1000;
    private ImageView img_select_10, img_select_60, img_select_100, img_select_300, img_select_500, img_select_1000;
    private View btn_wx_pay;
    private View btn_ali_pay;
    private View btn_other_pay;
    private int pay_id = 37;
    private int pay_type = 1;
    private ImageView img_wx_select;
    private ImageView img_ali_select;
    private ImageView img_other_select;
    private ImageView img_user_head;
    private TextView dlg_diamond_tv_decdiamod;
    private String avatar, msg, nickname;

    //GIft
    private GiftViewPagerAdapter gvpAdapter;
    private List<GridView> mLists;
    private ViewPager mViewPager;
    private int index = 0;
    private List<GiftsList.GiftInfo> mListGift;
    private LinearLayout llMid;
    private List<ImageView> lv;
    private int pageCount;
    public GiftsList.GiftInfo selectGift;
//    private IGiftSend iGiftSend;
    private TextView tv_money;

    public GiftDiamondPayDlg(Context context, String avatar, String msg, String nickname) {
        this(context);
        this.avatar = avatar;
        this.msg = msg;
        this.nickname = nickname;
        mContext = context;
        initView(context);
        if (ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts().size() == 0) {
//            String json = AppCtx.executeLoadMeConcernListTask(mContext, this, TASK_TYPE_GETLIST);
//            initGiftList(json);
            ModuleMgr.getCommonMgr().requestgetGifts();
        } else {
            mListGift = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
            initViewGrid();
        }
        //当前钻石数
        if (ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() > 0) {
            tv_money.setText("当前钻石：" + ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() + "");
        } else {
            executeDiamondTask();
        }
        selectVipTypeBtn(R.id.dlg_diamond_ll_diamond10);
    }

    public GiftDiamondPayDlg(Context context) {
        this(context, R.style.dialog);
    }

    public GiftDiamondPayDlg(Context context, int theme) {
        super(context, theme);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.f1_dlg_diamond_gift_pay, null);
        setContentView(view);

        img_user_head = (ImageView) findViewById(R.id.dlg_diamond_head);
//        PhotoUtils.loadPhotoFitCenter(mContext, avatar, img_user_head);
        ImageLoader.loadCenterCrop(getContext(), avatar, img_user_head);



        ((TextView) findViewById(R.id.dlg_diamond_nickname)).setText(nickname);
        ((TextView) findViewById(R.id.dlg_diamond_msg)).setText(msg);

        dlg_diamond_tv_decdiamod = (TextView) findViewById(R.id.dlg_diamond_tv_decdiamod);
        view.findViewById(R.id.btn_diamond_ok).setOnClickListener(this);

        btn_diamond10 = view.findViewById(R.id.dlg_diamond_ll_diamond10);
        btn_diamond60 = view.findViewById(R.id.dlg_diamond_ll_diamond60);
        btn_diamond100 = view.findViewById(R.id.dlg_diamond_ll_diamond100);
        btn_diamond300 = view.findViewById(R.id.dlg_diamond_ll_diamond300);
        btn_diamond500 = view.findViewById(R.id.dlg_diamond_ll_diamond500);
        btn_diamond1000 = view.findViewById(R.id.dlg_diamond_ll_diamond1000);

        btn_wx_pay = view.findViewById(R.id.btn_wx_pay);
        btn_ali_pay = view.findViewById(R.id.btn_ali_pay);
        btn_other_pay = view.findViewById(R.id.btn_other_pay);

        img_select_10 = (ImageView) view.findViewById(R.id.dlg_diamond_img_select10);
        img_select_60 = (ImageView) view.findViewById(R.id.dlg_diamond_img_select60);
        img_select_100 = (ImageView) view.findViewById(R.id.dlg_diamond_img_select100);
        img_select_300 = (ImageView) view.findViewById(R.id.dlg_diamond_img_select300);
        img_select_500 = (ImageView) view.findViewById(R.id.dlg_diamond_img_select500);
        img_select_1000 = (ImageView) view.findViewById(R.id.dlg_diamond_img_select1000);


        img_wx_select = (ImageView) view.findViewById(R.id.img_wx_select);
        img_ali_select = (ImageView) view.findViewById(R.id.img_ali_select);
        img_other_select = (ImageView) view.findViewById(R.id.img_other_select);


        selectVipTypeBtn(R.id.user_id);
        selectPayTypeBtn(R.id.btn_wx_pay);

        btn_diamond10.setOnClickListener(this);
        btn_diamond60.setOnClickListener(this);
        btn_diamond100.setOnClickListener(this);
        btn_diamond300.setOnClickListener(this);
        btn_diamond500.setOnClickListener(this);
        btn_diamond1000.setOnClickListener(this);


        btn_wx_pay.setOnClickListener(this);
        btn_ali_pay.setOnClickListener(this);
        btn_other_pay.setOnClickListener(this);

        setCanceledOnTouchOutside(true);

        ///Gift
        mViewPager = (ViewPager) findViewById(R.id.vp_gift_main);
        llMid = (LinearLayout) findViewById(R.id.ll_gift_main_mid);
        tv_money = (TextView) findViewById(R.id.dlg_diamond_money);

        initSelectAndPay();
    }

    private void selectVipTypeBtn(int btnId) {
        btn_diamond10.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_diamond60.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_diamond100.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_diamond300.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_diamond500.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_diamond1000.setBackgroundResource(R.drawable.f1_btn_rectangle_std);

        img_select_10.setImageResource(R.drawable.f1_btn_unselect);
        img_select_60.setImageResource(R.drawable.f1_btn_unselect);
        img_select_100.setImageResource(R.drawable.f1_btn_unselect);
        img_select_300.setImageResource(R.drawable.f1_btn_unselect);
        img_select_500.setImageResource(R.drawable.f1_btn_unselect);
        img_select_1000.setImageResource(R.drawable.f1_btn_unselect);


        switch (btnId) {
            case R.id.dlg_diamond_ll_diamond10:
                pay_id = 56;
                btn_diamond10.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_select_10.setImageResource(R.drawable.f1_btn_select);
                break;
            case R.id.dlg_diamond_ll_diamond60:
                pay_id = 57;
                btn_diamond60.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_select_60.setImageResource(R.drawable.f1_btn_select);
                break;
            case R.id.dlg_diamond_ll_diamond100:
                pay_id = 58;
                btn_diamond100.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_select_100.setImageResource(R.drawable.f1_btn_select);
                break;
            case R.id.dlg_diamond_ll_diamond300:
                pay_id = 59;
                btn_diamond300.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_select_300.setImageResource(R.drawable.f1_btn_select);
                break;
            case R.id.dlg_diamond_ll_diamond500:
                pay_id = 60;
                btn_diamond500.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_select_500.setImageResource(R.drawable.f1_btn_select);
                break;
            case R.id.dlg_diamond_ll_diamond1000:
                pay_id = 61;
                btn_diamond1000.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_select_1000.setImageResource(R.drawable.f1_btn_select);
                break;
        }
    }

    private void initSelectAndPay(){
        if (selectGift == null){
            return;
        }
        if (ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() - selectGift.getMoney() < 0) {
            findViewById(R.id.ll_gift_pay).setVisibility(View.VISIBLE);
            dlg_diamond_tv_decdiamod.setText((selectGift.getMoney() -ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum()) + "钻石");
        } else {
            findViewById(R.id.ll_gift_pay).setVisibility(View.GONE);
        }
    }

    private void selectPayTypeBtn(int btnId) {
        btn_wx_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_ali_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_std);
        btn_other_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_std);

        img_wx_select.setVisibility(View.GONE);
        img_ali_select.setVisibility(View.GONE);
        img_other_select.setVisibility(View.GONE);

        switch (btnId) {
            case R.id.btn_wx_pay:
                pay_type = 6;
                btn_wx_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_wx_select.setImageResource(R.drawable.f1_btn_select);
                img_wx_select.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_ali_pay:
                pay_type = 1;
                btn_ali_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_ali_select.setImageResource(R.drawable.f1_btn_select);
                img_ali_select.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_other_pay:
                pay_type = -1;
                btn_other_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_other_select.setImageResource(R.drawable.f1_btn_select);
                img_other_select.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlg_diamond_ll_diamond10:
            case R.id.dlg_diamond_ll_diamond60:
            case R.id.dlg_diamond_ll_diamond100:
            case R.id.dlg_diamond_ll_diamond500:
            case R.id.dlg_diamond_ll_diamond300:
            case R.id.dlg_diamond_ll_diamond1000:
                selectVipTypeBtn(v.getId());
                break;
            case R.id.btn_wx_pay:
            case R.id.btn_ali_pay:
            case R.id.btn_other_pay:
                selectPayTypeBtn(v.getId());
                break;
            case R.id.btn_diamond_ok:
                onSendGift();
                break;
        }
    }

    //Gift
    private void initDot(int select) {
        if (pageCount == 0)
            return;
        if (null == lv) {
            lv = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                ImageView iv = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12, 12);
                layoutParams.setMargins(10, 0, 10, 0);
                iv.setLayoutParams(layoutParams);
                iv.setBackgroundResource(R.drawable.f1_dot_normal);
                lv.add(iv);
                llMid.addView(iv);
            }
        }
        for (int i = 0; i < lv.size(); i++) {
            lv.get(i).setBackgroundResource(R.drawable.f1_dot_normal);
        }
        lv.get(select).setBackgroundResource(R.drawable.f1_dot_focused);
    }

    public void initSelect() {
        if (null != mListGift) {
            if (mListGift.size() > 0) {
                for (int i = 0; i < mListGift.size(); i++) {
                    mListGift.get(i).setIsSelect(false);
                }
                mListGift.get(0).setIsSelect(true);
                selectGift = mListGift.get(0);
                initSelectAndPay();
                if (null != mLists) {
                    if (null != mLists.get(index)) {
                        GiftGridviewSmallAdapter gvGift = (GiftGridviewSmallAdapter) mLists.get(index).getAdapter();
                        gvGift.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void initGiftData() {
        mViewPager.addOnPageChangeListener(new MyOnPageChanger());
        gvpAdapter = new GiftViewPagerAdapter(mContext, mLists);
        mViewPager.setAdapter(gvpAdapter);
    }

    private void executeDiamondTask() {
        //// TODO: 2017/4/27  获取钻石余额
        ModuleMgr.getCommonMgr().getMyDiamand(this);
    }


//    public IGiftSend getiGiftSend() {
//        return iGiftSend;
//    }
//
//    public void setiGiftSend(IGiftSend iGiftSend) {
//        this.iGiftSend = iGiftSend;
//    }

    private void initGridView() {
        if (null == mListGift) {
            return;
        }
        pageCount = (int) Math.ceil(mListGift.size() / 8.0f);
        mLists = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            GridView gv = new GridView(mContext);
            final GiftGridviewSmallAdapter gvGift = new GiftGridviewSmallAdapter(mContext, mListGift, i);
            gv.setAdapter(gvGift);
            gv.setGravity(Gravity.CENTER);
            gv.setClickable(true);
            gv.setFocusable(false);
            gv.setNumColumns(4);
            gv.setSelector(R.color.transparent);
            gv.setFadingEdgeLength(0);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int select = index * 8 + position;
                    for (int j = 0; j < mListGift.size(); j++) {
                        mListGift.get(j).setIsSelect(false);
                    }
                    mListGift.get(select).setIsSelect(true);
                    selectGift = mListGift.get(select);
                    gvGift.notifyDataSetChanged();
                    initSelectAndPay();
                }
            });
            mLists.add(gv);
        }
        initGiftData();
    }

    private void initViewGrid() {
        if (null != mListGift) {
            if (mListGift.size() > 0) {
                initGridView();
                initDot(0);
                initSelect();
//                AppModel.getInstance().lstGift = mListGift;
            } else
                dismiss();
        }
    }

    private void initGiftList(String json) {
        ModuleMgr.getCommonMgr().getGiftLists().parseJson(json);
        if (ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts().size() > 0){
            initViewGrid();
            initSelect();
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
//        Log.e("TTTTTTTTTJJ", response.getResponseString() + "|||");
        if (response.getUrlParam() == UrlParam.getMyDiamand){
            if (response.isOk()){
                int diamonds = JsonUtil.getJsonObject(response.getResponseString()).optInt("diamand", 0);
                ModuleMgr.getCenterMgr().getMyInfo().setDiamondsSum(diamonds);
                tv_money.setText("当前钻石：" + diamonds);
            }
            return;
        }
        initGiftList(response.getResponseString());
    }

    class MyOnPageChanger implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            initDot(position);
            Log.i("aaa", "当前在第" + position + "页");
        }

        @Override
        public void onPageScrollStateChanged(int state) {


        }
    }

    public void onSendGift() {
        if (null == selectGift) {
            PToast.showShort("请选择礼物.");
        } else {
            if (ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() >= selectGift.getMoney()) {
//                if (iGiftSend != null)
//                    iGiftSend.onSend(selectGift);
                //发送消息
                dismiss();
            } else {
//                PToast.showShort("支付跳转");
                UIShow.showPayListAct((FragmentActivity)mContext, 56);
//                AppCtx.putPreference("fromVip", false);
//                UIHelper.showPaymentActEx(mContext, pay_id, pay_type, UIHelper.MONTHLYLETTERACT);
                dismiss();
//                if (iGiftSend != null)
//                    iGiftSend.onSendToPay(selectGift);
                dismiss();
            }
        }
    }
}