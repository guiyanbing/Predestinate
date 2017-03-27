package com.juxin.predestinate.bean.center.user.others;

import android.os.Parcel;
import android.os.Parcelable;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户礼物信息
 * Created by Su on 2017/3/27.
 */

public class UserPrize extends BaseData implements Parcelable{
    private int total;
    private List<Prize> prizeList = new ArrayList<>();

    @Override
    public void parseJson(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        this.setTotal(jsonObject.optInt("total"));

        if (!jsonObject.isNull("list")) {
            this.prizeList = (List<Prize>) getBaseDataList(jsonObject.optJSONArray("list"), Prize.class);
        }

    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Prize> getPrizeList() {
        return prizeList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeTypedList(this.prizeList);
    }

    public UserPrize() {
    }

    protected UserPrize(Parcel in) {
        this.total = in.readInt();
        this.prizeList = in.createTypedArrayList(Prize.CREATOR);
    }

    public static final Creator<UserPrize> CREATOR = new Creator<UserPrize>() {
        @Override
        public UserPrize createFromParcel(Parcel source) {
            return new UserPrize(source);
        }

        @Override
        public UserPrize[] newArray(int size) {
            return new UserPrize[size];
        }
    };
}
