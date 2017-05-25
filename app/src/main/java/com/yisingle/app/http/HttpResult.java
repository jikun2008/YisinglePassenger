package com.yisingle.app.http;

import java.io.Serializable;

/**
 * @author yu
 */
public class HttpResult<T> implements Serializable {
    private String ret_string;
    private int ret_code = -1;
    private T response;

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
