package org.xiaoxi.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xiaoxi.cache.UrlCacheInterface;
import org.xiaoxi.cache.client.CacheClient;
import redis.clients.jedis.Jedis;

/**
 * Created by YanYang on 2016/6/18.
 */
@Component
public class UrlCacheImpl implements UrlCacheInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlCacheImpl.class);

    public String getShort_url(String long_url) throws Exception{
        try {
            return CacheClient.get(long_url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String getLong_url(String short_url) throws Exception{
        try {
            return CacheClient.get(short_url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void set(String key, String value) throws Exception{
        try {
            CacheClient.set(key, value);
            CacheClient.set(value, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
