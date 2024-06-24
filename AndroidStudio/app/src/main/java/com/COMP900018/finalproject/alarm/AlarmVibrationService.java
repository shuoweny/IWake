package com.COMP900018.finalproject.alarm;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.COMP900018.finalproject.R;

public class AlarmVibrationService extends Service {
    private Vibrator vibrator;
    private static final int NOTIFICATION_ID = 503;
    private static final String CHANNEL_ID = "alarm_vibrator_channel";
    @Override
    public void onCreate() {
        super.onCreate();

        // 1. 创建通知渠道
        createNotificationChannel();

        // 2. 创建通知
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm Vibrator")
                .setContentText("Alarm Vibrator")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm vibrator Channel";
            String description = "Alarm vibrator Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long[] pattern = new long[]{500, 1000, 1000, 2000, 2000};
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            if (vibrator != null) {
                vibrator.vibrate(pattern, 0);

            }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // 停止震动
        if (vibrator != null) {
            vibrator.cancel();
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
