package com.juxin.predestinate.ui.xiaoyou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;


/**
 * 标签详情页头部
 * Created by zm on 2016/8/23
 */
public class IntimacyDetailsHeadView extends LinearLayout {

    private TextView friend_label_head_txv_name,friend_label_head_txv_num;

    public IntimacyDetailsHeadView(Context context) {
        this(context,null);
    }

    public IntimacyDetailsHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.p1_xiaoyou_intimacy_head,this);
        setOrientation(VERTICAL);
        friend_label_head_txv_name = (TextView) findViewById(R.id.friend_label_head_txv_name);
        friend_label_head_txv_num = (TextView) findViewById(R.id.friend_label_head_txv_num);
    }

    /**
     * 设置头部信息
     * @param name 标签名称
     * @param num  标签下的人数信息
     */
    public void setHeadData(String name,String num){
        //设置头部信息
        friend_label_head_txv_name.setText(name);
        String str = getResources().getText(R.string.intimate_member)+"";
        friend_label_head_txv_num.setText(str+(num));
    }

}