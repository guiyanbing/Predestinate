package com.juxin.predestinate.bean.db.cache;

import com.juxin.predestinate.ui.mail.MailFragment;
import javax.inject.Singleton;
import dagger.Component;

/**
 * 配表
 * Created by Kind on 2017/3/29.
 */
@Singleton
@Component(modules = { CacheModule.class })
public interface CacheComponent {

    void inject(MailFragment mailFragment);
}
