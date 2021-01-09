package org.techtown.messagetts;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotiReceiver extends NotificationListenerService {
    private static final String TAG = "Noti Receiver";
    //TextToSpeech tts;

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        Log.d(TAG, "onNotificationRemoved ~ " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        Notification notification = sbn.getNotification();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        //Icon smallIcon = notification.getSmallIcon();
        //Icon largeIcon = notification.getLargeIcon();

        Log.d(TAG, "onNotificationPosted ~ " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId() +
                " postTime: " + sbn.getPostTime() +
                " title: " + title +
                " text : " + text +
                " subText: " + subText);

        /*
        if (sbn.getPackageName() != "com.kakao.talk") {
            if (sbn.getPackageName().contains("com.samsung.android.messaging")) {
                if (text != null && text != "메세지 보기" && !title.contains("메시지") && subText != null) {
                    Intent intent = new Intent(this, SmsReceiver.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("sender", title);
                    intent.putExtra("contents", text);

                    playSound(intent);

                    ((MainActivity) MainActivity.context_main).showToast("NotiReceiver 수신 성공 : " + sbn.getPackageName() +
                            "\n" + "발신 타이틀 :" + title +
                            "\n" + "발신 정보 :" + subText +
                            "\n" + "내용 : " + text);
                } else {

                    ((MainActivity) MainActivity.context_main).showToast("Noti 수신 실패 : " + sbn.getPackageName() +
                            "\n" + "발신 타이틀 :" + title +
                            "\n" + "발신 정보 :" + subText +
                            "\n" + "내용 : " + text);
                }
            } else {

                ((MainActivity) MainActivity.context_main).showToast("Noti 수신 : " + sbn.getPackageName() +
                        "\n" + "발신 타이틀 :" + title +
                        "\n" + "발신 정보 :" + subText +
                        "\n" + "내용 : " + text);
            }
        }
        */


        if (MainActivity.check.contains(sbn.getPackageName())) {
            if (title != null && !title.contains("메시지") && text != null && text != "메세지 보기") {
                Intent intent = new Intent(this, NotiReceiver.class);

                intent.putExtra("sender", title);
                intent.putExtra("contents", text);

                playSound(intent);

                ((MainActivity) MainActivity.context_main).showToast("Noti 수신 성공 : " + sbn.getPackageName() +
                        "\n" + "발신 타이틀 :" + title +
                        "\n" + "발신 정보 :" + subText +
                        "\n" + "내용 : " + text);
            } else {
                ((MainActivity) MainActivity.context_main).showToast("Noti 수신 실패 : " + sbn.getPackageName() +
                        "\n" + "발신 타이틀 :" + title +
                        "\n" + "발신 정보 :" + subText +
                        "\n" + "내용 : " + text);
            }
        }
    }

    public void playSound(Intent intent) {
        if (intent != null) {
            if (((MainActivity) MainActivity.context_main).start == true){
                //tts = ((MainActivity) MainActivity.context_main).tts;

                String sender = intent.getStringExtra("sender");
                String contents = intent.getStringExtra("contents");
                Log.d("playSound", "sender: " + sender + ", contents: " + contents);
                Log.d(TAG, "playSound 호출됨, " + "sender: " + sender + ", contents: " + contents);

                String text = sender + "님으로부터 온 메시지입니다 " + contents;
                //String text = "문자 왔어 문자 왔어";

                BBTTS.BBtts.speak(text);
            }
        }
    }
}
