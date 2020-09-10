package com.example.taskmanager.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taskmanager.model.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Task.DateConverter.class, Task.StateConverter.class, Task.UUIDConverter.class})
public abstract class TaskDataBase extends RoomDatabase {
    public abstract TaskDataBaseDao TaskDAO();
}
