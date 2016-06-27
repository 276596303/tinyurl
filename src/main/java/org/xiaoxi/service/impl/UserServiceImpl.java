package org.xiaoxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.xiaoxi.cache.client.CacheClient;
import org.xiaoxi.dao.UserDao;
import org.xiaoxi.entity.User;
import org.xiaoxi.exception.TinyurlException;
import org.xiaoxi.service.UserServiceInterface;

/**
 * Created by YanYang on 2016/6/23.
 */
@Service("UserServiceImpl")
public class UserServiceImpl implements UserServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final String slat = "shghghdjfhfjfhfjfjhskskhfahfa";

    @Autowired
    private UserDao userDao;

    public String insert(String username, String password)throws Exception{
        String token = null;
        try {
            if (username.equals("") || password.equals("")) {
                return "";
            }
            token = getMD5(username, password);
            password = getMD5(password);
            int flag = userDao.insert(username, password, token);
            if (flag < 0) {
                throw new RuntimeException("insert user failure");
            } else {
                CacheClient.set(username, token);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return token;
    }

    public User get(String username) throws Exception{
        try {
            return userDao.getByUsername(username);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean validUser(String username, String token) throws Exception{
        boolean valid = false;
        String existToken = "";
        try {
            //先从缓存中获取token
            existToken = CacheClient.get(username);
            //缓存中不存在，从数据库中获取
            if (existToken == null || existToken.equals("")) {
                User user = userDao.getByUsername(username);
                if (user != null) {
                    existToken = user.getToken();
                    //放入缓存
                    CacheClient.set(username, existToken);
                }
            }
            if (!token.equals("") && token.equals(existToken)) {
                valid = true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return valid;
    }

    //查看用户名是否存在
    public boolean isExist(String username) throws Exception{
        try {
            if (CacheClient.get(username) != null) {
                return true;
            } else {
                User user = get(username);
                if (user != null) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String getMD5(String username, String password) {
        String base = username + password + slat;
        String md5 = getMD5(base);
        return md5;
    }

    private String getMD5(String str) {
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}