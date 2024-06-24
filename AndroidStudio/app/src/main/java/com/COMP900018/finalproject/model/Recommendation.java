package com.COMP900018.finalproject.model;

public class Recommendation {
    private String title;
    private String description;
    private AlarmSetBean alarmSetBean;

    public Recommendation(String title, String description, AlarmSetBean alarmSetBean){
        this.title = title;
        this.description = description;
        this.alarmSetBean = alarmSetBean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlarmSetBean getAlarmSetBean() {
        return alarmSetBean;
    }

    public void setAlarmSetBean(AlarmSetBean alarmSetBean) {
        this.alarmSetBean = alarmSetBean;
    }
}
