package com.yisingle.app.http;

import com.google.gson.Gson;
import com.yisingle.app.base.Constant;
import com.yisingle.app.http.interceptor.HeaderInterceptor;
import com.yisingle.app.http.interceptor.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yu
 *         Create on 16/5/19.
 */
public class RetrofitManager {

    private static final long DEFAULT_TIMEOUT = 5000l;

    private ApiService apiService;
    private Retrofit retrofit;

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.getBaseUrl())
                .client(genericClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private OkHttpClient genericClient() {


        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggerInterceptor("xueliang_HD", true))
                .build();

    }


    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile RetrofitManager instance;

    //定义一个共有的静态方法，返回该类型实例
    public static RetrofitManager getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (RetrofitManager.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public <T> T createService(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public ApiService getApiService() {
        if (apiService == null)
            apiService = retrofit.create(ApiService.class);
        return apiService;
    }


    public void destory() {
        instance = null;
    }


}
