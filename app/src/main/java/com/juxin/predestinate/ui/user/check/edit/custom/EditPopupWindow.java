package com.juxin.predestinate.ui.user.check.edit.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.user.edit.EditKey;

import java.util.HashMap;

/**
 * 点击直接进行修改的popupWindow，目前仅在个人中心进行使用
 */
public class EditPopupWindow extends PopupWindow implements OnDismissListener {
    private Context context;
    private View mMenuView;

    private LinearLayout layout_parent;
    private EditText user_edit;
    private TextView txt;
    private UserDetail userDetail;

    private String editKey;  // 修改字段

    /**
     * @param editKey 需编辑修改的字段
     * @param txt     展示TextView
     */
    public EditPopupWindow(Context context, String editKey, TextView txt) {
        super(context);
        this.context = context;
        this.editKey = editKey;
        this.txt = txt;
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();

        initView();
        initEvent();
        setPopupWindowStyle();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.p1_edit_pop_window, null);
        layout_parent = (LinearLayout) mMenuView.findViewById(R.id.layout_parent);
        user_edit = (EditText) mMenuView.findViewById(R.id.user_edit);

        fillEditText();
    }

    private void fillEditText() {
        String data = "";
        if (editKey.equals(EditKey.s_key_age)) {
            user_edit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            data = String.valueOf(userDetail.getAge());
        }

        if (editKey.equals(EditKey.s_key_nickName)) {
            data = userDetail.getNickname();
        }
        user_edit.setText(data);
        user_edit.setSelection(data.length());
    }

    private void initEvent() {
        layout_parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setOnDismissListener(this);
    }

    private void setPopupWindowStyle() {
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        this.setAnimationStyle(R.style.AnimLeft);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 显示该popupWindow
     */
    public void showPopupWindow() {
        txt.setVisibility(View.INVISIBLE);
        int offSet = UIUtil.dp2px(35);
        this.showAsDropDown(txt, 0, -offSet);
    }

    @Override
    public void onDismiss() {
        final String editText = user_edit.getText().toString();

        // 修改年龄
        if (editKey.equals(EditKey.s_key_age)) {
            int result = Integer.parseInt(editText);

            if (result < 18) result = 18;
            if (result > 99) result = 99;

            if (result != userDetail.getAge()) {
                HashMap<String, Object> postParams = new HashMap<>();
                postParams.put(EditKey.s_key_age, result);
                ModuleMgr.getCenterMgr().updateMyInfo(postParams, null);
            }
        }

        // 修改昵称
        if (editKey.equals(EditKey.s_key_nickName)) {
            if (!editText.equals(userDetail.getNickname()) && !TextUtils.isEmpty(editText)) {
                HashMap<String, Object> postParams = new HashMap<>();
                postParams.put(EditKey.s_key_nickName, editText);
                ModuleMgr.getCenterMgr().updateMyInfo(postParams, null);
            }
        }
        txt.setVisibility(View.VISIBLE);
    }
}
