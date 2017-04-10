package com.juxin.predestinate.ui.xiaoyou.zanshi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 等级控件
 * Created by zm on 2016/8/23
 */
public class LevelView extends LinearLayout {

    //    private String[] boyText = {"初来乍到", "出手大方", "一掷千金", "挥金如土", "腰缠万贯", "锦衣玉食", "名门望族", "商贾巨子", "富可敌国", "富甲天下"};
    //    private String[] girlText = {"初见世面", "天生丽质", "红粉佳人", "小家碧玉", "大家闺秀", "百花魁首", "一笑倾城", "国色天香", "绝世无双", "貌若天仙"};
    private final Context context;
    private TextView tv_level_num, tv_level_tip;
    private LinearLayout ll_ring;
    private final int maxLevel = 10;

    public LevelView(Context context) {
        this(context, null);
    }

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.y2_levelview_ex, this);
//        ll_ring = (LinearLayout) view.findViewById(R.id.ll_ring);
//        tv_level_num = (TextView) view.findViewById(R.id.tv_level_num);
//        tv_level_tip = (TextView) view.findViewById(R.id.tv_level_tip);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * 设置等级
     *@param  gender 用户性别
     * @param experience 经验值
     */
    public void setLevelView(long experience,int gender) {
//        int ExPercent;
//        int position = 0;
//        int level = CommonConfig.getInstance().getLevel(experience, CommonConfig.getInstance().getBoyLevelInfo());
//        if (level > 0) {
//            position = level - 1;
//        }
//        int currentEx = CommonConfig.getInstance().getBoyLevelInfo().get(position).getExperience();//当前等级经验值
//        if (level < maxLevel) {
//            int nextEx = CommonConfig.getInstance().getBoyLevelInfo().get(position + 1).getExperience();
//            ExPercent = (int) (100.0 / ((float) (nextEx - currentEx) / (float) (experience - currentEx)));
//        } else {
//            ExPercent = 100;
//        }
//        RingGraph ringGraph = new RingGraph(context, new float[]{ExPercent, 100 - ExPercent});
//        ll_ring.addView(ringGraph);
//        String beforetxt = "V";
//        SpannableStringBuilder builder = new SpannableStringBuilder(beforetxt);
//        StyleSpan span = new StyleSpan(Typeface.ITALIC);
//        builder.setSpan(span, 0, beforetxt.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE); //设置前面的字体
//        builder.append(level + "");
//        tv_level_num.setText(builder);
//        tv_level_tip.setText(gender == 1 ? CommonConfig.getInstance().getBoyLevelInfo().get(position).getTip() : CommonConfig.getInstance().getGirlLevelInfo().get(position).getTip());
    }
}