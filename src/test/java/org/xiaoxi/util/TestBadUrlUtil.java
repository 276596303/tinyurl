package org.xiaoxi.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaoxi.utils.BadUrlUtil;

/**
 * Created by YanYang on 2016/7/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class TestBadUrlUtil {
    @Autowired
    BadUrlUtil badUrlUtil;

    @Test
    public void testBadUrlUtil() {
        long startTime = System.currentTimeMillis();
        String badUrl = "www.baid.com";
        long endTime = System.currentTimeMillis();
        System.out.println(badUrl + "存在字典树中 " + badUrlUtil.containsBadUrl(badUrl));
        System.out.println("耗时 " + (endTime - startTime));
    }
}
