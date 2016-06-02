package com.tsinghua.sec.cache;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-5-27.
 */
public class RequestCache {

    //认证请求的cache
    private static final Map<String, List<JSONObject>> authRequestCache = new ConcurrentHashMap<String, List<JSONObject>>();

    //Client发起的开箱请求
    private static final Map<String, String> openBoxRequest = new ConcurrentHashMap<String, String>();

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

    public void setOpenBoxRequest(String solder, String password) {
        openBoxRequest.put(solder, password);
    }

    public Map<String, String> getOpenBoxRequest() {
        Map<String, String> resultList = new HashMap<String, String>();
        synchronized (openBoxRequest) {
            if (openBoxRequest != null && openBoxRequest.size() > 0) {
                resultList = new HashMap<String, String>(openBoxRequest);
                openBoxRequest.clear();
            }
        }
        return resultList;
    }

    public String getOpenBoxPwd(String name) {
        return openBoxRequest.get(name);
    }

}
