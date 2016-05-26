package com.tsinghua.sec.cache;

import com.tsinghua.sec.domain.Solder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ji on 16-5-26.
 */
public class SolderCache {

    private static final Map<String, Solder> solderCache = new HashMap<String, Solder>();

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
}
