package org.xiaoxi.rateLimiter.dao;

import org.xiaoxi.rateLimiter.enums.Rate;

/**
 * Created by YanYang on 2016/6/24.
 */
public interface RateHandle {

    void insert(String action, String feature, long timestamp, Rate rate, boolean increment) throws Exception;

    int sum(String action, String feature, long current_time, Rate rate);

    boolean isLimit(String aciton, String feature, long current_time, int number, Rate rate) throws Exception;
}
