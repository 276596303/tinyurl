package org.xiaoxi.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaoxi.entity.BadUrl;

/**
 * Created by YanYang on 2016/7/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class TestBadUrlDao {
    @Autowired
    BadUrlDao badUrlDao;

    @Test
    public void testBadUrlDao() {
        BadUrl badUrl = new BadUrl();
        badUrl.setUrl("www.baidu.com");

        System.out.println(badUrlDao.selectAll());
    }
}
