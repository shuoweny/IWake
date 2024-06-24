/**
 * Alarm utility class to manage setting and cancelling alarms.
 * This class provides static methods to set exact alarms that
 * wake up the device and trigger the AlarmReceiver broadcast receiver.
 * It also provides methods to cancel any active alarms.
 *
 * @author ZHicheng Lin
 * @version 1
 */
package com.COMP900018.finalproject.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.model.DayOfWeek;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Alarm {

    private static int id = 4;

    /**
     * Sets an exact alarm to trigger the AlarmReceiver broadcast receiver.
     *
     * @param context         The application context used to access system services.
     * @param triggerAtMillis The time interval in milliseconds after which the alarm should be triggered.
     */

    public static void setAlarm(Context context, long triggerAtMillis, AlarmSetBean alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String alarmJSON = gson.toJson(alarm);
        intent.putExtra("alarm_config",alarmJSON);
        int requestCode = alarm.getId();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Set the exact alarm to wake up the device

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + triggerAtMillis, pendingIntent);
    }

    /**
     * Cancels any active alarms and stops the AlarmSoundService.
     *
     * @param context The application context used to access system services.
     */
    public static void cancelAlarm(Context context, int requestCode){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        pendingIntent.cancel();
        Intent serviceIntent = new Intent(context, AlarmSoundService.class);
        context.stopService(serviceIntent);
        Intent vibrationIntent = new Intent(context, AlarmVibrationService.class);
        context.stopService(vibrationIntent);
    }
    public static synchronized int getID(){
        return id++;
    }

    public static void scheduleNextAlarm(Context context, AlarmSetBean alarm) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        // 创建一个映射，将星期几映射到 Calendar 中的常量
        Map<String, Integer> dayMap = new HashMap<>();
        dayMap.put("Sunday", Calendar.SUNDAY);
        dayMap.put("Monday", Calendar.MONDAY);
        dayMap.put("Tuesday", Calendar.TUESDAY);
        dayMap.put("Wednesday", Calendar.WEDNESDAY);
        dayMap.put("Thursday", Calendar.THURSDAY);
        dayMap.put("Friday", Calendar.FRIDAY);
        dayMap.put("Saturday", Calendar.SATURDAY);

        // 找出最近的一天
        int today = now.get(Calendar.DAY_OF_WEEK);
        int closestDayDifference = 7; // 最大差值不会超过7天
        for (DayOfWeek day : alarm.getDays()) {
            int alarmDay = dayMap.get(day.toString());
            int difference = alarmDay - today;
            if (difference < 0) {
                difference += 7;
            }
            if (difference < closestDayDifference) {
                closestDayDifference = difference;
            }
        }

        Calendar nextAlarmTime = (Calendar) now.clone();
        nextAlarmTime.add(Calendar.DATE, closestDayDifference);
        nextAlarmTime.set(Calendar.HOUR_OF_DAY, alarm.getHours());
        nextAlarmTime.set(Calendar.MINUTE, alarm.getMinus());
        nextAlarmTime.set(Calendar.SECOND, 0);
        nextAlarmTime.set(Calendar.MILLISECOND, 0);


        if (nextAlarmTime.before(now)) {
            nextAlarmTime.add(Calendar.DATE, 7);
        }

        // 计算触发时间
        long triggerAtMillis = nextAlarmTime.getTimeInMillis() - System.currentTimeMillis();

        // 设置闹钟
        setAlarm(context, triggerAtMillis, alarm);
    }

}
