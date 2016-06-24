package org.xiaoxi.cache;

import org.junit.Test;
import org.xiaoxi.cache.client.CacheClient;

/**
 * Created by YanYang on 2016/6/19.
 */

public class TestCacheImpl {
    @Test
    public void testCache() throws Exception{
        CacheClient.set("001", "www.baidu.com");
        CacheClient.set("www.baidu.com", "001");

        System.out.println(CacheClient.get("001"));

        Thread.sleep(999);
        System.out.println(CacheClient.get("www.baidu.com"));
    }
}
