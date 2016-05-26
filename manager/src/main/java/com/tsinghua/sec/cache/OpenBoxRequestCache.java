package com.tsinghua.sec.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-5-23.
 */
public class OpenBoxRequestCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenBoxRequestCache.class);

    private static final Map<String, Integer> cache = new ConcurrentHashMap<String, Integer>();

    private static final OpenBoxRequestCache _INSTANCE = new OpenBoxRequestCache();

    private OpenBoxRequestCache(){}

    public static OpenBoxRequestCache getInstance() {
        return _INSTANCE;
    }

    public void add(String name) {
        cache.put(name, 0);
    }
}
