package com.juxin.predestinate.module.logic.cache;

import android.text.TextUtils;

import com.juxin.predestinate.bean.cache.CacheEntity;
import com.juxin.predestinate.bean.db.CacheEntityDao;
import com.juxin.predestinate.module.logic.db.DBManager;

/**
 * 全局缓存管理类
 * Created by ZRP on 2017/3/9.
 */
public class PCache {

    private CacheEntityDao cacheEntityDao;

    private PCache() {
        cacheEntityDao = DBManager.getInstance().getSession().getCacheEntityDao();
    }

    private static class SingletonHolder {
        public static PCache instance = new PCache();
    }

    public static PCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 将K-V添加到数据库，如果K已经存在，就更新V
     */
    public void cacheString(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) return;
        try {
            CacheEntity entity = cacheEntityDao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).build().unique();
            if (entity == null) {
                cacheEntityDao.insertOrReplace(new CacheEntity(key, value));
            } else {
                entity.setValue(value);
                cacheEntityDao.update(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 根据K获取V
     */
    public String getCache(String key) {
        CacheEntity unique = null;
        try {
            unique = cacheEntityDao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unique == null ? null : unique.getValue();
    }

    /**
     * 删除指定key的缓存
     */
    public void deleteCache(String key) {
        if (TextUtils.isEmpty(key)) return;
        try {
            CacheEntity entity = cacheEntityDao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).unique();
            if (entity != null) cacheEntityDao.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除本地所有数据库缓存，慎重使用
     */
    public void clear() {
        cacheEntityDao.deleteAll();
    }
}
