package com.yisingle.app.http;

import java.io.Serializable;

/**
 * @author yu
 */
public class HttpResult<T> implements Serializable {
    private String errorMsg;
    private int code = -1;
    private T response;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
