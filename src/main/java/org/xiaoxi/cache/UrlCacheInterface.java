package org.xiaoxi.cache;

/**
 * Created by YanYang on 2016/6/18.
 */
public interface UrlCacheInterface {
    String getShort_url(String long_url) throws Exception;

    String getLong_url(String short_url) throws Exception;

    void set(String key, String value) throws Exception;
}
