package com.juxin.predestinate.ui.wode.bean;


import android.text.TextUtils;

import com.juxin.mumu.bean.net.BaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息列表
 * Created by zm on 17/3/20.
 */
public class AttentionUserDetailList extends BaseData {

    private List attentionUserDetailList = new ArrayList();

    @Override
    public void parseJson(String s) {
        if (!TextUtils.isEmpty(s))
            attentionUserDetailList = getBaseDataList(getJsonArray(s),AttentionUserDetail.class);
    }

    public List getAttentionUserDetailList() {
        return attentionUserDetailList;
    }
}
