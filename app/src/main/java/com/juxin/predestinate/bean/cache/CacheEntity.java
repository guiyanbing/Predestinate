package com.juxin.predestinate.bean.cache;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 本地实现缓存的数据库表，以K-V的方式实现缓存，V尽量以json格式进行存储
 * Created by ZRP on 2017/3/9.
 */
@Entity
public class CacheEntity {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String key;
    private String value;

    @Generated(hash = 1506971430)
    public CacheEntity(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public CacheEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Generated(hash = 1391258017)
    public CacheEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
