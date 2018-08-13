package com.yisingle.app.http;

import android.net.ParseException;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonParseException;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;

/**
 *
 */
public final class ErrorHandler {

    private static final String ERROR_CODE_TIME_OUT = "ERR_TIME_OUT";
    private static final String ERROR_CODE_HTTP_EX = "ERR_HTTP";
    private static final String ERROR_CODE_PARSE_EX = "ERR_PARSE";
    private static final String ERROR_CODE_BUSY_EX = "ERR_BUSY";
    private static final String ERROR_CODE_NET_EX = "ERR_NETWORK";

    private static Map<String, String> ERR = new HashMap<>(8);

    static {
        ERR.put(ERROR_CODE_TIME_OUT, "网络不畅，请稍后再试！");
        ERR.put(ERROR_CODE_HTTP_EX, "服务器异常，请稍后再试！");
        ERR.put(ERROR_CODE_PARSE_EX, "数据解析异常！");
        ERR.put(ERROR_CODE_BUSY_EX, "服务器繁忙！");
        ERR.put(ERROR_CODE_NET_EX, "请检查网络");
    }

    public static ApiException handleException(Throwable e, boolean showToast) {
        ApiException exception = parseException(e);
        if (showToast) {
            ToastUtils.showShortSafe(exception.getMessage());
        }
        return exception;
    }


    private static ApiException parseException(Throwable e) {
        if (e instanceof ApiException) {
            return (ApiException) e;
        } else if (e instanceof NetworkDisconnectException) {// 网络链接断开
            return new ApiException(ERROR_CODE_NET_EX, ERR.get(ERROR_CODE_NET_EX));
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException) {// 超时
            return new ApiException(ERROR_CODE_TIME_OUT, ERR.get(ERROR_CODE_TIME_OUT));
        } else if (e instanceof HttpException) {// server 异常
            return new ApiException(ERROR_CODE_HTTP_EX, ERR.get(ERROR_CODE_HTTP_EX));
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            return new ApiException(ERROR_CODE_PARSE_EX, ERR.get(ERROR_CODE_PARSE_EX));
        } else {
            return new ApiException(ERROR_CODE_BUSY_EX, ERR.get(ERROR_CODE_BUSY_EX));
        }
    }
}
