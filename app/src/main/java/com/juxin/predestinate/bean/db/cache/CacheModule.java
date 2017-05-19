package com.juxin.predestinate.bean.db.cache;

import android.app.Application;
import com.juxin.library.observe.RxBus;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kind on 2017/5/18.
 */
@Module(includes = {DBCache.class})
public class CacheModule {
    private final Application mApplication;
    public CacheModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return RxBus.getInstance();
    }
}