package com.map.library;

import android.text.TextUtils;

/**
 * Created by jikun on 17/9/27.
 */

public class NaviVoiceUtils {


    private final static String[] judgeContainStrings = {
            "大约需要",
            "违章拍照",
            "测速拍照",
            "车辆汇入",
            "当前车速"
    };

    private final static String[] judgeEqualsStrings = {
            "请走右侧车道",
            "请走左侧车道"
    };

    /**
     * 判断导航回调的语音文本是否重要
     */
    public static boolean isImportantNavigationText(String text) {
        boolean isImport = true;
        if (TextUtils.isEmpty(text)) {
            isImport = false;
        } else {
            for (int i = 0; i < judgeContainStrings.length; i++) {

                if (text.contains(judgeContainStrings[i])) {
                    isImport = false;
                    break;
                }
            }
            for (int i = 0; i < judgeEqualsStrings.length; i++) {
                if (text.equals(judgeEqualsStrings[i])) {
                    isImport = false;
                    break;
                }
            }
        }


        return isImport;

    }


}
