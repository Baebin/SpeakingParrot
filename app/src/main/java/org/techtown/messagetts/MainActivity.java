package org.techtown.messagetts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.techtown.messagetts.R.drawable.tts_on;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private static final String TAG = "MainActivity";
    public static Context context_main;

    String file = "data";

    TextToSpeech tts;
    TextView textView_Switch;
    TextView textView_Volume;
    ImageView imageView_Switch;

    int volume = 100;
    boolean start = true;
    public static boolean debug = false;
    public static ArrayList<String> check;
    public static boolean firstStart;

    BBSoundPlayer bbSoundPlayer = new BBSoundPlayer();

    Intent serviceIntent, TTSserviceIntent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate 호출됨");

        bbSoundPlayer.initSounds(getApplicationContext()); // Setting Clear
        context_main = this;

        textView_Switch = findViewById(R.id.textView_Switch);
        textView_Volume = findViewById(R.id.textView_Volume);
        imageView_Switch = findViewById(R.id.imageView_Switch);

        SharedPreferences sf = getSharedPreferences(file, 0);
        start = sf.getBoolean("start", true);
        volume = sf.getInt("volume", 100);

        check = getCheckOptions();
        firstStart = getStart();

        if (!firstStart) {
            Intent intent_select = new Intent(MainActivity.this, PopUP_select.class);
            startActivity(intent_select);
            Log.d(TAG, "firstStart 호출됨");
        }

        Switch();
        Volume(volume);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.KOREAN);
                    tts.setPitch(1.0f);
                    tts.setSpeechRate(1.0f);
                    Log.d(TAG, "tts 초기화됨");
                }
            }
        });

        imageView_Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbSoundPlayer.play(BBSoundPlayer.click_01);
                playVIB();
                Switch();
                Log.d(TAG, "onClick 호출됨");
            }
        });

        SeekBar seekBar = findViewById(R.id.seekBar_Volume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bbSoundPlayer.play(BBSoundPlayer.click_02);
                volume = (progress*10);
                Volume(progress*10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar.setProgress(volume/10);

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        //showToast("권한 거부된 것: " + permissions.length);
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        //showToast("권한 허가된 것: " + permissions.length);
    }

    public void showToast(String data) {
        if (MainActivity.debug == true) {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();

        bbSoundPlayer.play(BBSoundPlayer.click_03);
        playVIB();

        switch (curId) {
            case R.id.menu_debug:
                debug = !debug;
                showToast("Debug Mode : " + debug);
                break;
            case R.id.menu_select:
                Intent intent_select = new Intent(MainActivity.this, PopUP_select.class);
                startActivity(intent_select);
                break;
            case R.id.menu_dev:
                Intent intent_dev = new Intent(MainActivity.this, PopUP.class);
                startActivity(intent_dev);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "onNewIntent 호출됨");
    }

    public void playSound(Intent intent) {
        if (intent != null) {
            String sender = intent.getStringExtra("sender");
            String contents = intent.getStringExtra("contents");
            Log.d("playSound", "sender: " + sender + ", contents: " + contents);
            Log.d(TAG, "playSound 호출됨, " + "sender: " + sender + ", contents: " + contents);

            //String text = sender + "님으로부터 온 메시지입니다 : " + contents;
            String text = "문자 왔어 문자 왔어";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
            } else { // API 20
                tts.speak(text, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }
    */

    public void playVIB() {
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(100);
    }

    public void Volume(int volume) {
        int End = 8;
        if (volume < 100) {
            if (volume < 10) {
                End = 6;
            } else {
                End = 7;
            }
        }
        textView_Volume.setText("볼륨: " + volume + "%");
        SpannableStringBuilder ssb = new SpannableStringBuilder(textView_Volume.getText().toString());
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#89FF00")), 4, End, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView_Volume.setText(ssb);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
        am.getStreamVolume(am.STREAM_MUSIC);
        am.setStreamVolume(am.STREAM_MUSIC, (volume*amStreamMusicMaxVol)/100,0 );
    }

    public void Switch() {
        if (!permissionGrantred()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } else {
            int End = 9;
            if (start) {
                setImage(R.drawable.tts_off);
                textView_Switch.setText("현재 상태: OFF");
                End = 10;
            } else {
                setImage(R.drawable.tts_on);
                textView_Switch.setText("현재 상태: ON");
            }
            start = !start;

            SpannableStringBuilder ssb = new SpannableStringBuilder(textView_Switch.getText().toString());
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#89FF00")), 7, End, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView_Switch.setText(ssb);

            serviceIntent = new Intent(this, BBBackgroundService.class);
            TTSserviceIntent = new Intent(this, BBTTS.class);

            if (start) {
                startService(serviceIntent);
                startService(TTSserviceIntent);
            } else {
                stopService(serviceIntent);
                stopService(TTSserviceIntent);
            }
        }
    }

    public void setImage(int resId) {
        imageView_Switch.setImageResource(resId);
    }

    @Override
    protected void onDestroy() { // 오류 방지 (tts 객체 삭제)
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }

        Log.d(TAG, "onDestory 호출됨");
    }


    @Override
    protected void onStop() {
        super.onStop();

        saveSF();

        Log.d(TAG, "onStop 호출됨");
    }

    public void saveSF() {
        SharedPreferences sf = getSharedPreferences(file, MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean("start", !start);
        editor.putInt("volume", volume);
        editor.putBoolean("first_start", firstStart);
        editor.apply();

        setCheckOptions(check);
    }

    private boolean permissionGrantred() {
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (sets != null && sets.contains(getPackageName())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getStart() {
        SharedPreferences sf = getSharedPreferences(file, MODE_PRIVATE);
        return sf.getBoolean("first_start", false);
    }

    public void setCheckOptions(ArrayList<String> values) {
        SharedPreferences sf = getSharedPreferences(file, MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            jsonArray.put(values.get(i));
        }

        if (values.isEmpty()) {
            editor.putString("check", null);
        } else {
            editor.putString("check", jsonArray.toString());
        }

        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<String> getCheckOptions() {
        SharedPreferences sf = getSharedPreferences(file, MODE_PRIVATE);
        ArrayList<String> returnList = new ArrayList<String>();
        String json = sf.getString("check", null);

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String returnValue = jsonArray.optString(i);
                    returnList.add(returnValue);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

}