package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.bean.my.SendGiftResultInfo;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.my.GiftHelper;
import com.juxin.predestinate.ui.user.my.adapter.GiftGridviewSmallAdapter;
import com.juxin.predestinate.ui.user.my.adapter.GiftViewPagerAdapter;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;

import java.util.ArrayList;
import java.util.List;

public class GiftDiamondPayDlg extends BaseActivity implements View.OnClickListener, RequestComplete,GiftHelper.OnRequestGiftListCallback {

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
    private TextView tv_money;

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
    private String toUid;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        avatar = getIntent().getStringExtra("avatar");
        msg = getIntent().getStringExtra("msg");
        nickname = getIntent().getStringExtra("nickname");
        toUid = getIntent().getStringExtra("toUid");
        GiftDiamondPayAct(mContext, avatar, msg, nickname);
    }

    public void GiftDiamondPayAct(Context context, String avatar, String msg, String nickname) {
        this.avatar = avatar;
        this.msg = msg;
        this.nickname = nickname;
        mContext = context;
        initView(context);
        if (ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts().size() == 0) {
            ModuleMgr.getCommonMgr().requestGiftList(this);
        } else {
            mListGift = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
            initViewGrid();
            reSetGift();
        }
        //当前钻石数
        tv_money.setText(getString(R.string.the_current_diamond) + ModuleMgr.getCenterMgr().getMyInfo().getDiamand() + "");
//        if (ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() > 0) {
//        } else {
//            ModuleMgr.getCommonMgr().getMyDiamand(this);
//        }
        selectVipTypeBtn(R.id.dlg_diamond_ll_diamond10);
    }

    private void initView(Context context) {
        setContentView(R.layout.f1_dlg_diamond_gift_pay);
        img_user_head = (ImageView) findViewById(R.id.dlg_diamond_head);
        ImageLoader.loadCircle(mContext, avatar, img_user_head);

        ((TextView) findViewById(R.id.dlg_diamond_nickname)).setText(nickname);
        ((TextView) findViewById(R.id.dlg_diamond_msg)).setText(msg);

        dlg_diamond_tv_decdiamod = (TextView) findViewById(R.id.dlg_diamond_tv_decdiamod);
        findViewById(R.id.btn_diamond_ok).setOnClickListener(this);

        btn_diamond10 = findViewById(R.id.dlg_diamond_ll_diamond10);
        btn_diamond60 = findViewById(R.id.dlg_diamond_ll_diamond60);
        btn_diamond100 = findViewById(R.id.dlg_diamond_ll_diamond100);
        btn_diamond300 = findViewById(R.id.dlg_diamond_ll_diamond300);
        btn_diamond500 = findViewById(R.id.dlg_diamond_ll_diamond500);
        btn_diamond1000 = findViewById(R.id.dlg_diamond_ll_diamond1000);

        btn_wx_pay = findViewById(R.id.btn_wx_pay);
        btn_ali_pay = findViewById(R.id.btn_ali_pay);
        btn_other_pay = findViewById(R.id.btn_other_pay);

        img_select_10 = (ImageView) findViewById(R.id.dlg_diamond_img_select10);
        img_select_60 = (ImageView) findViewById(R.id.dlg_diamond_img_select60);
        img_select_100 = (ImageView) findViewById(R.id.dlg_diamond_img_select100);
        img_select_300 = (ImageView) findViewById(R.id.dlg_diamond_img_select300);
        img_select_500 = (ImageView) findViewById(R.id.dlg_diamond_img_select500);
        img_select_1000 = (ImageView) findViewById(R.id.dlg_diamond_img_select1000);

        img_wx_select = (ImageView) findViewById(R.id.img_wx_select);
        img_ali_select = (ImageView) findViewById(R.id.img_ali_select);
        img_other_select = (ImageView) findViewById(R.id.img_other_select);

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
        findViewById(R.id.iv_dlg_close).setOnClickListener(this);

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
        if (ModuleMgr.getCenterMgr().getMyInfo().getDiamand() - selectGift.getMoney() < 0) {
            findViewById(R.id.ll_gift_pay).setVisibility(View.VISIBLE);
            dlg_diamond_tv_decdiamod.setText((selectGift.getMoney() -ModuleMgr.getCenterMgr().getMyInfo().getDiamand()) + "钻石");
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
                pay_type = GoodsConstant.PAY_TYPE_WECHAT;
                btn_wx_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_wx_select.setImageResource(R.drawable.f1_btn_select);
                img_wx_select.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_ali_pay:
                pay_type = GoodsConstant.PAY_TYPE_ALIPAY;
                btn_ali_pay.setBackgroundResource(R.drawable.f1_btn_rectangle_select);
                img_ali_select.setImageResource(R.drawable.f1_btn_select);
                img_ali_select.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_other_pay:
                pay_type = GoodsConstant.PAY_TYPE_OTHER;
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
            case R.id.iv_dlg_close:
                finish();
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
                       notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void notifyDataSetChanged(){
        for (int i = 0 ;i < mLists.size();i++){
            ((GiftGridviewSmallAdapter)mLists.get(i).getAdapter()).notifyDataSetChanged();
        }
    }

    private void initGiftData() {
        mViewPager.addOnPageChangeListener(new MyOnPageChanger());
        gvpAdapter = new GiftViewPagerAdapter(mContext, mLists);
        mViewPager.setAdapter(gvpAdapter);
    }

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
                    position = select;
                    reSetGift();
                    mListGift.get(select).setIsSelect(true);
                    selectGift = mListGift.get(select);
                    notifyDataSetChanged();
                    initSelectAndPay();
                }
            });
            mLists.add(gv);
        }
        initGiftData();
    }

    private void reSetGift(){
        for (int j = 0; j < mListGift.size(); j++) {
            mListGift.get(j).setIsSelect(false);
        }
    }

    private void initViewGrid() {
        if (null != mListGift) {
            if (mListGift.size() > 0) {
                initGridView();
                initDot(0);
                initSelect();
            } else
                finish();
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
                tv_money.setText(mContext.getString(R.string.the_current_diamond) + diamonds);
            }
            return;
        }
        if (response.getUrlParam() == UrlParam.sendGift){
            SendGiftResultInfo info = new SendGiftResultInfo();
            info.parseJson(response.getResponseString());
            ModuleMgr.getCenterMgr().getMyInfo().setDiamand(info.getDiamand());
            ModuleMgr.getChatMgr().sendGiftMsg(null, toUid + "",mListGift.get(position).getId() , 1, 0);
            PToast.showShort(info.getMsg() + "");
            return;
        }
        initGiftList(response.getResponseString());
    }

    @Override
    public void onRequestGiftListCallback(boolean isOk) {
        if (isOk){
            mListGift = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
            if (mListGift.size() > 0){
                initViewGrid();
                initSelect();
            }
        }
    }

    class MyOnPageChanger implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            initDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void onSendGift() {
        if (null == selectGift) {
            PToast.showShort(mContext.getString(R.string.please_select_a_gift));
        } else {
            if (ModuleMgr.getCenterMgr().getMyInfo().getDiamand() >= selectGift.getMoney()) {
                //发送消息
                ModuleMgr.getCommonMgr().sendGift(toUid,selectGift.getId()+"",1,2,this);
                finish();
            } else {
                UIShow.showPayListAct((FragmentActivity)mContext, pay_id);
                finish();
            }
        }
    }
}