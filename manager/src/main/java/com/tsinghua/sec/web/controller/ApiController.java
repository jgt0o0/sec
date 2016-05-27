package com.tsinghua.sec.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.sec.cache.ClientCache;
import com.tsinghua.sec.cache.LogMessageCache;
import com.tsinghua.sec.cache.SolderCache;
import com.tsinghua.sec.domain.Solder;
import com.tsinghua.sec.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ji on 16-5-23.
 */
@Controller
@RequestMapping("/api/")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @ResponseBody
    @RequestMapping("register")
    public JSONObject register(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String name = param.getString("name");
            String key = param.getString("key");

            ClientCache.getInstance().addOnlineClient(name, key);

            pageResult.setObj(SolderCache.getInstance().getSolder(name));
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取在线列表失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getOnlineSolder")
    public JSONObject getOnlineSolder() {
        PageResult pageResult = new PageResult();
        try {
            pageResult.setObj(ClientCache.getInstance().getOnlineClient());
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取在线列表失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getOnlineClient")
    public JSONObject getOnlineClient() {
        PageResult pageResult = new PageResult();
        try {
            List<JSONObject> list = new ArrayList<>();
            Map<String, String> clients = ClientCache.getInstance().getOnlineClient();
            if (clients != null && clients.size() > 0) {
                for (Map.Entry<String, String> entry : clients.entrySet()) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", entry.getKey());
                    obj.put("key", entry.getValue());
                    list.add(obj);
                }
            }
            pageResult.setList(list);
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取在线列表失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getSolders")
    public JSONObject getSolders() {
        PageResult pageResult = new PageResult();
        try {
            List<Solder> solders = SolderCache.getInstance().getSolders();
            pageResult.setList(solders);
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取士兵列表失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getLog")
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


    @ResponseBody
    @RequestMapping("auth")
    public JSONObject auth(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String source = param.getString("source");
            String target = param.getString("target");
            SolderCache.getInstance().setAuth(source, target);
        } catch (Exception e) {
            LOGGER.error("认证失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getAuthInfo")
    public JSONObject getAuthInfo() {
        PageResult pageResult = new PageResult();
        try {
            pageResult.setObj(SolderCache.getInstance().getAuthCache());
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取认证信息");
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("openBox")
    public JSONObject openBox(String message) {
        PageResult pageResult = new PageResult();
        try {

        } catch (Exception e) {
            LOGGER.error("尝试开箱子异常", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }
}
