package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.juxin.library.log.PToast;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.discover.DefriendType;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.util.ArrayList;
import java.util.List;

/**
 * 举报界面
 * Created by zhang on 2017/4/14.
 */

public class DefriendAct extends BaseActivity implements DefriendAdapter.OnSelect, RequestComplete {

    private ListView listView;
    private EditText editText;

    private List<DefriendType> defriendTypes = new ArrayList<>();

    private long tuid; //被举报人uid
    private long dynamicId; //被举报的动态id

    private DefriendAdapter adapter;

    private String toTypeName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_defriend_act);
        tuid = getIntent().getLongExtra("tuid", 0);
        dynamicId = getIntent().getLongExtra("dynamicId", 0);
        initTitle();
        getDefriendTypeData();

        initView();
    }

    /**
     * 初始化页面标题栏
     */
    private void initTitle() {
        setBackView(R.id.base_title_back);
        setTitle(getResources().getString(R.string.defriend_title));
        setTitleRight(getResources().getString(R.string.defriend_title_right), R.color.text_theme_blue, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(toTypeName)) {
                    PToast.showCenterShort("请选择举报类型");
                } else {
                    String detail = TextUtils.isEmpty(editText.getText().toString()) ? "" : editText.getText().toString();
                    ModuleMgr.getCommonMgr().complainBlack(tuid, toTypeName, detail, DefriendAct.this);
                }
            }
        });
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.defriend_list);
        editText = (EditText) findViewById(R.id.defriend_edt);
        adapter = new DefriendAdapter(this, defriendTypes, this);
        listView.setAdapter(adapter);
    }


    /**
     * 初始化数据 （测试数据）
     */
    private void getDefriendTypeData() {
        String[] defrienStrs = getResources().getStringArray(R.array.defriend_type_name);
        int[] defriendNums = getResources().getIntArray(R.array.defriend_type_num);
        for (int i = 0; i < defrienStrs.length; i++) {
            DefriendType defriendType = new DefriendType();
            defriendType.setCheck(false);
            defriendType.setStr_typeName(defrienStrs[i]);
            defriendType.setInt_typeNum(defriendNums[i]);
            defriendTypes.add(defriendType);
        }
    }

    @Override
    public void onSelectChange(DefriendType defriendType) {
        if (defriendType.isCheck()) {
            toTypeName = defriendType.getStr_typeName();
            MMToast.showShort("选择了 == " + defriendType.getStr_typeName());
        } else {
            toTypeName = "";
            MMToast.showShort("取消了 == " + defriendType.getStr_typeName());
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            MMToast.showShort("举报成功");
        }
    }
}