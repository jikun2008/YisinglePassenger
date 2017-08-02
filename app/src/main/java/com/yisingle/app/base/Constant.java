package com.yisingle.app.base;

/**
 * Created by jikun on 17/5/23.
 */

public class Constant {


    private static final String WS = "ws://";
    private static final String HTPP = "http://";
    private static final String IP = "119.23.51.14";//阿里云IP
    private static final String LOCAL_IP = "192.168.2.3";
    private static final String PORT = "8080";
    public static final String LOGIN_PASSENGER_ID = "login_passenger_id";


    public static final String PHONE_NUM = "phone_number";

    public static final String PASS_WORD = "pass_word";

    public static final String IS_LOGIN_SUCCESS = "is_login_success";


    public static String getBaseUrl() {

        return HTPP + LOCAL_IP + ":" + PORT + "/";
    }

    public static String getWsBaseUrl() {
        return WS + LOCAL_IP + ":" + PORT + "/";
    }


}
