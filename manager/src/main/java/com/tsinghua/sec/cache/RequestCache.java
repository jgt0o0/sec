package com.tsinghua.sec.cache;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-5-27.
 */
public class RequestCache {

    private static final Map<String, List<JSONObject>> authRequestCache = new ConcurrentHashMap<String, List<JSONObject>>();

    private static final RequestCache _INSTANCE = new RequestCache();

    private RequestCache() {
    }

    public static RequestCache getInstance() {
        return _INSTANCE;
    }

    public void setAuthRequest(String solder, JSONObject object) {
        synchronized (authRequestCache) {
            List<JSONObject> list = authRequestCache.get(solder);
            if (list != null) {
                list.add(object);
            } else {
                list = new ArrayList<>();
                list.add(object);
            }
            authRequestCache.put(solder, list);
        }
    }

    public List<JSONObject> getAuthRequest(String solder) {
        return authRequestCache.remove(solder);
    }
}
