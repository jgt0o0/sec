package com.tsinghua.sec.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ji on 15-10-15.
 */
@Controller
@RequestMapping("/index/")
public class IndexController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("index")
    public String index() {
        return "index";
    }


    @ResponseBody
    @RequestMapping("rsa")
    public JSONObject rsa(String message) {
        return null;
    }

}
