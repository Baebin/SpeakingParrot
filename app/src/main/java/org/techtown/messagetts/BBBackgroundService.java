package org.techtown.messagetts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Locale;

public class BBBackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    public static Context context_background;

    @Override
    public void onCreate() {
        super.onCreate();

        context_background = this;

        startForegroundService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startForegroundService() {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
//                .bigText("· 열심히 일하는 중")
                .setBigContentTitle("· 열심히 일하는 중");

        Bitmap LargeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(R.mipmap.ic_main)
                        .setLargeIcon(LargeIcon)
                        .setContentTitle("· 열심히 일하는 중")
                        .setOngoing(true)
                        .setWhen(0)
                        .setShowWhen(false)
                        .setStyle(style);
//                        .setContentText("· 열심히 일하는 중")

        Intent notiIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 체크
            mNotificationManager.createNotificationChannel(new NotificationChannel("1", "포그라운드 서비스", NotificationManager.IMPORTANCE_NONE));
        }

        Notification notification = builder.build();
        startForeground(1, builder.build());
    }
}
