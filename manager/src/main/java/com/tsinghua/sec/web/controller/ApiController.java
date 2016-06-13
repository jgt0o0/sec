package com.tsinghua.sec.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.sec.cache.*;
import com.tsinghua.sec.domain.Solder;
import com.tsinghua.sec.service.PointNum;
import com.tsinghua.sec.service.ShareKeys;
import com.tsinghua.sec.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by ji on 16-5-23.
 */
@Controller
@RequestMapping("/api/")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    /**
     * Client上线后会向Server发送public key
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("register")
    public JSONObject register(String message) {
        LOGGER.error("请求{}", message);
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String name = param.getString("name");
            String key = param.getString("key");

            ClientCache.getInstance().addOnlineClient(name, key);

            pageResult.setObj(SolderCache.getInstance().getSolder(name));
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("注册失败{}", message, e);
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

    /**
     * 获取在线节点列表
     *
     * @return 士兵名字和public key
     */
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

    /**
     * 获取本次行动士兵列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getSolders")
    public JSONObject getSolders() {
        PageResult pageResult = new PageResult();
        try {
            List<Solder> solders = SolderCache.getInstance().getSolders();
            for (Solder s : solders) {
//                s.setRank(null);
            }
            pageResult.setList(solders);
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取士兵列表失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    /**
     * 查询日志
     *
     * @return
     */
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


    /**
     * Client发送认证请求,Server会缓存请求等待目标Client领取request
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("auth")
    public JSONObject auth(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String target = param.getString("receiver");
            String source = param.getString("sender");
            if (StringUtils.isEmpty(target)) {
                throw new RuntimeException("没有认证目标");
            }
            RequestCache.getInstance().setAuthRequest(target, param);
            pageResult.setCode(0);
            LogMessageCache.getInstance().writeMsg("[" + source + "]向[" + target + "] 发起认证请求");
        } catch (Exception e) {
            LOGGER.error("认证失败{}", message, e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getAuthRequest")
    public JSONObject getAuthRequest(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String name = param.getString("name");
            pageResult.setList(RequestCache.getInstance().getAuthRequest(name));
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取认证信息异常{}", message, e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    /**
     * 当目标Client人证成功之后会给Server反馈认证成功
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("authSuccess")
    public JSONObject authSuccess(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String source = param.getString("source");
            String target = param.getString("target");
            SolderCache.getInstance().setAuth(source, target);
            LogMessageCache.getInstance().writeMsg("[" + source + "]成功认证[" + target + "]");
        } catch (Exception e) {
            LOGGER.error("处理认证成功消息异常{}", message, e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    /**
     * Server页面定时获取已认证的信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getAuthInfo")
    public JSONObject getAuthInfo() {
        PageResult pageResult = new PageResult();
        try {
            pageResult.setObj(SolderCache.getInstance().getAuthCache());
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取认证信息失败", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    /**
     * 百万富翁请求
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("millionaire")
    public JSONObject millionaire(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);

            String receiver = param.getString("receiver");
            if (receiver.equals("all")) {
                Map<String, String> onlineClients = ClientCache.getInstance().getOnlineClient();
                if (onlineClients != null && onlineClients.size() > 0) {
                    for (String name : onlineClients.keySet()) {
                        MillionaireReqCache.getInstance().addRequest(name, param);
                    }
                }
                String msg = param.getString("msg");
                LogMessageCache.getInstance().writeMsg("[最高指挥官]是:" + msg);
            } else {
                MillionaireReqCache.getInstance().addRequest(receiver, param);
                try {

                    String msg = param.getString("msg");
                    if (msg.contains("Step=0")) {
                        String[] msgArray = msg.split("&");
                        String stage = msgArray[5];
                        LogMessageCache.getInstance().writeMsg("[百万富翁算法阶段]" + stage);
                    }
                } catch (Exception e) {
                    LOGGER.error("解析百万富翁msg异常", e);
                }
            }
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("百万富翁请求失败{}j", message, e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    /**
     * 客户端获取百万富翁请求
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("getMillionaireReq")
    public JSONObject getMillionaireReq(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String name = param.getString("name");
            pageResult.setObj(MillionaireReqCache.getInstance().getRequests(name));
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("获取百万富翁请求异常{}", message, e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }


    /**
     * client 发送开箱请求
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("openBoxRequest")
    public JSONObject openBoxRequest(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String name = param.getString("name");
            String pwd = param.getString("password");
            RequestCache.getInstance().setOpenBoxRequest(name, pwd);
            pageResult.setCode(0);
            LogMessageCache.getInstance().writeMsg("[" + name + "]开始寻找箱子");
        } catch (Exception e) {
            LOGGER.error("尝试开箱子异常", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("getOpenBoxRequest")
    public JSONObject getOpenBoxRequest() {
        PageResult pageResult = new PageResult();
        try {
            pageResult.setList(new ArrayList(RequestCache.getInstance().getOpenBoxRequest().keySet()));
            pageResult.setCode(0);
        } catch (Exception e) {
            LOGGER.error("Server 获取开箱请求异常", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }

    @ResponseBody
    @RequestMapping("openBox")
    public JSONObject openBox(String message) {
        PageResult pageResult = new PageResult();
        try {
            JSONObject param = JSON.parseObject(message);
            String solder = param.getString("name");
            Set<String> solders = new HashSet<String>();
            if (!BoxStatusCache.getInstance().isOpened()) {
                solders = BoxStatusCache.getInstance().addOpenOp(solder);
                List<PointNum> pointNums = new ArrayList<PointNum>();
                for (String tmpSolder : solders) {
                    String pwd = SolderCache.getInstance().getSolder(tmpSolder).getPassword();
                    if (pwd != null) {
                        String[] pairs = pwd.split(",");
                        double x = Double.parseDouble(pairs[0]);
                        double y = Double.parseDouble(pairs[1]);
                        PointNum pointNum = new PointNum(x, y);
                        pointNums.add(pointNum);
                    }
                }
                int result = ShareKeys.getInstance().SolveKey(pointNums);
                if (result == ShareKeys.KEY_NUM) {
                    BoxStatusCache.getInstance().setOpened();
                    LogMessageCache.getInstance().writeMsg("[" + solders + "]成功打开箱子");
                } else {
                    LogMessageCache.getInstance().writeMsg("[" + solders + "]尝试打开箱子失败");
                }
            } else {
                LogMessageCache.getInstance().writeMsg("箱子已经被打开了");
            }


        } catch (Exception e) {
            LOGGER.error("开箱异常", e);
            pageResult.setCode(-1);
        }
        return pageResult.toJson();
    }
}
