package com.tsinghua.sec.cache;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-6-4.
 */
public class MillionaireReqCache {

    private static final MillionaireReqCache _INSTANCE = new MillionaireReqCache();

    private Map<String, Set<JSONObject>> requestCache = new ConcurrentHashMap<String, Set<JSONObject>>();

    private MillionaireReqCache() {
    }

    public static MillionaireReqCache getInstance() {
        return _INSTANCE;
    }

    public void addRequest(String receiver, JSONObject message) {
        synchronized (requestCache) {
            Set<JSONObject> requests = requestCache.get(receiver);
            if (requests == null) {
                requests = new HashSet<JSONObject>();
            }
            requests.add(message);
            requestCache.put(receiver, requests);
        }
    }

    public List<JSONObject> getRequests(String name) {
        List<JSONObject> result = new ArrayList<JSONObject>();
        synchronized (requestCache) {
            Set<JSONObject> set = requestCache.get(name);
            if (set != null) {
                result = new ArrayList<JSONObject>(set);
                set.clear();
            }
        }
        return result;
    }
}
