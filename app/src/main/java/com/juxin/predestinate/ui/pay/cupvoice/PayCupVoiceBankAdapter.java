package com.juxin.predestinate.ui.pay.cupvoice;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.UIShow;
import java.util.List;

/**
 * Created by Kind on 2017/4/27.
 */

public class PayCupVoiceBankAdapter extends ExBaseAdapter<String> implements View.OnClickListener {

    private PayGood payGood;

    public PayCupVoiceBankAdapter(Context context, List<String> datas, PayGood payGood) {
        super(context, datas);
        this.payGood = payGood;
    }

    @Override
    public int getCount() {
        if (getList() == null)
            return 0;
        else
            return getList().size() % 2 == 0 ? getList().size() / 2 : getList().size() / 2 + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.f1_pay_voice_item);
            vh = new ViewHolder();
            vh.tv_left = (TextView) convertView.findViewById(R.id.pay_voice_item_left);
            vh.tv_right = (TextView) convertView.findViewById(R.id.pay_voice_item_right);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String string_left, string_rigth = null;
        string_left = getList().get(position * 2);
        if (position * 2 + 1 < getList().size()) {
            string_rigth = getList().get(position * 2 + 1);
        }

        if (string_left != null) {
            vh.tv_left.setVisibility(View.VISIBLE);
            vh.tv_left.setText(string_left);
            vh.tv_left.setTag(string_left);
            vh.tv_left.setOnClickListener(this);
        } else {
            vh.tv_left.setVisibility(View.INVISIBLE);
        }

        if (string_rigth != null) {
            vh.tv_right.setVisibility(View.VISIBLE);
            vh.tv_right.setText(string_rigth);
            vh.tv_right.setTag(string_rigth);
            vh.tv_right.setOnClickListener(this);
        } else {
            vh.tv_right.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_left, tv_right;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_voice_item_left:
            case R.id.pay_voice_item_right:
                String bank_name = (String) v.getTag();
                UIShow.shoPayCupVoiceDetailAct((Activity) getContext(), payGood, bank_name, Constant.PAY_VOICEACT);
                break;
            default:
                break;
        }
    }
}