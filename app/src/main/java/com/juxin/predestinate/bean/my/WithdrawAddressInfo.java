package com.juxin.predestinate.bean.my;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 标签列表
 * Created by zm on 17/3/20.
 */
public class WithdrawAddressInfo extends BaseData {

//    "status": "ok",
//            "list": {
//        "accountname": "你好",
//                "accountnum": "2147483647345353453",    //支付宝账号/银行账号
//                "bank": "中国银行",                    //为"支付宝"时 类型为支付宝
//                "subbank": "长沙支行"
//    }

    private String accountname;
    private String accountnum;
    private String bank;
    private String subbank;
    private String status;
    private int paytype;

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
        if (jsonObject != null){
            this.setAccountname(jsonObject.optString("accountname"));
            this.setAccountnum(jsonObject.optString("accountnum"));
            this.setBank(jsonObject.optString("bank"));
            this.setSubbank(jsonObject.optString("subbank"));
        }
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname != null?accountname:"";
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum != null?accountnum:"";
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank != null?bank:"";
    }

    public String getSubbank() {
        return subbank;
    }

    public void setSubbank(String subbank) {
        this.subbank = subbank != null?subbank:"";
    }

    public int getPaytype() {
        if ("支付宝".equalsIgnoreCase(bank))
            paytype = 2;
        else
            paytype =1;
        return paytype;
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
