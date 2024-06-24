package com.COMP900018.finalproject.model;

import java.io.Serializable;

public class Task implements Serializable {
    private TaskType type;
    private String description;
    private int point;

    public Task(TaskType type, String description, int point) {
        this.type = type;
        this.description = description;
        this.point = point;
    }

    public TaskType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getPoint() {
        return point;
    }


}
