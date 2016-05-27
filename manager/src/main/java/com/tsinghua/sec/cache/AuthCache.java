package com.tsinghua.sec.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证信息缓存
 * Created by ji on 16-5-26.
 */
public class AuthCache {
    //存储每个士兵的认证信息,每个士兵一条,key:士兵,value:已认证的列表
    private final Map<String, Set<String>> authCache = new HashMap<String, Set<String>>();

    private static final AuthCache _INSTANCE = new AuthCache();

    private AuthCache() {
    }

    public static AuthCache getInstance() {
        return _INSTANCE;
    }

    public Map<String,Set<String>> getAuthCache() {
        return authCache;
    }

}
