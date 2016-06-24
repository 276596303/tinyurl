package org.xiaoxi.rateLimiter;

import com.sun.scenario.effect.impl.prism.PrImage;
import org.junit.Test;
import org.xiaoxi.rateLimiter.dao.impl.RateHandleImpl;
import org.xiaoxi.rateLimiter.enums.Rate;

import java.util.concurrent.ExecutorService;

/**
 * Created by YanYang on 2016/6/24.
 */
public class testRateLimiter {

    private RateHandleImpl rateHandle = new RateHandleImpl();
    @Test
    public void testRateLimiter() throws Exception {

        String action = "POST";
        String feature = "xiaoxi";

        long start_time = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            test(action, feature);
        }

        long end_time = System.currentTimeMillis();

        System.out.println("耗时" + (end_time - start_time));
    }

    private void test(String action, String feature) throws Exception{
        for (int i = 0; i < 60; i++) {

            long current_time = System.currentTimeMillis();

            boolean isLimit = rateHandle.isLimit(action, feature, current_time, 10, Rate.SECOND);

            if (isLimit) {
                break;
            }
        }
    }
}
