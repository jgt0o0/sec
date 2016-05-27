package com.tsinghua.sec.cache;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;

/**
 * Created by ji on 16-5-27.
 */
public class BoxStatusCache {
    private volatile boolean isOpened = false;

    private static Set<String> openOps = new ConcurrentSet<String>();

    private static final BoxStatusCache _INSTANCE = new BoxStatusCache();

    private BoxStatusCache() {
    }

    public static BoxStatusCache getInstance() {
        return _INSTANCE;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened() {
        isOpened = true;
    }


    public Set<String> addOpenOp(String solder) {
        openOps.add(solder);
        return openOps;
    }

}
