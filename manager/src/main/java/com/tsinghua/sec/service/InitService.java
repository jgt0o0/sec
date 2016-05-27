package com.tsinghua.sec.service;

import com.tsinghua.sec.util.PropertiesUtil;

/**
 * Created by ji on 16-5-27.
 */
public class InitService {
    public void init() {
        PropertiesUtil.getInstance();
    }
}
