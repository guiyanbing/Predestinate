package com.juxin.predestinate.bean.config;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 支付方式控制列表
 * Created by ZRP on 2017/5/18.
 */
public class PayTypeList extends BaseData {

    private List<PayType> payTypes;

    @Override
    public void parseJson(String jsonStr) {
        this.payTypes = (List<PayType>) getBaseDataList(getJsonArray(jsonStr), PayType.class);
        Collections.sort(payTypes, new Comparator<PayType>() {
            @Override
            public int compare(PayType arg0, PayType arg1) {
                // 服务器不会返回相等的值，故直接进行比对
                return Integer.valueOf(arg0.getListOrder()).compareTo(arg1.getListOrder());
            }
        });
    }

    /**
     * @return 获取配置的支付列表项，已进行排序
     */
    public List<PayType> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<PayType> payTypes) {
        this.payTypes = payTypes;
    }

    @Override
    public String toString() {
        return "PayTypeList{" +
                "payTypes=" + payTypes +
                '}';
    }

    /**
     * 支付方式服务器控制
     */
    public static class PayType extends BaseData {

        private String name;    //支付类型
        private int listOrder;  //排序
        private int status;     //状态，1：开启，0：关闭

        @Override
        public void parseJson(String jsonStr) {
            JSONObject jsonObject = getJsonObject(jsonStr);
            this.setName(jsonObject.optString("name"));
            this.setListOrder(jsonObject.optInt("listorder"));
            this.setStatus(jsonObject.optInt("status"));
        }

        /**
         * @return 该支付类型是否可用
         */
        public boolean isEnable() {
            return status == 1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getListOrder() {
            return listOrder;
        }

        public void setListOrder(int listOrder) {
            this.listOrder = listOrder;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "PayType{" +
                    "name='" + name + '\'' +
                    ", listOrder=" + listOrder +
                    ", status=" + status +
                    '}';
        }
    }
}
