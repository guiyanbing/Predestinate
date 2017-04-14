package com.juxin.predestinate.ui.xiaoyou.zanshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;

import java.util.ArrayList;


/**
 * 用户等级控件
 * Created by zm on 2016/8/23
 */
public class LevelDialogView extends LinearLayout implements View.OnClickListener{

    private final Context context;
    private LevelView mLevelView;
    private TextView txvIntimacy;
    private TextView txvMoney;
    private ImageView imgHelp;
    private RelativeLayout rlBottom;
    private ImageView imgLevel1,imgLevel2,imgLevel3,imgLevel4,imgLevel5,imgLevel6;
    private TextView txvLevel1,txvLevel2,txvLevel3,txvLevel4,txvLevel5,txvLevel6;

    private ArrayList<ImageView> imgViews = new ArrayList<>();
    private ArrayList<TextView> txvViews = new ArrayList<>();

    private int[] levels = new int[]{R.drawable.p1_lv01a, R.drawable.p1_lv01b,
            R.drawable.p1_lv02a, R.drawable.p1_lv02b, R.drawable.p1_lv03a, R.drawable.p1_lv03b,
            R.drawable.p1_lv04a, R.drawable.p1_lv04b, R.drawable.p1_lv05a, R.drawable.p1_lv05b,
            R.drawable.p1_lv06a, R.drawable.p1_lv06b};
    private int[] intimacys = new int[]{10,100,500,1000,10000,50000};

    public LevelDialogView(Context context) {
        this(context, null);
    }

    public LevelDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.p1_level_dialog, this);
        mLevelView = (LevelView) findViewById(R.id.chat_level_level);
        txvIntimacy = (TextView) findViewById(R.id.chat_level_txv_intimacy);
        txvMoney = (TextView) findViewById(R.id.chat_level_txv_money);
        imgHelp = (ImageView) findViewById(R.id.chat_level_img_help);
        rlBottom = (RelativeLayout) findViewById(R.id.chat_level_rl_bottom);

        imgLevel1 = (ImageView) findViewById(R.id.chat_level_img_level1);
        imgLevel2 = (ImageView) findViewById(R.id.chat_level_img_level2);
        imgLevel3 = (ImageView) findViewById(R.id.chat_level_img_level3);
        imgLevel4 = (ImageView) findViewById(R.id.chat_level_img_level4);
        imgLevel5 = (ImageView) findViewById(R.id.chat_level_img_level5);
        imgLevel6 = (ImageView) findViewById(R.id.chat_level_img_level6);
        imgViews.add(imgLevel1);
        imgViews.add(imgLevel2);
        imgViews.add(imgLevel3);
        imgViews.add(imgLevel4);
        imgViews.add(imgLevel5);
        imgViews.add(imgLevel6);

        txvLevel1 = (TextView) findViewById(R.id.chat_level_txv_level1);
        txvLevel2 = (TextView) findViewById(R.id.chat_level_txv_level2);
        txvLevel3 = (TextView) findViewById(R.id.chat_level_txv_level3);
        txvLevel4 = (TextView) findViewById(R.id.chat_level_txv_level4);
        txvLevel5 = (TextView) findViewById(R.id.chat_level_txv_level5);
        txvLevel6 = (TextView) findViewById(R.id.chat_level_txv_level6);
        txvViews.add(txvLevel1);
        txvViews.add(txvLevel2);
        txvViews.add(txvLevel3);
        txvViews.add(txvLevel4);
        txvViews.add(txvLevel5);
        txvViews.add(txvLevel6);
        rlBottom.setVisibility(View.GONE);
    }

    public void setLevel(int level){
        for (int i = 0 ;i < levels.length/2 ;i++){
            if (level <= i-1){
                imgViews.get(i).setBackgroundResource(levels[i*2-1]);
            }else {
                imgViews.get(i).setBackgroundResource(levels[(i-1)*2]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_level_level:
                rlBottom.setVisibility(View.VISIBLE);
                break;
            case R.id.chat_level_img_help:
                // TODO: 2017/4/12 跳转到等级说明页
                break;
        }
    }
}