package org.techtown.messagetts;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Locale;

public class BBTTS extends Service implements TextToSpeech.OnInitListener {
    private static final String TAG = "TTS";

    public static BBTextToSpeech BBtts = new BBTextToSpeech();
    //private String text = "";

    @Override
    public void onCreate() {
        BBtts.tts = new TextToSpeech(this, this);
        BBtts.tts.setLanguage(Locale.KOREAN);
        BBtts.tts.setPitch(1.0f);
        BBtts.tts.setSpeechRate(1.0f);
        Log.d(TAG, "tts 초기화됨");

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.v(TAG, "onStart 호출됨");

        super.onStart(intent, startId);
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "onInit 호출됨");
    }

    @Override
    public void onDestroy() { // 오류 방지 (tts 객체 삭제)
        super.onDestroy();

        if (BBtts.tts != null) {
            BBtts.tts.stop();
            BBtts.tts.shutdown();
            BBtts.tts = null;
        }

        Log.d(TAG, "onDestory 호출됨");
    }

    static class BBTextToSpeech {
        TextToSpeech tts;
        public void speak(String text) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
            } else { // API 20
                tts.speak(text, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

}