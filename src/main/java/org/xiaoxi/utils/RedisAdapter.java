package org.xiaoxi.utils;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;


/**
 * Created by YanYang on 2016/7/20.
 */
@Component
public class RedisAdapter implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(RedisAdapter.class);

    private static String SPLIT = ":";
    private static String EVENT = "EVENT";
    private static String USER_PREFIX = "USER" + SPLIT;

    private JedisPool jedisPool;

    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("127.0.0.1", 6379);
    }

    public String getEventKey() {
        return EVENT;
    }

    public String getUserPrefix() {
        return USER_PREFIX;
    }

    public String getSPLIT() {
        return SPLIT;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("消息放入队列失败" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("出队列异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("jedis set方法 异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("redis get方法异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public void setObject(String key, Object object) {
        set(key, JSON.toJSONString(object));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
