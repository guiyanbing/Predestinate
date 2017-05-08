package com.juxin.predestinate.ui.xiaoyou.wode;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatRecordPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.wode.adapter.GiftGridviewAskForAdapter;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.GiftsList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 礼物
 *
 * @author IQQ
 */
public class AskforGiftDialog extends Dialog implements OnClickListener, RequestComplete {

    private ChatAdapter.ChatInstance chatInstance = null;
    private GiftViewPagerAdapter gvpAdapter;
    private List<GridView> mLists;
    private ViewPager mViewPager;
    private int index = 0;
    private List<GiftsList.GiftInfo> mListGift;
    private Context mContext;
    private LinearLayout llMid;
    private List<ImageView> lv;
    private int pageCount;
    private String other, channel;
    public GiftsList.GiftInfo selectGift;
    private TextView tv_pagesize;
//    private IGiftSend iGiftSend;
    private ImageView btn_input_change;
    private EditText tv_edit;
    private Button btn_voice;
    private LinearLayout ll_voice, ll_voice_main;
    private boolean isEdit = true;
    private ChatRecordPanel recordPanel;
    private RelativeLayout rMain;
    private int voice_length;
    private String voice_file, sVoiceUrl;
//    private ChatPanelVoice chatPanelVoice;
    private long timeCount;

    public AskforGiftDialog(Context context, String otheruserid, String channelid) {
        this(context, R.style.No_Background);
        other = otheruserid;
        channel = channelid;
        mContext = context;
        initView();
        //获取礼物列表 待完善
        mListGift = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
        if (mListGift.size() > 0) {//配置中已经返回
            initViewGrid();
        } else {//配置中未返回就去请求配置
//            AppCtx.execLstGiftTask(mContext, TASK_TYPE_GETLIST, this);
            ModuleMgr.getCommonMgr().requestgetGifts(this);
        }
//        if (ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() == 0) {//请求钻石数目
//            ModuleMgr.getCommonMgr().getMyDiamand(this);
////            executeDiamondTask();
//        }
    }

    public AskforGiftDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    protected AskforGiftDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    private void initView() {
        setContentView(R.layout.f1_askfor_gift_dlg);
        rMain = (RelativeLayout) findViewById(R.id.rl_ask_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_gift_main);
        llMid = (LinearLayout) findViewById(R.id.ll_gift_main_mid);
        tv_pagesize = (TextView) findViewById(R.id.tv_gift_pagesize);
        findViewById(R.id.tv_gift_main_send).setOnClickListener(this);

        btn_input_change = (ImageView) findViewById(R.id.askfor_btn_voice);
        btn_voice = (Button) findViewById(R.id.askfor_btn_record);
        tv_edit = (EditText) findViewById(R.id.askfor_editmsg);
        ll_voice = (LinearLayout) findViewById(R.id.askfor_voice);
        ll_voice_main = (LinearLayout) findViewById(R.id.askfor_voice_parent);

        if (chatInstance == null) {
            chatInstance = new ChatAdapter.ChatInstance();
            chatInstance.context = getContext();
            chatInstance.chatAdapter = new ChatAdapter();
            chatInstance.chatAdapter.setChatInstance(chatInstance);
        }

        recordPanel = new ChatRecordPanel(mContext,chatInstance);
        rMain.addView(recordPanel.getContentView());
        recordPanel.setVisibility(View.GONE);
        chatInstance.chatContentAdapter = null;
        chatInstance.chatInputPanel = null;
        chatInstance.chatExtendPanel = null;

        initEdit();
        btn_input_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEdit();
            }
        });

        btn_voice.setOnTouchListener(onTouchListener);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            float PosY = event.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:

//                    channelId = getChatInstance().chatAdapter.getChannelId();
//                    whisperId = getChatInstance().chatAdapter.getWhisperId();

//                    chatRecordPanel = getChatInstance().chatRecordPanelUser;
//
//                    if (chatRecordPanel == null) {
//                        chatRecordPanel = getChatInstance().chatRecordPanel;
//                    }

                    if (timeCount == 0 || System.currentTimeMillis() - timeCount > 500) {
                        recordPanel.onTouch(action, 0f);
                    } else {
                        MMLog.autoDebug("---ChatInputPanel--->点击间隔<500ms，过于频繁");
                    }


                    btn_voice.setText("松开结束");
                    btn_voice.setPressed(true);
                    recordPanel.setVisibility(View.VISIBLE);
//                    ChatMediaRecord.getInstance().startRecordVoice(onRecordListener);
                    break;

                case MotionEvent.ACTION_MOVE:
//                    recordPanel.initYPos(PosY);
                    recordPanel.onTouch(action, event.getY());
                    break;

                case MotionEvent.ACTION_UP:
//                    chatVoiceRecord.setText("按下 开始");
//                    chatVoiceRecord.setPressed(false);



//                    chatRecordPanel.onTouch(action, event.getY(), "", "");//




                    timeCount = System.currentTimeMillis();

                    btn_voice.setText("按住说话");
                    btn_voice.setPressed(false);
//                    outVoice(PosY);
                    recordPanel.setVisibility(View.GONE);
                    break;

                default:
//                    chatVoiceRecord.setText("按下 开始");
//                    chatVoiceRecord.setPressed(false);
                    recordPanel.onTouch(action, 0f);
//                    timeCount = System.currentTimeMillis();
                    btn_voice.setText("按住说话");
                    btn_voice.setPressed(false);
                    return false;
            }
            return true;
        }
    };

//    private void uploadVoice(final String sPath) {
//        BaseUtil.uploadFile(mContext, true, sPath, "voice", new OnAsyncCallback() {
//            @Override
//            public void requestSuccess(BaseAsyncTask task) {
//                try {
//                    String str = task.getJsonResult();
//                    JSONObject jso = new JSONObject(str);
//                    if ("ok".equals(jso.optString("status")) && jso.optJSONObject("res") != null) {
//                        sVoiceUrl = jso.optJSONObject("res").optString("file_http_path");
//                        //缓存到本地，避免本地播放时下载
//                        AppCtx.myCache.put(sVoiceUrl, sPath);
//                        switchVoice();
//                    } else {
//                        T.showLong(mContext, "语音发送失败!");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void requestFail(BaseAsyncTask task, Exception e) {
//                T.showLong(mContext, "语音录取失败，请稍后再试");
//            }
//        });
//    }


//    private void switchVoice() {
//        tv_edit.setVisibility(View.GONE);
//        btn_voice.setVisibility(View.GONE);
//        ll_voice_main.setVisibility(View.VISIBLE);
//        if (chatPanelVoice == null)
//            chatPanelVoice = new ChatPanelVoice(mContext, ll_voice);
//        //chatPanelVoice.setParent(ll_voice);
//        chatPanelVoice.initData(sVoiceUrl, String.valueOf(voice_length), false);
//    }

    private void initEdit() {
        btn_input_change.setBackgroundResource(R.drawable.f1_btn_voice_speak);
        tv_edit.setVisibility(View.VISIBLE);
        btn_voice.setVisibility(View.GONE);
        ll_voice_main.setVisibility(View.GONE);
    }

    private void changeEdit() {
        if (isEdit) {
            isEdit = false;
            btn_input_change.setBackgroundResource(R.drawable.p1_chat_c01);
            tv_edit.setVisibility(View.GONE);
            btn_voice.setVisibility(View.VISIBLE);
            ll_voice_main.setVisibility(View.GONE);
            voice_file = "";
            sVoiceUrl = "";
            voice_length = 0;
        } else {
            isEdit = true;
            initEdit();
        }
    }


    private void initDot(int select) {
        if (true) return;
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
            for (int i = 0; i < mListGift.size(); i++) {
                mListGift.get(i).setIsSelect(false);
            }
            mListGift.get(0).setIsSelect(true);
            selectGift = mListGift.get(0);
            if (null != mLists) {
                if (null != mLists.get(index)) {
                    GiftGridviewAskForAdapter gvGift = (GiftGridviewAskForAdapter) mLists.get(index).getAdapter();
                    gvGift.notifyDataSetChanged();
                }
            }
            tv_pagesize.setText(Html.fromHtml("<font color=#fd6c8e>" + 1 + "</font>/" + mLists.size()));
        }
    }

    private void initData() {
        mViewPager.addOnPageChangeListener(new MyOnPageChanger());
        gvpAdapter = new GiftViewPagerAdapter(mContext, mLists);
        mViewPager.setAdapter(gvpAdapter);
    }

    private void initGridView() {
        if (null == mListGift) {
            return;
        }
        pageCount = (int) Math.ceil(mListGift.size() / 6.0f);
        mLists = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            GridView gv = new GridView(mContext);
            final GiftGridviewAskForAdapter gvGift = new GiftGridviewAskForAdapter(mContext, mListGift, i);
            gv.setAdapter(gvGift);
            gv.setGravity(Gravity.CENTER);
            gv.setClickable(true);
            gv.setFocusable(false);
            gv.setNumColumns(3);
            gv.setSelector(R.color.transparent);
            gv.setFadingEdgeLength(0);
            gv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int select = index * 6 + position;
                    for (int j = 0; j < mListGift.size(); j++) {
                        mListGift.get(j).setIsSelect(false);
                    }
                    mListGift.get(select).setIsSelect(true);
                    selectGift = mListGift.get(select);
                    gvGift.notifyDataSetChanged();
                }
            });
            mLists.add(gv);
        }
        initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_gift_main_send:
                if (null == selectGift) {
                    PToast.showShort("请选择礼物.");
                } else {
                    if (isEdit && TextUtils.isEmpty(tv_edit.getText().toString().trim())) {
                        PToast.showShort("请输入你想说的话");
                    } else if (!isEdit && TextUtils.isEmpty(sVoiceUrl)) {
                        PToast.showShort("请录入你想说的话");
                    } else
                        onSend();
                }
                break;
        }
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

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.getGiftLists){
            if (response.isOk()){
                GiftsList info = new GiftsList();
                info.parseJson(response.getResponseString());
                ModuleMgr.getCommonMgr().setGiftLists(info);
                mListGift = ModuleMgr.getCommonMgr().getGiftLists().getArrCommonGifts();
                initGridView();
            }
            return;
        }
//        if (response.getUrlParam() == UrlParam.getMyDiamand){
//            if (response.isOk()){
//                int diamonds = JsonUtil.getJsonObject(response.getResponseString()).optInt("diamand", 0);
//                ModuleMgr.getCenterMgr().getMyInfo().setDiamondsSum(diamonds);
//            }
//        }
    }


    class MyOnPageChanger implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            index = position;
            initDot(position);
            Log.i("aaa", "当前在第" + position + "页");
            ((GiftGridviewAskForAdapter) mLists.get(index).getAdapter()).notifyDataSetChanged();

            tv_pagesize.setText(Html.fromHtml("<font color=#fd6c8e>" + (position + 1) + "</font>/" + mLists.size()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void onSend() {
        Map<String, Object> requestObject = new HashMap<>();
        requestObject.put("gift_id", selectGift.getId());
        if (isEdit) {
            requestObject.put("content", tv_edit.getText().toString());
        } else {
            requestObject.put("voice_url", sVoiceUrl.toLowerCase().replace(".amr", ""));
            requestObject.put("voice_len", voice_length);
        }
        // 发送请求
        ModuleMgr.getCommonMgr().CMDRequest("POST", true, UrlParam.qunFa.getFinalUrl(), requestObject, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()){
                    PToast.showShort("发送成功");
                    return;
                }
                PToast.showShort("发送失败,请稍后重试");
            }
        });
        dismiss();
    }
}