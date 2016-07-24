package org.xiaoxi.utils;

import org.springframework.stereotype.Component;
import org.xiaoxi.entity.User;

/**
 * Created by YanYang on 2016/7/23.
 */
@Component
public class UserHolder {
    private ThreadLocal<User> threadLocal = new ThreadLocal<User>();

    public void add(User user) {
        threadLocal.set(user);
    }

    public User get(User user) {
        return threadLocal.get();
    }

    public void clear() {
        threadLocal.remove();
    }
}
