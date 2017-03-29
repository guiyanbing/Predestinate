package com.juxin.predestinate.ui.user.information;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * 编辑签名页面
 */
public class UserEditSignAct extends BaseActivity implements TextWatcher {
    private static final int MAX_INPUT_NUM = 120;     // 最大输入字数限制

    private EditText editTxt_sign_content;
    private TextView txt_sign_content_num;
    private ImageView img_sign_content_del;

    private String sign;
    private String signContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_edit_sign_act);

        initTitle();
        initView();
        fillData();
    }

    private void initTitle() {
        setBackView();
        setTitle(getResources().getString(R.string.user_info_edit_sign));
        setTitleRight(getResources().getString(R.string.user_info_save), new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInputInfo()) {
                    updateAndClose();
                }
            }
        });
    }

    private void initView() {
        this.editTxt_sign_content = (EditText) findViewById(R.id.editTxt_sign_content);
        this.editTxt_sign_content.setFilters(new InputFilter[]{new EmojiFilter()});
        this.editTxt_sign_content.addTextChangedListener(this);
        this.txt_sign_content_num = (TextView) findViewById(R.id.txt_sign_content_num);
        this.img_sign_content_del = (ImageView) findViewById(R.id.img_sign_content_del);
        this.img_sign_content_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editTxt_sign_content.setText("");// 清空所有输入的数据
            }
        });
    }

    private void fillData() {
        this.sign = getIntent().getStringExtra("sign");
        if ("我正在构思一个伟大的签名...".equals(sign)) {
            sign = "";
        }

        if (sign != null && !"".equals(sign)) {
            editTxt_sign_content.setText(sign);
            editTxt_sign_content.setSelection(sign.length());
            txt_sign_content_num.setText(format(sign.length()));

        } else {
            txt_sign_content_num.setText(format(0));
        }
    }

    private String format(int num) {
        return String.format(getString(R.string.user_info_edit_limit), num, MAX_INPUT_NUM);
    }

    private boolean checkInputInfo() {
        signContent = editTxt_sign_content.getText().toString().trim();
        if (TextUtils.isEmpty(signContent)) {
            MMToast.showShort("请输入你的内心独白");
            return true;
        }
        if (sign != null && signContent.equals(sign)) {
            MMToast.showShort("请编辑后再提交");
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int balanceNum = s.toString().length();
        if (balanceNum < 0) {
            balanceNum = 0;
            signContent = s.toString().substring(0, MAX_INPUT_NUM);
            editTxt_sign_content.setText(signContent);
            editTxt_sign_content.setSelection(MAX_INPUT_NUM);
            MMToast.showShort("已经不能再输入！");
        }
        txt_sign_content_num.setText(format(balanceNum));
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void updateAndClose() {
        String contact = editTxt_sign_content.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra("newValue", contact);
        setResult(UserInfoAct.UPDATE_USER_SIGN, intent);
        finish();
    }

    /**
     * Emoji 过滤器
     */
    static class EmojiFilter implements InputFilter {
        private static Set<String> filterSet = null;

        private static void addUnicodeRangeToSet(Set<String> set, int start, int end) {
            if (set == null) {
                return;
            }
            if (start > end) {
                return;
            }
            for (int i = start; i <= end; i++) {
                filterSet.add(new String(new int[]{i}, 0, 1));
            }
        }

        private static void addUnicodeRangeToSet(Set<String> set, int code) {
            if (set == null) {
                return;
            }
            filterSet.add(new String(new int[]{code}, 0, 1));
        }

        static {
            filterSet = new HashSet<String>();

            // See http://apps.timwhitlock.info/emoji/tables/unicode

            // 1. Emoticons ( 1F601 - 1F64F )
            addUnicodeRangeToSet(filterSet, 0x1F601, 0X1F64F);

            // 2. Dingbats ( 2702 - 27B0 )
            addUnicodeRangeToSet(filterSet, 0x2702, 0X27B0);

            // 3. Transport and map symbols ( 1F680 - 1F6C0 )
            addUnicodeRangeToSet(filterSet, 0X1F680, 0X1F6C0);

            // 4. Enclosed characters ( 24C2 - 1F251 )
            addUnicodeRangeToSet(filterSet, 0X24C2);
            addUnicodeRangeToSet(filterSet, 0X1F170, 0X1F251);

            // 6a. Additional emoticons ( 1F600 - 1F636 )
            addUnicodeRangeToSet(filterSet, 0X1F600, 0X1F636);

            // 6b. Additional transport and map symbols ( 1F681 - 1F6C5 )
            addUnicodeRangeToSet(filterSet, 0X1F681, 0X1F6C5);

            // 6c. Other additional symbols ( 1F30D - 1F567 )
            addUnicodeRangeToSet(filterSet, 0X1F30D, 0X1F567);

            // 5. Uncategorized
            addUnicodeRangeToSet(filterSet, 0X1F004);
            addUnicodeRangeToSet(filterSet, 0X1F0CF);
            // 与6c. Other additional symbols ( 1F30D - 1F567 )重复
            // 去掉重复部分虽然不去掉HashSet也不会重复，原范围（0X1F300 - 0X1F5FF）
            addUnicodeRangeToSet(filterSet, 0X1F300, 0X1F30D);
            addUnicodeRangeToSet(filterSet, 0X1F5FB, 0X1F5FF);
            addUnicodeRangeToSet(filterSet, 0X00A9);
            addUnicodeRangeToSet(filterSet, 0X00AE);
            addUnicodeRangeToSet(filterSet, 0X0023);
            //阿拉伯数字0-9，配合0X20E3使用
            //addUnicodeRangeToSet(filterSet, 0X0030, 0X0039);
            // 过滤掉203C开始后的2XXX 段落
            //addUnicodeRangeToSet(filterSet, 0X203C, 0X24C2);
            addUnicodeRangeToSet(filterSet, 0X203C);
            addUnicodeRangeToSet(filterSet, 0X2049);
            //严格验证的话需要判断前面是否是数字
            //Android上显示和数字分开可以不判断
            addUnicodeRangeToSet(filterSet, 0X20E3);
            addUnicodeRangeToSet(filterSet, 0X2122);
            addUnicodeRangeToSet(filterSet, 0X2139);
            addUnicodeRangeToSet(filterSet, 0X2194, 0X2199);
            addUnicodeRangeToSet(filterSet, 0X21A9, 0X21AA);
            addUnicodeRangeToSet(filterSet, 0X231A, 0X231B);
            addUnicodeRangeToSet(filterSet, 0X23E9, 0X23EC);
            addUnicodeRangeToSet(filterSet, 0X23F0);
            addUnicodeRangeToSet(filterSet, 0X23F3);
            addUnicodeRangeToSet(filterSet, 0X25AA, 0X25AB);
            addUnicodeRangeToSet(filterSet, 0X25FB, 0X25FE);
            //TODO： 26XX 太杂全部过滤
            addUnicodeRangeToSet(filterSet, 0X2600, 0X26FE);
            addUnicodeRangeToSet(filterSet, 0X2934, 0X2935);
            addUnicodeRangeToSet(filterSet, 0X2B05, 0X2B07);
            addUnicodeRangeToSet(filterSet, 0X2B1B, 0X2B1C);
            addUnicodeRangeToSet(filterSet, 0X2B50);
            addUnicodeRangeToSet(filterSet, 0X2B55);
            addUnicodeRangeToSet(filterSet, 0X3030);
            addUnicodeRangeToSet(filterSet, 0X303D);
            addUnicodeRangeToSet(filterSet, 0X3297);
            addUnicodeRangeToSet(filterSet, 0X3299);
        }

        public EmojiFilter() {
            super();
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                   int dend) {
            // check black-list set
            if (filterSet.contains(source.toString())) {
                MMToast.showShort("暂时不支持表情输入");
                return "";
            }
            return source;
        }
    }
}
