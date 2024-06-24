package com.COMP900018.finalproject.localStorage;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.COMP900018.finalproject.alarm.Alarm;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.model.DayOfWeek;
import com.COMP900018.finalproject.model.Recommendation;
import com.COMP900018.finalproject.model.Task;
import com.COMP900018.finalproject.model.TaskType;
import com.COMP900018.finalproject.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
public class DataIO {


    public static ArrayList<AlarmSetBean> getAlarms(Context context){
        ArrayList<AlarmSetBean> alarms = new ArrayList<>();
        String alarms_json = DataIO.readJSON(context,"alarms.json");
        Gson gson1 = new Gson();
        Type alarmListType = new TypeToken<List<AlarmSetBean>>(){}.getType();
        alarms = gson1.fromJson(alarms_json, alarmListType);

        if (alarms == null){
            alarms = new ArrayList<>();
        }
        for(AlarmSetBean alarmSetBean: alarms){
            Alarm.cancelAlarm(context,alarmSetBean.getId());
            Alarm.scheduleNextAlarm(context,alarmSetBean);
        }
        return alarms;
    }

    public static ArrayList<Recommendation> getRecommendation(Context context){
        ArrayList<Recommendation> recommendations;
        Gson gson = new Gson();
        Type taskListType = new TypeToken<List<Recommendation>>(){}.getType();
        recommendations = gson.fromJson(DataIO.readJSON(
                context,"recommendations.json"), taskListType);
        if(recommendations == null){
            recommendations = new ArrayList<>();
            ArrayList<Task> tasks = getTasks(context);
            AlarmSetBean alarmSetBean = new AlarmSetBean("Sunrise Serenity",
                    tasks,6,00);
            LinkedList<DayOfWeek> everyday = getComboDays(1);
            alarmSetBean.setDays(everyday);
            alarmSetBean.setMusic("alarm1");
            alarmSetBean.setId(1);
            Recommendation recommendation1 = new Recommendation("Sunrise Serenity",
                    "Rise and shine every morning as you awaken to the gentle light of dawn, creating a serene start to your day.",alarmSetBean);

            AlarmSetBean alarmSetBean2 = new AlarmSetBean("Morning Zen Master",
                    tasks,7,30);
            LinkedList<DayOfWeek> weekdays = getComboDays(2);
            alarmSetBean2.setDays(weekdays);
            alarmSetBean2.setMusic("alarm2");
            alarmSetBean2.setId(2);
            Recommendation recommendation2 = new Recommendation("Morning Zen Master",
                    "Achieve inner peace and focus with guided meditation exercises," +
                            "setting the tone for a harmonious workweek.",alarmSetBean2);

            AlarmSetBean alarmSetBean3 = new AlarmSetBean("Energize Express",
                    tasks,5,45);
            LinkedList<DayOfWeek> fewDays = getComboDays(3);
            alarmSetBean3.setDays(fewDays);
            alarmSetBean3.setId(3);
            alarmSetBean3.setMusic("alarm3");
            Recommendation recommendation3 = new Recommendation("Energize Express",
                    "Jumpstart your day with high-intensity workouts on selected weekdays.",alarmSetBean3);

            recommendations.add(recommendation1);
            recommendations.add(recommendation2);
            recommendations.add(recommendation3);
            //recommendations.add(recommendation4);

            gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonTasks = gson.toJson(recommendations);
            DataIO.saveJSON(context,"recommendations.json",jsonTasks);
        }

        return  recommendations;
    }
    private static LinkedList<DayOfWeek> getComboDays(int code){
        LinkedList<DayOfWeek> days = new LinkedList<>();
        if(code == 1){
            days.add(DayOfWeek.Monday);
            days.add(DayOfWeek.Tuesday);
            days.add(DayOfWeek.Wednesday);
            days.add(DayOfWeek.Thursday);
            days.add(DayOfWeek.Friday);
            days.add(DayOfWeek.Saturday);
            days.add(DayOfWeek.Sunday);
        }else if(code == 2){
            days.add(DayOfWeek.Monday);
            days.add(DayOfWeek.Tuesday);
            days.add(DayOfWeek.Wednesday);
            days.add(DayOfWeek.Thursday);
            days.add(DayOfWeek.Friday);
        }else if(code == 3){
        days.add(DayOfWeek.Monday);
        days.add(DayOfWeek.Wednesday);
        days.add(DayOfWeek.Friday);
    } else{
            days.add(DayOfWeek.Friday);
            days.add(DayOfWeek.Saturday);
            days.add(DayOfWeek.Saturday);
        }
        return days;
    }
    public static ArrayList<Task> getTasks(Context context){
        ArrayList<Task> tasks;
        Gson gson = new Gson();
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        tasks = gson.fromJson(DataIO.readJSON(
                context,"tasks.json"), taskListType);
        if (tasks == null) {
            tasks = new ArrayList<>();
            tasks.add(new Task(TaskType.LIGHTTASK,
                    "Open Your blinds",10));
            tasks.add(new Task(TaskType.MOTIONTASK,
                    "Shake it up!",10));
            tasks.add(new Task(TaskType.CAMERATASK,
                    "Take your best selfie",15));
            gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonTasks = gson.toJson(tasks);
            DataIO.saveJSON(context,"tasks.json",jsonTasks);
        }
        return tasks;
    }


    public static String readJSON(Context context, String filename){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line=reader.readLine()) != null){
                content.append(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public static void saveJSON(Context context,String filename,String json){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(json);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public static List<File> getAllImageFiles() {
        try{
            String folderName = "com.COMP900018.finalproject";
            String directoryPath = Environment.getExternalStorageDirectory() +
                    "/Android/data/" + folderName + "/files/pictures/";

            List<File> imageFiles = new ArrayList<>();
            File directory = new File(directoryPath);
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {

                        if (isImageFile(file)) {
                            imageFiles.add(file);
                        }
                    }
                }
            }
            return imageFiles;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ArrayList<>();

    }

    private static boolean isImageFile(File file) {
        // 获取文件扩展名并将其转换为小写
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 判断文件扩展名是否为常见的图片格式，如 jpg、png、gif 等
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") ||
                fileExtension.equals("png") || fileExtension.equals("gif") ||
                fileExtension.equals("bmp");
    }


}
