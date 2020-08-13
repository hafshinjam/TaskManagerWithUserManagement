package com.example.taskmanager.model;


import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {
    private UUID taskID;
    private String TaskName;
    private State TaskState;

    public Task() {
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
        TaskState = taskState;
    }
}
