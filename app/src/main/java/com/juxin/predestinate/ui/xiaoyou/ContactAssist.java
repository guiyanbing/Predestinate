package com.juxin.predestinate.ui.xiaoyou;

import com.juxin.predestinate.third.pinyin.Pinyin;
import com.juxin.predestinate.ui.xiaoyou.bean.BaseFriendInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zm on 2017/3/29
 */
public class ContactAssist {

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 筛选关键字
     * @param sortFriends 需要排序的集合
     */
    private List<BaseFriendInfo> filterData(String filterStr,List<BaseFriendInfo> sortFriends) {
        List<BaseFriendInfo> filterDateList = new ArrayList<BaseFriendInfo>();

        for (int i = 0;i < sortFriends.size();i++){
            String contactSort = "";
            // 汉字转换成拼音
            String pinyin = Pinyin.toPinyin(sortFriends.get(i).getNickname().toCharArray()[0]);
//            Log.d("pinyin_phone", "name = " + contactName.toCharArray()[0] + "    pinyin = " + pinyin);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                contactSort = sortString.toUpperCase();
            } else {
                contactSort = "#";
            }
            sortFriends.get(i).setSortKey(contactSort);
        }

        // 根据a-z进行排序
        Comparator comp = new Mycomparator();
        Collections.sort(filterDateList, comp);
        return filterDateList;
    }
//    private String getNamePinyin(String name) {
//        StringBuilder namePinyin = new StringBuilder();
//        char[] nameArray = name.toCharArray();
//        for (int i = 0; i < nameArray.length; i++) {
//            namePinyin.append(Pinyin.toPinyin(nameArray[i]));
//        }
//        return namePinyin.toString();
//    }
    // 通讯社按中文拼音排序
    public class Mycomparator implements Comparator<BaseFriendInfo> {
        public int compare(BaseFriendInfo lhs, BaseFriendInfo rhs) {
            Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);
            return cmp.compare(lhs.getSortKey(), rhs.getSortKey());
        }
    }

}
