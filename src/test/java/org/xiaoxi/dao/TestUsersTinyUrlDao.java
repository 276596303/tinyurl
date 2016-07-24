package org.xiaoxi.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaoxi.entity.UsersTinyUrl;

import java.util.List;

/**
 * Created by YanYang on 2016/7/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class TestUsersTinyUrlDao {
    @Autowired
    UsersTinyUrlDao usersTinyUrlDao;

    @Test
    public void testUsersTinyUrldao() {
        UsersTinyUrl usersTinyUrl = new UsersTinyUrl();
        usersTinyUrl.setUserId(1);
        usersTinyUrl.setShortUrlId(1);

        usersTinyUrlDao.insert(usersTinyUrl);

        List<UsersTinyUrl> lists = usersTinyUrlDao.selectByUserId(1);

        System.out.println(lists);
    }
}
