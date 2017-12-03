package com.map.library;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 语音播报组件
 */
public class TTSController implements SynthesizerListener {

    private Context mContext;
    // 默认本地发音人
    public static String voicerLocalName = "xiaoyan";
    private static TTSController ttsManager;
    private SpeechSynthesizer mTts;
    private Queue<String> queue = new LinkedList<>();

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("SHIXIN", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败,错误码：" + code, Toast.LENGTH_SHORT).show();
                ;
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
                // startSpeaking("神马专车欢迎您");
            }
        }
    };

    private TTSController(Context context) {
        mContext = context.getApplicationContext();
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void init() {

        String text = "58385104";
        SpeechUtility.createUtility(mContext, "appid=" + text);

        // 初始化合成对象.
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        initSpeechSynthesizer();
    }


    public void startQueueToSpeak(String text) {
        if (queue.isEmpty() || !mTts.isSpeaking()) {
            queue.offer(text);
            mTts.startSpeaking(text, this);
        } else {
            queue.offer(text);
        }

    }

    /**
     * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
     *
     * @param
     */
    public void startSpeaking(String playText) {
        Log.e("测试代码", "测试代码导航文字:" + playText + ":" + NaviVoiceUtils.isImportantNavigationText(playText));
        // 进行语音合成.
        if (mTts != null) {
            if (NaviVoiceUtils.isImportantNavigationText(playText)) {
                //如果是重要的语音信息,直接语音合成
                mTts.startSpeaking(playText, this);
            } else {
                //不重要的语音信息,只有在语音不在合成的时候，才进行合成
                if (!mTts.isSpeaking()) {
                    mTts.startSpeaking(playText, this);
                }
            }

        }


    }

    public void stopSpeaking() {
        if (mTts != null)
            mTts.stopSpeaking();
    }


    private void initSpeechSynthesizer() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
//        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        // 设置在线合成发音人
//        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

        //设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocalName);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");

    }

    public void destroy() {
        queue.clear();
        if (mTts != null) {
            mTts.stopSpeaking();
            mTts.destroy();
            mTts = null;
            ttsManager = null;
        }
    }


    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {

        for (String test : queue) {
            Log.e("测试代码", "测试代码队列" + test);
        }
        queue.poll();
        for (String test : queue) {
            Log.e("测试代码", "poll后测试代码队列" + test);
        }
        if (!queue.isEmpty()) {
            String info = queue.peek();
            Log.e("测试代码", "测试代码队列" + queue.peek());
            if (null != info) {
                mTts.startSpeaking(info, this);
            }
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    //获取本地的发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + voicerLocalName + ".jet"));
        return tempBuffer.toString();
    }


}
