/**
 * AlarmReceiver is a BroadcastReceiver that is invoked when an alarm is triggered.
 * Upon receiving an alarm, it starts the AlarmSoundService to play an alarm sound.
 *
 * Note: This BroadcastReceiver needs to be registered in the AndroidManifest.xml
 * with the appropriate intent-filter to correctly respond to the alarm.
 *
 * @author Zhicheng Lin
 * @version 1
 */
package com.COMP900018.finalproject.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.ui.alarm.AlarmSetOffActivity;
import com.COMP900018.finalproject.ui.alarm.CachedRecord;
import com.google.gson.Gson;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, AlarmSoundService.class);
        Intent vibrationIntent = new Intent(context, AlarmVibrationService.class);
        Intent dialogIntent = new Intent(context, AlarmSetOffActivity.class);
        String alarmConfigJson = intent.getStringExtra("alarm_config");
        if (alarmConfigJson != null) {
            // reset the alarm for next time
            Gson gson1 = new Gson();
            AlarmSetBean alarm = gson1.fromJson(alarmConfigJson, AlarmSetBean.class);

            if (CachedRecord.alarmsRecord == null) {
                CachedRecord.initialiseRecord(alarm);
            }
            Alarm.cancelAlarm(context, alarm.getId());
            Alarm.scheduleNextAlarm(context, alarm);
            dialogIntent.putExtra("alarm_config", alarmConfigJson);
            serviceIntent.putExtra("music", alarm.getMusic());
        } else if (intent.getStringExtra("music") != null) {
            serviceIntent.putExtra("music", intent.getStringExtra("music"));
        }

        context.startForegroundService(vibrationIntent);
        context.startForegroundService(serviceIntent);
        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogIntent);
        createNotification(context,dialogIntent);

    }

    private void createNotification(Context context, Intent notifyIntent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Channel";
            String description = "Alarm Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("505", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "505")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("IWake Alarm!")
                .setContentText("Your alarm is ringing!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE
        );

        builder.setContentIntent(notifyPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = 505;
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
}
