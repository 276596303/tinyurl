package org.xiaoxi.entity;

import java.util.Date;

/**
 * Created by YanYang on 2016/7/24.
 */
public class UsersTinyUrl {
    private int userId;
    private int shortUrlId;
    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UsersTinyUrl{" +
                "userId=" + userId +
                ", shortUrlId=" + shortUrlId +
                ", createTime=" + createTime +
                '}';
    }
}
