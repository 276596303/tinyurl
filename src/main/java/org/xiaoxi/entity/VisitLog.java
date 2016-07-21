package org.xiaoxi.entity;

import java.util.Date;

/**
 * Created by YanYang on 2016/7/20.
 */
public class VisitLog {
    private int id;
    private int shortUrl;
    private Date visitTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(int shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    @Override
    public String toString() {
        return "VisitLog{" +
                "id=" + id +
                ", shortUrl=" + shortUrl +
                ", visitTime=" + visitTime +
                '}';
    }
}
