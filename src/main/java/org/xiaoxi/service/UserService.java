package org.xiaoxi.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.xiaoxi.cache.client.CacheClient;
import org.xiaoxi.dao.UserDao;
import org.xiaoxi.entity.User;
import org.xiaoxi.utils.RedisAdapter;
import org.xiaoxi.utils.UserHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YanYang on 2016/6/23.
 */
@Service("UserService")
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final String slat = "shghghdjfhfjfhfjfjhskskhfahfa";

    @Autowired
    private UserDao userDao;

    @Autowired
    RedisAdapter redisAdapter;


    public Map<String, String> register(User user) {
        Map<String, String> map = new HashMap<String, String>();
        String username = user.getUsername();
        String password = user.getPassword();

        if (StringUtils.isBlank(username) || username.length() < 6) {
            map.put("usernameMessage", "用户名长度不能小于6个字符");
            return map;
        }
        if (StringUtils.isBlank(password) || password.length() < 6) {
            map.put("passwordMessage", "密码长度不能小于6个字符");
            return map;
        }

        if (getByUsername(username) != null) {
            map.put("usernameMessage", "用户名已被注册");
            return map;
        }

        int userId = addUser(user);
        if (userId != Integer.MIN_VALUE && userId > 0) {
            map.put("success", String.valueOf(userId));
            return map;
        } else {
            map.put("success", String.valueOf(Integer.MIN_VALUE));
            return map;
        }
    }

    public int addUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            if (username.equals("") || password.equals("")) {
                return Integer.MIN_VALUE;
            }
            password = getMD5(password);
            User usr = new User();
            usr.setUsername(username);
            usr.setPassword(password);
            int flag = userDao.insert(usr);
            int userId = usr.getId();
            if (flag < 0) {
                throw new RuntimeException("insert user failure");
            } else {
                return usr.getId();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Integer.MIN_VALUE;
        }
    }

    public User getByUsername(String username) {
        try {
            return userDao.getByUsername(username);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public User getByUserId(int userId) {
        try {
            return userDao.getByUserId(userId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public int validUser(User user) throws Exception{
        int userId = Integer.MIN_VALUE;
        String username = user.getUsername();
        String password = user.getPassword();
        password = getMD5(password);
        String USER_PREFIX = redisAdapter.getUserPrefix();
        String SPLIT = redisAdapter.getSPLIT();

        String key = USER_PREFIX + username + SPLIT + password;
        try {
            String value = redisAdapter.get(key);
            if (value != null && !"".equals(value)) {
                userId = Integer.valueOf(value);
                return userId;
            } else {
                User user1 = getByUsername(username);
                if (user1 != null && user1.getPassword().equals(password)) {
                    redisAdapter.set(key, String.valueOf(user1.getId()));
                    userId = user1.getId();
                    return userId;
                } else {
                    return userId;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    //查看用户名是否存在
    public boolean isExist(String username) {
        try {
            User user = getByUsername(username);
            if (user != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("验证用户名异常稍后重试");
        }
    }

    private String getMD5(String str) {
        str = slat + str;
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}