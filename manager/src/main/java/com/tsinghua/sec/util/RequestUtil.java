package com.tsinghua.sec.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 15-12-12.
 */
public class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static Object toClassBean(String content, Class clazz) throws Exception {
        try {
            JSONObject paramObj = JSON.parseObject(content);
            return JSON.toJavaObject(paramObj, clazz);
        } catch (Exception e) {
            LOGGER.error("参数转换异常", e);
            throw new RuntimeException("参数转换异常");
        }
    }
}
