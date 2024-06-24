package com.COMP900018.finalproject.model;

import com.COMP900018.finalproject.alarm.Alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class AlarmSetBean implements Serializable {
    private String name;
    private ArrayList<Task> tasks;
    private int hours;
    private int minus;
    private int id;
    private String music;

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    private LinkedList<DayOfWeek> days;

    public AlarmSetBean(String name,ArrayList<Task> tasks,int hours, int minus) {
        this.name = name;
        this.tasks = tasks;
        this.hours = hours;
        this.minus = minus;
        id = Alarm.getID();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }


    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinus() {
        return minus;
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }

    public LinkedList<DayOfWeek> getDays() {
        return days;
    }

    public void setDays(LinkedList<DayOfWeek> days) {
        this.days = days;
    }
}
