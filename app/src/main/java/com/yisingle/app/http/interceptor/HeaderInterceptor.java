package com.yisingle.app.http.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp全局header拦截器
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request()
                .newBuilder()
                //.addHeader("Authorization", "1232131231")
                //上面的请求头是有作用的
                //不知道为什么在这里添加请求头字段Content-Type没有作用
                //.addHeader("Content-Type", "application/json;charset=UTF-8")
                .build();

        return chain.proceed(request);
    }
}