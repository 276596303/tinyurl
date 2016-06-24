package org.xiaoxi.enums;

/**
 * Created by YanYang on 2016/6/23.
 */
public enum DataCode {

    TOKEN(1, "口令"),
    URL(2, "网址");

    private int code;
    private String desc;

    DataCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
