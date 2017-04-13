package com.juxin.predestinate.ui.user.check.edit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private EditText nick_edit;
    private TextView txt_nickname;
    private UserDetail userDetail;

    public EditPopupWindow(Context context, TextView txt_nickname) {
        super(context);
        this.context = context;
        this.txt_nickname = txt_nickname;
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();

        initView();
        initEvent();
        setPopupWindowStyle();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.p1_edit_pop_window, null);
        layout_parent = (LinearLayout) mMenuView.findViewById(R.id.layout_parent);
        nick_edit = (EditText) mMenuView.findViewById(R.id.edtTxt_user_nickname);
        nick_edit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        nick_edit.setText(String.valueOf(userDetail.getAge()));
        nick_edit.setSelection(String.valueOf(userDetail.getAge()).length());
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
     *
     * @param v parentView
     */
    public void showPopupWindow(View v) {
        txt_nickname.setVisibility(View.INVISIBLE);
        int offSet = UIUtil.dp2px(35);
        this.showAsDropDown(v, 0, -offSet);
    }

    @Override
    public void onDismiss() {
        int result = Integer.parseInt(nick_edit.getText().toString());

        if (result < 18) result = 18;
        if (result > 99) result = 99;

        if (result != userDetail.getAge()) {
            HashMap<String, Object> postParams = new HashMap<>();
            postParams.put(EditKey.s_key_age, result);
            // 向服务器提交数据
            ModuleMgr.getCenterMgr().updateMyInfo(postParams);
            // 更新本地的数据
            ModuleMgr.getCenterMgr().getMyInfo().setAge(result);
            txt_nickname.setText(result + "岁");
        }
        txt_nickname.setVisibility(View.VISIBLE);
    }
}
