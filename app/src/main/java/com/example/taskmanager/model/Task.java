package com.example.taskmanager.model;


import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {
    private UUID taskID;
    private String TaskName;
    private State TaskState;

    public Task(){
        taskID = UUID.randomUUID();
    }
    public Task(String taskName, State taskState) {
        this();
        TaskName = taskName;
        TaskState = taskState;
    }

    public UUID getTaskID() {
        return taskID;
    }

    public void setTaskID(UUID taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public State getTaskState() {
        return TaskState;
    }

    public void setTaskState(State taskState) {
        TaskState = taskState;
    }
}
