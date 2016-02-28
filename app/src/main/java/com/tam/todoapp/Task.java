package com.tam.todoapp;

/**
 * Created by toan on 2/28/2016.
 */
public class Task {

    private int id;
    private String taskName;
    private String status;
    public static final String COL_ID = "id";
    public static final String COL_TASKNAME = "taskName";
    public static final String COL_STATUS = "status";
    public static final String ARRAY_STATUS[] = { "High", "Medium", "Low", "Done" };
    public Task() {
        this.status = "Low";
        this.taskName = "";
    }

    public Task(String taskName, String status) {
        this.taskName = taskName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
