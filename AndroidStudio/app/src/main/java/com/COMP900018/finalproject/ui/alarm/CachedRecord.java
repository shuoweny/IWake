package com.COMP900018.finalproject.ui.alarm;

import android.content.Context;
import android.content.Intent;


import com.COMP900018.finalproject.MainActivity;
import com.COMP900018.finalproject.alarm.Alarm;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.model.Task;
import com.COMP900018.finalproject.model.TaskType;
import com.COMP900018.finalproject.ui.camera.CameraPage;
import com.COMP900018.finalproject.ui.light.LightActivity;
import com.COMP900018.finalproject.ui.shake.ShakeOrangeActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class CachedRecord {
    public static boolean isNew = true;
    public static AlarmSetBean alarmsRecord;
    public static ArrayList<Task> remainingTasks = new ArrayList<>();
    public static int mark;
    public static int total;
    public static LinkedList<Date> wakeup_times = new LinkedList<>();
    public static boolean commit = false;

    public static void addWakeupTime(Date time){
        if(wakeup_times.size()>=10){
            wakeup_times.removeFirst();
        }
        wakeup_times.add(time);
    }

    public static String getAverageTimes(){
        //return "07:42";
        if(wakeup_times.size() == 0){
            return "--:--";
        }
        long totalMillis = 0;
        for (Date time : wakeup_times) {
            totalMillis += time.getTime();
        }
        long averageMillis = totalMillis / wakeup_times.size();
        Date averageTime = new Date(averageMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(averageTime);
    }
    public static void initialiseRecord(AlarmSetBean alarm){
        alarmsRecord = alarm;
        remainingTasks = alarm.getTasks();
        commit = false;
        mark = 0;
    }
    public static void clear(){
        alarmsRecord = null;
        commit = false;
        remainingTasks = new ArrayList<>();
        mark = 0 ;
        total = 0;
    }

    public static void doneTheWork(TaskType task){
        for (int i =0;i<remainingTasks.size();i++){
            total+=remainingTasks.get(i).getPoint();
            if(task.equals(remainingTasks.get(i).getType())){
                mark += remainingTasks.get(i).getPoint();
                remainingTasks.remove(i);
                break;
            }
        }
    }

    public static Task getTask(){

        if (remainingTasks.size() == 0){
            commit = false;
            return null;
        }
        return remainingTasks.get(0);
    }


    public static void turnToPage(Context context){
        Intent intent;
        Task task = getTask();

        if (alarmsRecord==null){
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }else if(task==null){
            Alarm.cancelAlarm(context,alarmsRecord.getId());
            intent = new Intent(context, AlarmSuccessActivity.class);
        }else if(!commit){
            intent = new Intent(context, AlarmSetOffActivity.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(alarmsRecord);
            intent.putExtra("alarm_config",json);
        }else{
            switch (task.getType()){
                case LIGHTTASK: intent = new Intent(context, LightActivity.class);break;
                case MOTIONTASK: intent = new Intent(context, ShakeOrangeActivity.class);break;
                case CAMERATASK: intent =  new Intent(context, CameraPage.class);break;
                default:intent = new Intent(context,MainActivity.class);break;
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void delay(Context context,AlarmSetBean alarm){
        Alarm.cancelAlarm(context,alarm.getId());
        clear();
        Alarm.setAlarm(context,1000*60*5,alarm);
        mark -= 5;

    }






}
