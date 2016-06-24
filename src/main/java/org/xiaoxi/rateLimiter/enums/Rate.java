package org.xiaoxi.rateLimiter.enums;

/**
 * Created by YanYang on 2016/6/24.
 */
public enum Rate {

    SECOND("/s"),
    MINUTE("/m"),
    HOUR("/h"),
    DAY("/d");

    private String unit;

    Rate(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
