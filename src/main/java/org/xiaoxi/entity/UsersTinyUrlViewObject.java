package org.xiaoxi.entity;

/**
 * Created by YanYang on 2016/7/24.
 */
public class UsersTinyUrlViewObject {
    private String shortUrl;
    private String host;
    private int count;

    public UsersTinyUrlViewObject() {
    }

    public UsersTinyUrlViewObject(String shortUrl, String host, int count) {
        this.shortUrl = shortUrl;
        this.host = host;
        this.count = count;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UsersTinyUrlViewObject{" +
                "shortUrl='" + shortUrl + '\'' +
                ", host='" + host + '\'' +
                ", count=" + count +
                '}';
    }
}
