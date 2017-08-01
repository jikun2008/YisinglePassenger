package com.yisingle.app.http;


import com.yisingle.app.data.CarPositionData;
import com.yisingle.app.data.OrderData;
import com.yisingle.app.data.UserData;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {



    // 登陆乘客端账号
    @POST("yisingle/passenger/login.action")
    Observable<HttpResult<UserData>> loginPassener(@Body Map<String, String> params);
    // 注册乘客端账号
    @POST("yisingle/passenger/register.action")
    Observable<HttpResult<UserData>> registerPassener(@Body Map<String, String> params);

    // 获取定位位置周围的汽车
    @POST("yisingle/getNearByCarPosition.action")
    Observable<HttpResult<List<CarPositionData>>> getNearByCarPosition(@Body Map<String, String> params);


    // 发送订单
    @POST("yisingle/sendOrder.action")
    Observable<HttpResult<OrderData>> sendOrderData(@Body Map<String, String> params);
}