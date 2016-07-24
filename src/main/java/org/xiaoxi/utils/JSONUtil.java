package org.xiaoxi.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by YanYang on 2016/7/23.
 */
public class JSONUtil {
    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String message) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
}
