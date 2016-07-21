package org.xiaoxi.async;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxi.utils.RedisAdapter;

/**
 * Created by YanYang on 2016/7/20.
 */
@Service
public class EventProducer {
    @Autowired
    RedisAdapter redisAdapter;

    public boolean fireEvent(EventModel model) {
        try {
            String json = JSONObject.toJSONString(model);
            String key = redisAdapter.getEventKey();
            redisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
