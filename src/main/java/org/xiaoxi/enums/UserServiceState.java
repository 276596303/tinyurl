package org.xiaoxi.enums;

/**
 * Created by YanYang on 2016/6/23.
 */
public enum UserServiceState {

    SUCCESS("获取口令成功"),
    FAILURE("获取用户口令失败，请重新尝试");

    private String info;

    UserServiceState(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
