package com.juxin.predestinate.bean.db.cache;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.juxin.predestinate.bean.db.DBCenter;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.BuildConfig;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kind on 2017/4/7.
 */
@Module
public class DBCache {

    @Provides
    @Singleton
    SQLiteOpenHelper provideOpenHelper(Application application) {
        return new DBCacheHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        final SqlBrite.Builder builder = new SqlBrite.Builder();
        if (BuildConfig.DEBUG) {
            //noinspection CheckResult
            builder.logger(new SqlBrite.Logger() {
                @Override
                public void log(String message) {
                    if (!TextUtils.isEmpty(message)) {
                        //XLog.d(message);
                    }
                }
            });
        }
        return builder.build();
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, rx.schedulers.Schedulers.io());
        db.setLoggingEnabled(BuildConfig.DEBUG);
        return db;
    }

    @Provides
    @Singleton
    DBCenter provideConsumerDao(BriteDatabase briteDatabase) {
        return new DBCenter(briteDatabase);
    }
}
