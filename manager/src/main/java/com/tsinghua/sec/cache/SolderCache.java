package com.tsinghua.sec.cache;

import com.tsinghua.sec.domain.Solder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 士兵缓存
 * Created by ji on 16-5-26.
 */
public class SolderCache {

    private final Map<String, Solder> solderCache = new HashMap<String, Solder>();

    //solder1_solder2, value:0,1:认证
    private final Map<String, String> authCache = new ConcurrentHashMap<String, String>();

    private static final SolderCache _INSTANCE = new SolderCache();

    private SolderCache() {
    }

    public static SolderCache getInstance() {
        return _INSTANCE;
    }

    public void addSolder(String name, Solder solder) {
        solderCache.put(name, solder);
    }

    public List<Solder> getSolders() {
        return new ArrayList<>(solderCache.values());
    }

    public Solder getSolder(String name) {
        return solderCache.get(name);
    }

    public void initAuth() {
        Set<String> names = solderCache.keySet();
        for (String name : names) {
            for (String tmpName : names) {
                if (!name.equals(tmpName)) {
                    authCache.put(name + "_" + tmpName, "0");
                }
            }
        }
    }

    public void clearSolderAuth(String solder) {
        synchronized (authCache) {
            for (Map.Entry<String, String> entry : authCache.entrySet()) {
                if (entry.getKey().startsWith(solder + "_")||entry.getKey().endsWith("_" + solder)) {
                    authCache.put(entry.getKey(), "0");
                }
            }
        }
    }

    public void setAuth(String name1, String name2) {
        synchronized (authCache) {
            String key = name1 + "_" + name2;
            if (authCache.containsKey(key)) {
                authCache.put(key, "1");
            }
        }
    }

    public Map<String,String> getAuthCache() {
        return authCache;
    }
}
