package com.juxin.predestinate.ui.user.information;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIUtil;

/**
 * 编辑内容 通用界面
 */
public class EditContentAct extends BaseActivity implements View.OnClickListener {

    private EditText editText;
    private String defaultValue; // 初始value
    private boolean isSaved; // 是否保存了更改信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_edit_content_act);
        setBackView();
        setTitleRight("保存", this);

        initData();
    }

    private void initTitle(String defaultValue) {
//        setTitle(getResources().getString(R.string.user_info_edit_name));
        ModuleMgr.getCenterMgr().inputFilterSpace(editText);
        editText.setHint(getResources().getString(R.string.user_info_put_name));
        if (!TextUtils.isEmpty(defaultValue)) {
            editText.setText(defaultValue);
            UIUtil.endCursor(editText);
        }
    }

    private void initData() {
        defaultValue = getIntent().getStringExtra("defaultValue");
        editText = (EditText) findViewById(R.id.edit_content_text);
        initTitle(defaultValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSaved = false;
    }

    @Override
    public void onClick(View v) {
        if (test()) {
            isSaved = true;
            saveAndFinish();
        }
    }

    /**
     * 检测输入数据是否合法
     */
    private boolean test() {
        String contact = editText.getText().toString().trim();
        if (TextUtils.isEmpty(contact)) {
            return false;
        }

        if (contact.length() < 2) {
            MMToast.showShort("昵称至少2个字符");
            return false;
        }
        return true;
    }

    /**
     * 保存修改信息值，并关闭当前页面
     */
    private void saveAndFinish() {
        String contact = editText.getText().toString().trim();
        if (isSaved) {
            if (!contact.equals(defaultValue)) {
                Intent intent = new Intent();
                intent.putExtra("newValue", contact);
                setResult(UserInfoAct.UPDATE_NICK_NAME, intent);
            }
            finish();
        }
    }
}
