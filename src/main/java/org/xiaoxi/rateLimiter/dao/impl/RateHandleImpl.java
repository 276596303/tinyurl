package org.xiaoxi.rateLimiter.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xiaoxi.rateLimiter.dao.RateHandle;
import org.xiaoxi.rateLimiter.dao.RedisClient;
import org.xiaoxi.rateLimiter.enums.Rate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by YanYang on 2016/6/24.
 */
@Component("RateHandleImpl")
public class RateHandleImpl implements RateHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateHandleImpl.class);

    // 记录访问（log）
    public void insert(String action, String feature, long timestamp, Rate rate, boolean increment) throws Exception{

        int diff = 1;
        int clock = 1;
        if (rate == Rate.SECOND) {
            diff = diff * 100;  //  100毫秒/Bucket
            clock = clock * 1;  //  定时1秒钟
        } else if (rate == Rate.MINUTE) {
            diff = diff * 1000;  //  1秒/Bucket
            clock = clock * 60;  //  定时一分钟
        } else if (rate == Rate.HOUR) {
            diff = diff * 1000 * 60;  //  1分钟/Bucket
            clock = clock * 60 * 60;  //  定时一小时
        } else if (rate == Rate.DAY) {
            diff = diff * 1000 * 60 * 60;  //   一小时/Bucket
            clock = clock * 60 * 60 * 24;  //   定时一天
        }

        timestamp = timestamp / diff;  // 换算Bucket

        String key = action + feature + timestamp;
        try {
            if (RedisClient.get(key, "0") != null && increment) {
                RedisClient.incrBy(key, 1);
            } else {
                RedisClient.set(key, "1", clock);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean isLimit(String action, String feature, long current_time, int number, Rate rate) throws Exception{
        insert(action, feature, current_time, rate, true);

        //TODO 把求和的方法合并进来，优化时间
        int diff = 1;   // 作被除数（将当前时间换算成对应的Bucket）
        int size = 1;   //应该向前取多大的区间范围
        if (rate == Rate.SECOND) {
            diff = diff * 100;  //  100毫秒/Bucket
            size = size * 10;   // 向前找10个bucket
        } else if (rate == Rate.MINUTE) {
            diff = diff * 1000;  //  1秒/Bucket
            size = size * 60;   // 向前找60个bucket
        } else if (rate == Rate.HOUR) {
            diff = diff * 1000 * 60;  //  1分钟/Bucket
            size = size * 60;   // 向前找10个bucket
        } else if (rate == Rate.DAY) {
            diff = diff * 1000 * 60 * 60;  //   一小时/Bucket
            size = size * 24;   // 向前找10个bucket
        }

        current_time = current_time / diff;  // 换算Bucket

        int sum = 0;
        //把所有的bucket key放入array中进行批量查询
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            //TODO
            long bucket = current_time - i;
            StringBuffer sb = new StringBuffer();
            sb.append(action);
            sb.append(feature);
            sb.append(bucket);
            String key = sb.toString();
            keys.add(key);
        }
        List<String> values = RedisClient.pipeGet(keys, "0");

        for (String value : values) {
            sum += Integer.parseInt(value);
            if (sum > number) {
                return true;
            }
        }
        return false;
    }

    public int sum(String action, String feature, long current_time, Rate rate) {


        int diff = 1;   // 作被除数（将当前时间换算成对应的Bucket）
        int size = 1;   //应该向前取多大的区间范围
        if (rate == Rate.SECOND) {
            diff = diff * 100;  //  100毫秒/Bucket
            size = size * 10;   // 向前找10个bucket
        } else if (rate == Rate.MINUTE) {
            diff = diff * 1000;  //  1秒/Bucket
            size = size * 60;   // 向前找60个bucket
        } else if (rate == Rate.HOUR) {
            diff = diff * 1000 * 60;  //  1分钟/Bucket
            size = size * 60;   // 向前找10个bucket
        } else if (rate == Rate.DAY) {
            diff = diff * 1000 * 60 * 60;  //   一小时/Bucket
            size = size * 24;   // 向前找10个bucket
        }

        current_time = current_time / diff;  // 换算Bucket

        int sum = 0;

        for (int i = 0; i < size; i++) {
            long bucket = current_time - i;
            String count = RedisClient.get(action + feature + bucket, "0");
            sum = sum + Integer.parseInt(count);
        }
        return sum;
    }
}
