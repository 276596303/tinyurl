package org.xiaoxi.entity;

/**
 * Created by YanYang on 2016/6/18.
 * tinyurl entity
 */
public class Tinyurl {
    private long id;
    private String url;

    public Tinyurl() {
    }

    public Tinyurl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return "Tinyurl{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
