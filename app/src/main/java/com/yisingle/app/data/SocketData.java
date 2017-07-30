package com.yisingle.app.data;

import java.io.Serializable;

/**
 * Created by jikun on 17/7/24.
 */

public class SocketData<T> extends SocketHeadData implements Serializable {
    private T response;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
