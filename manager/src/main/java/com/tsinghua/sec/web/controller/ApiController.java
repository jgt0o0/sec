package com.tsinghua.sec.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.sec.cache.ClientCache;
import com.tsinghua.sec.cache.LogMessageCache;
import com.tsinghua.sec.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ji on 16-5-23.
 */
@Controller
@RequestMapping("/api/")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @ResponseBody
    @RequestMapping("/getOnlineSolder")
    public JSONObject getOnlineSolder(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String name = param.getString("name");
            String key = param.getString("key");

            ClientCache.getInstance().add(name, key);

            Set<String> onlineUsers = ClientCache.getInstance().getOnlineClient();
            List<String> userList = new ArrayList<>(onlineUsers);
            pageResult.setList(userList);
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取在线列表失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("/getLog")
    public JSONObject getLog() {
        PageResult pageResult = new PageResult();
        try {
            List<String> logs = LogMessageCache.getInstance().getMessageList();
            pageResult.setList(logs);
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取日志异常", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }
}
