package com.juxin.predestinate.ui.wode.bean;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 标签列表
 * Created by zm on 17/3/20.
 */
public class WithdrawAddressInfo extends BaseData {

    private String accountname;
    private String accountnum;
    private String bank;
    private String subbank;
    private String status;

    public boolean isOk() {
        if ("ok".equalsIgnoreCase(status))
            return true;
        else
            return false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s).optJSONObject("list");
        this.setAccountname(jsonObject.optString("accountname"));
        this.setAccountnum(jsonObject.optString("accountnum"));
        this.setBank(jsonObject.optString("bank"));
        this.setSubbank(jsonObject.optString("subbank"));
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getSubbank() {
        return subbank;
    }

    public void setSubbank(String subbank) {
        this.subbank = subbank;
    }

    @Override
    public String toString() {
        return "RankList{" +
                //                    "uid=" + uid +
                //                    ", avatar=" + avatar +
                //                    ", nickname=" + nickname +
                //                    ", gender=" + gender +
                //                    ", score=" + score +
                //                    ", exp=" + exp +
                '}';
    }
}
