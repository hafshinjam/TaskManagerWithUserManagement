package com.example.taskmanager.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

@Database(entities = {User.class},version = 1,exportSchema = false)
@TypeConverters(Task.UUIDConverter.class)
public abstract class UserDataBase extends RoomDatabase {
    public abstract UserDataBaseDao UserDao();

}
