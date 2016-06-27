package org.xiaoxi.rateLimiter.dao;

import org.junit.Test;
import org.xiaoxi.rateLimiter.enums.Rate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YanYang on 2016/6/27.
 */
public class testRedisClient {

    @Test
    public void testRedisClient() {

        for (int i = 0; i < 5; i++) {
            RedisClient.set("xiaoxi" + i, String.valueOf(i));
        }

        List<String> keys = new ArrayList<String>();
        keys.add("xiaoxi10");
        keys.add("xiaoxi1");
        keys.add("xiaoxi2");
        keys.add("xiaoxi3");
        keys.add("xiaoxi4");

        List<String> result = RedisClient.pipeGet(keys, "-1");

        System.out.println(result.size());
        for (Object o : result) {
            System.out.print(o + " ");
        }


    }
}
