package org.xiaoxi.exception;

/**
 * Created by YanYang on 2016/6/18.
 */
public class TinyurlException extends RuntimeException{
    public TinyurlException(String message){
        super(message);
    }

    public TinyurlException(String message, Throwable cause){
        super(message, cause);
    }
}
