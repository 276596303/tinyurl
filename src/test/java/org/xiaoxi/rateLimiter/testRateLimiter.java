package org.xiaoxi.rateLimiter;

import com.sun.scenario.effect.impl.prism.PrImage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaoxi.filter.RateLimiterFilter;
import org.xiaoxi.rateLimiter.dao.RateHandle;
import org.xiaoxi.rateLimiter.dao.impl.RateHandleImpl;
import org.xiaoxi.rateLimiter.enums.Rate;

import java.util.concurrent.ExecutorService;

/**
 * Created by YanYang on 2016/6/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class testRateLimiter {

    @Autowired
    private RateHandle rateHandle;

    @Autowired
    private RateLimiterFilter rateLimiterFilter;

    @Test
    public void testRateLimiter() throws Exception {

        if (rateLimiterFilter != null) {
            System.out.println("we got the filter");
        }
        String action = "POST";
        String feature = "xiaoxi";

        long start_time = System.currentTimeMillis();

        for (int i = 0; i < 5; i++) {
            test(action, feature);
            Thread.sleep(10);
        }

        long end_time = System.currentTimeMillis();

        //System.out.println("耗时" + (end_time - start_time));
    }

    private void test(String action, String feature) throws Exception{
        for (int i = 0; i < 19; i++) {

            long current_time = System.currentTimeMillis();

            boolean isLimit = rateHandle.isLimit(action, feature, current_time, 20, Rate.SECOND);

            if (isLimit) {
                System.out.println("限制");
                break;
            }
        }
    }
}
