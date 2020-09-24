package com.example.taskmanager.model;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Entity(tableName = "TaskTable")
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long ID;
    @ColumnInfo(name = "uuid")
    private UUID taskID;
    @ColumnInfo(name = "name")
    private String taskName;
    @ColumnInfo(name = "description")
    private String taskDescription;
    @ColumnInfo(name = "state")
    private State taskState;
    @ColumnInfo(name = "date")
    private Date taskDate;
    @ColumnInfo(name = "initiator")
    private String taskInitiatorUserName;
    @ColumnInfo(name = "taskPicturePath")
    private String TaskPicturePath;

    public Task() {
        taskID = UUID.randomUUID();
    }

    public void setTaskInitiatorUserName(String taskInitiatorUserName) {
        this.taskInitiatorUserName = taskInitiatorUserName;
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

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTaskPicturePath() {
        return TaskPicturePath;
    }

    public void setTaskPicturePath(String taskPicturePath) {
        TaskPicturePath = taskPicturePath;
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


    public String getPhotoFileName() {
        return "IMG_" + getTaskID() + ".jpg";
    }

    public static class StateConverter {
        @TypeConverter()
        public String StateToString(State state) {
            return state.name();
        }

        @TypeConverter
        public State StringToState(String value) {
            switch (value) {
                case "TODO":
                    return State.TODO;
                case "DONE":
                    return State.DONE;
                default:
                    return State.DOING;
            }
        }
    }

    public static class UUIDConverter {
        @TypeConverter()
        public String UUIDToString(UUID uuid) {
            return uuid.toString();
        }

        @TypeConverter()
        public UUID StringToUUID(String value) {
            return UUID.fromString(value);
        }
    }

    public static class DateConverter {
        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return date.getTime();
            }
        }
    }

    public String generatePhotoFileName(){
        return "IMG"+getTaskID()+".jpg";
    }

    public String getTaskTextToShare() {
        return "Task name = " +
                taskName + ", Description=" +
                taskDescription + ", Date= " +
                taskDate + ", Status= " +
                taskState.name()
                ;
    }
}
