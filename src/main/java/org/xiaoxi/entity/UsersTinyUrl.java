package org.xiaoxi.entity;

/**
 * Created by YanYang on 2016/7/24.
 */
public class UsersTinyUrl {
    private int userId;
    private int shortUrlId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShortUrlId() {
        return shortUrlId;
    }

    public void setShortUrlId(int shortUrlId) {
        this.shortUrlId = shortUrlId;
    }

    @Override
    public String toString() {
        return "UsersTinyUrl{" +
                "userId=" + userId +
                ", shortUrlId=" + shortUrlId +
                '}';
    }
}
