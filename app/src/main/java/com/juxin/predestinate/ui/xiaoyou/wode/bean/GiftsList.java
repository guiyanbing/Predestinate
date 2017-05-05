package com.juxin.predestinate.ui.xiaoyou.wode.bean;


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
    private List arrlineGifts;

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

    public List getArrlineGifts() {
        List gifts = new ArrayList();
        if (arrlineGifts != null)
            gifts.addAll(arrlineGifts);
        return gifts;
    }

    public GiftInfo getGiftInfo(int id){
        if (arrCommonGifts != null){
            for (int i = 0 ;i< arrCommonGifts.size();i++){
                GiftInfo gift = (GiftInfo) arrCallGifts.get(i);
                if (gift.getId() == id) return gift;
            }
        }
        return null;
    }

    public void setArrCommonGifts(List arrCommonGifts) {
        this.arrCommonGifts = arrCommonGifts;
    }

    public void setArrCallGifts(List arrCallGifts) {
        this.arrCallGifts = arrCallGifts;
    }

    public void setArrlineGifts(List arrlineGifts) {
        this.arrlineGifts = arrlineGifts;
    }

    @Override
    public void parseJson(String s) {
        arrCommonGifts = getBaseDataList(getJsonObject(s).optJSONArray("list"), GiftInfo.class);
        arrCallGifts = getBaseDataList(getJsonObject(s).optJSONArray("calllist"), GiftInfo.class);
        arrlineGifts = getBaseDataList(getJsonObject(s).optJSONArray("linelist"), GiftInfo.class);
    }

    public static class GiftInfo extends BaseData {

        private boolean isSelect = false;
        private String pic = "";
        private String name = "";
        private int money = 0;
        private int id = 0;
        private String gif = "";

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
}
