package com.juxin.predestinate.ui.xiaoyou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.TabGroupActivity;


/**
 * 标签详情页头部
 * Created by zm on 2016/8/23
 */
public class LabelDetailsHeadView extends LinearLayout implements RequestComplete {

    private TextView friend_label_head_txv_num;
    private EditText friend_label_head_txv_name;
    private long tab = -1;

    public LabelDetailsHeadView(Context context) {
        this(context, null);
    }

    public LabelDetailsHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.p1_xiaoyou_label_head, this);
        setOrientation(VERTICAL);
        friend_label_head_txv_name = (EditText) findViewById(R.id.friend_label_head_txv_name);
        friend_label_head_txv_num = (TextView) findViewById(R.id.friend_label_head_txv_num);
        int i = 0;
        if (TabGroupActivity.arrLabes.size() >= TabGroupActivity.arrLabes.get(TabGroupActivity.arrLabes.size()-1).getId() ){
            i = TabGroupActivity.arrLabes.size();
        }else {
            i = (int)TabGroupActivity.arrLabes.get(TabGroupActivity.arrLabes.size()-1).getId();
        }
        friend_label_head_txv_name.setText("新建标签"+i);
        friend_label_head_txv_name.setSelection(friend_label_head_txv_name.getText().length());
        friend_label_head_txv_name.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    //处理事件
                    ModuleMgr.getCommonMgr().ModifyTagGroup(tab, friend_label_head_txv_name.getText().toString(), LabelDetailsHeadView.this);
                    return true;
                }
                return false;
            }
        });
    }

    public String getName(){
        return friend_label_head_txv_name.getText().toString();
    }
    public void setTab(long tab){
        this.tab = tab;
    }
    /**
     * 设置头部信息
     * @param name 标签名称
     * @param num  标签下的人数信息
     */
    public void setHeadData(String name,String num){
        //设置头部信息
        friend_label_head_txv_name.setText(name);
        friend_label_head_txv_num.setText(num);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {

    }
}