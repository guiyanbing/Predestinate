package com.juxin.predestinate.bean.db.cache;

import com.juxin.predestinate.bean.db.AppComponent;
import com.juxin.predestinate.module.local.chat.ChatMgr;
import javax.inject.Singleton;
import dagger.Component;

/**
 * 配表
 * Created by Kind on 2017/3/29.
 */

@Component( modules = { CacheModule.class })
public interface CacheComponent {

   // void inject(ChatMgr chatMgr);
}