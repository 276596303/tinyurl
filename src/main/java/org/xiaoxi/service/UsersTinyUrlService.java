package org.xiaoxi.service;

import com.sun.xml.internal.ws.developer.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxi.dao.UsersTinyUrlDao;
import org.xiaoxi.entity.UsersTinyUrl;

import java.util.Date;
import java.util.List;

/**
 * Created by YanYang on 2016/7/24.
 */
@Service
public class UsersTinyUrlService {
    private static final Logger logger = LoggerFactory.getLogger(UsersTinyUrlService.class);

    @Autowired
    UsersTinyUrlDao usersTinyUrlDao;

    public boolean addTinyUrlToUser(UsersTinyUrl usersTinyUrl) {
        try {
            if (usersTinyUrl.getCreateTime() == null) {
                usersTinyUrl.setCreateTime(new Date());
            }
            int success = usersTinyUrlDao.insert(usersTinyUrl);
            return true;
        } catch (Exception e) {
            logger.error("插入到失败" + e.getMessage());
            return false;
        }
    }

    public List<UsersTinyUrl> getTinyUrlByUserId(int userId) {
        try {
            List<UsersTinyUrl> result = usersTinyUrlDao.selectByUserId(userId);
            return result;
        } catch (Exception e) {
            logger.error("获取用户tinyurl失败" + e.getMessage());
            return null;
        }
    }

}
