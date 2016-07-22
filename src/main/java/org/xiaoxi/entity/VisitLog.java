package org.xiaoxi.entity;

import java.util.Date;

/**
 * Created by YanYang on 2016/7/20.
 */
public class VisitLog {
    private int id;
    private String host;
    private Date visitTime;
    private int cnt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "VisitLog{" +
                "id=" + id +
                ", host=" + host +
                ", visitTime=" + visitTime +
                ", cnt=" + cnt +
                '}';
    }
}
