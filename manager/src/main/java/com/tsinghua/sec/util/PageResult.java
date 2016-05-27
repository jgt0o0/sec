package com.tsinghua.sec.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by ji on 15-10-15.
 */
public class PageResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final SerializeConfig mapping = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 返回业务编码
     */
    private int code;

    /**
     * 返回结果信息
     */
    private String message;

    /**
     * 总记录数
     */
    private Long totalCount;

    /**
     * 集合形式的结果
     */
    private List list;
    /**
     * 对象形式的结果
     */
    private Object obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        PageResult result = this;
        if (result == null) {
            result = new PageResult();
            result.setCode(1);
            result.setMessage("数据不存在");
        }

        List dataList = result.getList();
        if (dataList != null && dataList.size() > 0) {
            JSONArray jsonArray = list2Array(dataList);
            json.put("rows", jsonArray);
        }

        if (result.getTotalCount() != null) {
            json.put("total", result.getTotalCount());
        }

        if (result.getObj() != null) {
            json.put("obj", JSON.parse(JSON.toJSONString(result.getObj(), mapping)));
        }

        json.put("code", result.getCode());
        json.put("message", result.getMessage());
        return json;
    }

    /**
     * List 转为 JsonArray, <b>List不能为空</b>
     *
     * @param list 要转换的list
     * @return JsonArray数组
     */
    private static JSONArray list2Array(List list) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : list) {
            Class c = item.getClass();
            if (c == int.class || c == Integer.class ||
                    c == long.class || c == Long.class ||
                    c == float.class || c == Float.class ||
                    c == double.class || c == Double.class ||
                    c == boolean.class || c == Boolean.class ||
                    c == byte.class || c == Byte.class ||
                    c == char.class || c == Byte.class ||
                    c == short.class || c == Short.class ||
                    c == String.class) {
                jsonArray.add(item);
            } else {
                jsonArray.add(JSON.parse(JSON.toJSONString(item, mapping)));
            }
        }
        return jsonArray;
    }

}
