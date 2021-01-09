package org.techtown.messagetts;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

public class BBSoundPlayer {
    public static final int click_01 = R.raw.multimedia_click_1;
    public static final int click_02 = R.raw.multimedia_button_click_005;
    public static final int click_03 = R.raw.multimedia_button_click_003;
    public static final int click_04 = R.raw.sound_check;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void initSounds(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();

        soundPoolMap = new HashMap(2);
        soundPoolMap.put(click_01, soundPool.load(context, click_01, 1));
        soundPoolMap.put(click_02, soundPool.load(context, click_02, 2));
        soundPoolMap.put(click_03, soundPool.load(context, click_03, 3));
        soundPoolMap.put(click_04, soundPool.load(context, click_04, 4));
    }

    public static void play(int position){
        if( soundPoolMap.containsKey(position) ) {
            soundPool.play(soundPoolMap.get(position), 1, 1, 1, 0, 1f);
        }
    }
}
