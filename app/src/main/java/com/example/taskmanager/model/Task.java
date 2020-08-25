package com.example.taskmanager.model;


import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class Task implements Serializable {
    private UUID taskID;
    private String taskName;
    private String taskDescription;
    private State taskState;
    private Date taskDate;
    private String taskInitiatorUserName;

    public Task() {
        taskID = UUID.randomUUID();
    }

    /**
     * for new tasks use this constructor (if you have a task already made and want to use those
     * values use the one with UUID)
     *
     * @param taskName
     * @param taskDescription
     * @param taskState
     * @param taskDate
     * @param taskInitiator
     */
    public Task(String taskName, String taskDescription, State taskState, Date taskDate, User taskInitiator) {
        this();
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskState = taskState;
        this.taskDate = taskDate;
        this.taskInitiatorUserName = taskInitiator.getUserName();
    }

    public Task(UUID uuid, String taskName, String taskDescription, State taskState, Date taskDate, String taskInitiator) {
        this.taskID = uuid;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskState = taskState;
        this.taskDate = taskDate;
        this.taskInitiatorUserName = taskInitiator;
    }

    public String getTaskInitiatorUserName() {
        return taskInitiatorUserName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        String currentDate = formatter.format(Date.parse(taskDate.toString()));
        return currentDate;
    }

    public String getTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:SS");
        String currentTime = formatter.format(Date.parse(taskDate.toString()));
        return currentTime;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public UUID getTaskID() {
        return taskID;
    }

    public void setTaskID(UUID taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public State getTaskState() {
        return taskState;
    }

    @Override
    public int hashCode() {
        return this.taskID.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Task task = (Task) obj;
        return (this.taskID.equals(task.taskID));
    }

    public void setTaskState(State taskState) {
        this.taskState = taskState;
    }
}
