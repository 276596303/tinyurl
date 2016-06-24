package org.xiaoxi.enums;

/**
 * Created by YanYang on 2016/6/18.
 */
public enum TinyurlStateEnum {

    SUCCESS("网址转换成功"),
    TRANSFER_FAILURE("转换失败"),
    CHECK_URL("核查网址是否有效"),
    VALID_FAILURE("请核查用户名与口令是否正确");

    private String stateInfo;

    TinyurlStateEnum(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
}
