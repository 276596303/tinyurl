package org.xiaoxi.dto;

/**
 * Created by YanYang on 2016/6/18.
 */
public class TinyurlResult<T> {

    private int dataCode; //数据代码（表示返回的是什么类型的数据）

    private String dataDesc; //返回的数据类型描述

    private boolean success; //是否成功

    private T data; //数据

    private String error; //错误描述

    public TinyurlResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public TinyurlResult(boolean success, T data, int dataCode, String dataDesc) {
        this.success = success;
        this.data = data;
        this.dataCode = dataCode;
        this.dataDesc = dataDesc;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getDataCode() {
        return dataCode;
    }

    public void setDataCode(int dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    @Override
    public String toString() {
        return "TinyurlResult{" +
                "dataCode=" + dataCode +
                ", dataDesc='" + dataDesc + '\'' +
                ", success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
