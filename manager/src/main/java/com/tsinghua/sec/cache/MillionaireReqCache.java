package com.tsinghua.sec.cache;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-6-4.
 */
public class MillionaireReqCache {

    private static final MillionaireReqCache _INSTANCE = new MillionaireReqCache();

    private Map<String, List<JSONObject>> requestCache = new ConcurrentHashMap<String, List<JSONObject>>();

    private MillionaireReqCache() {
    }

    public static MillionaireReqCache getInstance() {
        return _INSTANCE;
    }

    public void addRequest(String receiver, JSONObject message) {
        synchronized (requestCache) {
            List<JSONObject> requests = requestCache.get(receiver);
            if (requests == null) {
                requests = new LinkedList<>();
            }
            requests.add(message);
            requestCache.put(receiver, requests);
        }
    }

    public JSONObject getRequests(String name) {
        JSONObject result = null;
        synchronized (requestCache) {
            List<JSONObject> set = requestCache.get(name);
            if (set != null && set.size() > 0) {
                result = set.remove(0);
            }
        }
        return result;
    }
}