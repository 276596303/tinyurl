package org.xiaoxi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaoxi.entity.VisitLog;

import java.util.List;

/**
 * Created by YanYang on 2016/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class TestVisitLogService {
    @Autowired
    VisitLogService visitLogService;

    @Test
    public void testVisitLogService() {
        List<VisitLog> list = visitLogService.getAllUrlCountDataOneMonth();
        System.out.println(list);
    }
}
