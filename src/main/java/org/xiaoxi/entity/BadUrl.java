package org.xiaoxi.entity;

/**
 * Created by YanYang on 2016/7/22.
 */
public class BadUrl {
    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BadUrl{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
