package com.juxin.predestinate.ui.user.paygoods.bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 支付商品信息
 * <p>
 * Created by Su on 2016/9/18.
 */
public class PayGoods extends BaseData implements Parcelable {
    private PayGood payGood;      // 单个商品详细信息
    private ArrayList<PayGood> payGoodList; // 商品列表

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);

        //商品详细信息
        if (!jsonObject.isNull("detail")) {
            payGood = new PayGood(jsonObject.optString("detail"));
        }

        // 商品列表
        if (!jsonObject.isNull("list")) {
            payGoodList = new ArrayList<>();
            this.payGoodList = (ArrayList<PayGood>) getBaseDataList(jsonObject.optJSONArray("list"), PayGood.class);
        }
    }

    public PayGood getCommodityInfo() {
        return payGood;
    }

    public ArrayList<PayGood> getCommodityList() {
        return payGoodList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.payGood, flags);
        dest.writeTypedList(this.payGoodList);
    }

    public PayGoods() {
    }

    protected PayGoods(Parcel in) {
        this.payGood = in.readParcelable(PayGood.class.getClassLoader());
        this.payGoodList = in.createTypedArrayList(PayGood.CREATOR);
    }

    public static final Creator<PayGoods> CREATOR = new Creator<PayGoods>() {
        @Override
        public PayGoods createFromParcel(Parcel source) {
            return new PayGoods(source);
        }

        @Override
        public PayGoods[] newArray(int size) {
            return new PayGoods[size];
        }
    };
}
