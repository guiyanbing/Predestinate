package com.juxin.predestinate.ui.user.check.edit.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;

/**
 * 修改昵称弹框
 */
public class NickEditDialog extends SimpleTipDialog implements android.view.View.OnClickListener {
    private Context context;
    private EditText editText;
    private String defaultVlaue;
    private OnNickEditSelecterListener onSelecterListener;

    public NickEditDialog(FragmentActivity context, String defaultVlaue) {
        super(context);
        this.context = context;
        this.defaultVlaue = defaultVlaue;
    }

    @NonNull
    @Override
    protected View makeCenterView() {

//		View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
//		editText = (EditText) view.findViewById(R.id.edit);
//		if (defaultVlaue != null) {
//			editText.setText(defaultVlaue);
//		}
//		editText.setSelection(editText.getText().length());
//		return view;
        return null;
    }

    @Override
    public void onClick(View v) {
        String result = editText.getText().toString();
        if (TextUtils.isEmpty(result)) {
            PToast.showShort("请输入你要修改的昵称");
            return;
        }
        if (result.length() > 7) {
            PToast.showShort("最多只能输入7个字符");
            return;
        }
        if (onSelecterListener != null) {
            onSelecterListener.selected(result);
        }
        dismiss();
    }

    public interface OnNickEditSelecterListener {
        public void selected(String value);
    }

    public void setOnSelecterListener(OnNickEditSelecterListener onSelecterListener) {
        this.onSelecterListener = onSelecterListener;
    }
}
