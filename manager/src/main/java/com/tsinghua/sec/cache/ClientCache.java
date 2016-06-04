package com.tsinghua.sec.cache;

import com.tsinghua.sec.util.cache.ExpireCache;
import com.tsinghua.sec.util.cache.ExpireCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 线上节点缓存
 * Created by ji on 16-5-23.
 */
public class ClientCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCache.class);

    private static final ClientCache _INSTANCE = new ClientCache();


    private static final ExpireCache<String, String> onlineCache = ExpireCache.setExpireTime(20, 20, TimeUnit.DAYS, false)
            .build(new ExpireCallBack() {
                @Override
                public Object handler(Object key, boolean isEnd) throws Exception {
                    LOGGER.warn("[{}] 下线", key);
                    LogMessageCache.getInstance().writeMsg("[" + key + "] 下线");
                    SolderCache.getInstance().clearSolderAuth((String) key);
                    return null;
                }
            });

    private ClientCache() {
    }

    public static ClientCache getInstance() {
        return _INSTANCE;
    }

    public Map<String,String> getOnlineClient() {
        return onlineCache.getMap();
    }

    public void addOnlineClient(String k, String v) {
        boolean contains = onlineCache.put(k, v);
        if (!contains) {
            LogMessageCache.getInstance().writeMsg("[" + k + "] 上线");
        }
    }
}
