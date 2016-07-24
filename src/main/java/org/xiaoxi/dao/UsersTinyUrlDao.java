package org.xiaoxi.dao;

import org.xiaoxi.entity.UsersTinyUrl;

import java.util.List;

/**
 * Created by YanYang on 2016/7/24.
 */
public interface UsersTinyUrlDao {
    int insert(UsersTinyUrl usersTinyUrl);

    List<UsersTinyUrl> selectByUserId(int userId);
}
