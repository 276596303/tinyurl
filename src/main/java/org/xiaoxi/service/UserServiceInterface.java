package org.xiaoxi.service;

import org.xiaoxi.entity.User;

/**
 * Created by YanYang on 2016/6/23.
 */
public interface UserServiceInterface {

    String insert(String username, String password) throws Exception;

    User get(String username) throws Exception;

    boolean validUser(String username, String token) throws Exception;

    boolean isExist(String username) throws Exception;
}
