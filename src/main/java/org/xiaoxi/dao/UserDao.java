package org.xiaoxi.dao;

import org.apache.ibatis.annotations.Param;
import org.xiaoxi.entity.User;

/**
 * Created by YanYang on 2016/6/23.
 */
public interface UserDao {
    int insert(@Param("username")String username,
                @Param("password")String password,
                @Param("token")String token);

    User getByUsername(@Param("username")String username);
}
