/**
 * AlarmSoundService is a service that plays an alarm sound using MediaPlayer.
 * When started, this service plays a looping alarm sound. The sound will keep playing
 * until the service is explicitly stopped.
 *
 * @author Zhicheng Lin
 * @version 1
 */


package com.COMP900018.finalproject.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.COMP900018.finalproject.R;

public class AlarmSoundService extends Service {

    private static final int NOTIFICATION_ID = 503;
    private static final String CHANNEL_ID = "alarm_music_channel";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        // 1. 创建通知渠道
        createNotificationChannel();

        // 2. 创建通知
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm music")
                .setContentText("Alarm music is play")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm music Channel";
            String description = "Alarm music Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        String music = intent.getStringExtra("music");

        mediaPlayer = MediaPlayer.create(this, getMusicId(music));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return START_STICKY;
    }
    private int getMusicId(String music){
        if(music.toLowerCase().equals("alarm2")){
            return R.raw.alarm2;
        }else if(music.toLowerCase().equals("alarm3")){
            return R.raw.alarm3;
        }else if(music.toLowerCase().equals("alarm4")){
            return R.raw.alarm4;
        }else if(music.toLowerCase().equals("alarm5")){
            return R.raw.alarm5;
        }else if(music.toLowerCase().equals("alarm6")){
            return R.raw.alarm6;
        }else{
            return R.raw.alarm1;
        }
    }
    @Override
    public void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
