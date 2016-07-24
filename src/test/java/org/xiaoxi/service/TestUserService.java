package org.xiaoxi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by YanYang on 2016/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class TestUserService {
    @Autowired
    private UserService userService;

    @Test
    public void testUserService() throws Exception{

        String username = "hongxiyang";
        boolean isExist = userService.isExist(username);

        System.out.println(isExist);
    }
}
