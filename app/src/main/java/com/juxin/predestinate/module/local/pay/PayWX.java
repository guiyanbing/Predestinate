package com.juxin.predestinate.module.local.pay;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * Created by Kind on 2017/4/25.
 */

public class PayWX extends BaseData {

    private boolean isOK = false;//是否正常


    private String payID;
    private String payContent;
    private String param;
    private int cupPayType;


    //银联语音
    private String result;
    private String content;
    private String realName;
    private String mobile;
    private String pan;
    private String idcard;

    //微信支付
    private int payType;//payType 1为自有炬鑫支付，2为weixin协议三方支付, 3为webview弹微信支付 4威富通支付
    private String payData;//如果为三方支付，直接为微信协议支付：weixin://
    private String prepay_id;
    private String mch_id;
    private String app_id;

    @Override
    public void parseJson(String jsonStr) {

    }

    public PayWX() {
        super();
    }

    public PayWX(String jsonStr, int cup) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        String result = jsonObject.optString("result");
        if ("1".equals(result)) {
            this.setOK(true);
            this.setResult(result);
            this.setPayID(jsonObject.optString("content"));
            this.setParam(jsonObject.optString("param"));
            this.setCupPayType(jsonObject.optInt("payType"));
        }
    }
    //微信
    public PayWX(String jsonStr) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        if (!jsonObject.isNull("payType")) {
            this.setOK(true);
            this.setPayType(jsonObject.optInt("payType"));
            this.setPayData(jsonObject.isNull("payData") ? null : jsonObject.optString("payData"));

            switch (getPayType()) {
                case 1:
                    if (!jsonObject.isNull("payData")) {
                        JSONObject jsoC =  getJsonObject(jsonObject.optString("payData"));
                        if (("SUCCESS".equals(jsoC.optString("return_code"))) &&
                                ("SUCCESS".equals(jsoC.optString("result_code")))) {
                            this.setPrepay_id(jsoC.optString("prepay_id"));
                            this.setMch_id(jsoC.optString("mch_id"));
                            this.setApp_id(jsoC.optString("appid"));
                        }
                    }
                    break;
            }
        }
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayData() {
        return payData;
    }

    public void setPayData(String payData) {
        this.payData = payData;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }


    public String getPayID() {
        return payID;
    }

    public void setPayID(String payID) {
        this.payID = payID;
    }

    public String getPayContent() {
        return payContent;
    }

    public void setPayContent(String payContent) {
        this.payContent = payContent;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getCupPayType() {
        return cupPayType;
    }

    public void setCupPayType(int cupPayType) {
        this.cupPayType = cupPayType;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}