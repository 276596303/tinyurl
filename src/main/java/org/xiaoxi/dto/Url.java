package org.xiaoxi.dto;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * Created by YanYang on 2016/6/18.
 */
public class Url {
    private String short_url;

    private String long_url;

    public Url(String short_url, String long_url) {
        this.short_url = short_url;
        this.long_url = long_url;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public String getLong_url() {
        return long_url;
    }

    public void setLong_url(String long_url) {
        this.long_url = long_url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "short_url='" + short_url + '\'' +
                ", long_url='" + long_url + '\'' +
                '}';
    }
}
