package com.juxin.predestinate.bean.my;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物列表
 * Created by zm on 17/3/20.
 */
public class GiftsList extends BaseData {

    private List arrCommonGifts;
    private List arrCallGifts;
    private List arrLineGifts;
    private String strGiftConfig = "";

    public List getArrCommonGifts() {
        List gifts = new ArrayList();
        if (arrCommonGifts != null)
            gifts.addAll(arrCommonGifts);
        return gifts;
    }

    public List getArrCallGifts() {
        List gifts = new ArrayList();
        if (arrCallGifts != null)
            gifts.addAll(arrCallGifts);
        return gifts;
    }

    public List getArrLineGifts() {
        List gifts = new ArrayList();
        if (arrLineGifts != null)
            gifts.addAll(arrLineGifts);
        return gifts;
    }

    public GiftInfo getGiftInfo(int id) {
        if (arrCommonGifts != null) {
            for (int i = 0; i < arrCommonGifts.size(); i++) {
                GiftInfo gift = (GiftInfo) arrCommonGifts.get(i);
                if (gift.getId() == id) return gift;
            }
        }
        return null;
    }

    public String getStrGiftConfig() {
        return strGiftConfig;
    }

    public void setStrGiftConfig(String strGiftConfig) {
        this.strGiftConfig = strGiftConfig;
    }

    @Override
    public void parseJson(String s) {
        strGiftConfig = s;
        arrCommonGifts = getBaseDataList(getJsonObject(s).optJSONArray("list"), GiftInfo.class);
        arrCallGifts = getBaseDataList(getJsonObject(s).optJSONArray("calllist"), GiftInfo.class);
        arrLineGifts = getBaseDataList(getJsonObject(s).optJSONArray("linelist"), GiftInfo.class);
    }

    public static class GiftInfo extends BaseData {

        private boolean isSelect = false;
        private String pic = "";
        private String name = "";
        private int money = 0;
        private int id = 0;
        private String gif = "";
        private boolean isShow;

        @Override
        public void parseJson(String s) {
            JSONObject jsonObject = getJsonObject(s);
            //json串解析
            this.setId(jsonObject.optInt("id"));
            this.setName(jsonObject.optString("name"));
            this.setGif(jsonObject.optString("packet"));
            this.setMoney(jsonObject.optInt("cost"));
            this.setPic(jsonObject.optString("img"));
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGif() {
            return gif;
        }

        public void setGif(String gif) {
            this.gif = gif;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setIsShow(boolean isShow) {
            this.isShow = isShow;
        }

        @Override
        public String toString() {
            return "GiftInfo{" +
                    "isSelect=" + isSelect +
                    ", pic='" + pic + '\'' +
                    ", name='" + name + '\'' +
                    ", money=" + money +
                    ", id=" + id +
                    ", gif='" + gif + '\'' +
                    ", isShow=" + isShow +
                    '}';
        }
    }
}
