package com.yisingle.app.utils;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class JsonUtils {

    //protected final static Log logger = LogFactory.getLog(JsonUtils.class);

    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {

        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }
}