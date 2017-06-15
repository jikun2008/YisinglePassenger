package com.yisingle.app.http;


import com.yisingle.app.data.CarPositionData;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {

    // 获取定位位置周围的汽车
    @POST("yisingle/getNearByCarPosition.action")
    Observable<HttpResult<List<CarPositionData>>> getNearByCarPosition(@Body Map<String, String> params);
}