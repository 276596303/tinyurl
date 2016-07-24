package org.xiaoxi.async;

/**
 * Created by YanYang on 2016/7/20.
 */
public enum EventType {

    URL(0),
    USERS_TINYURL(1);

    private int type;
    EventType(int type) {
        this.type = type;
    }
}
