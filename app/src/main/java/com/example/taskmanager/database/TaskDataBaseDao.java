package com.example.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.model.Task;

import java.util.List;

@Dao
public interface TaskDataBaseDao {

    @Insert
    void insertTask(Task task);

    @Insert
    void insertTasks(Task...tasks);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("select * from TaskTable")
    List<Task> getTasks();

    @Query("select * from TaskTable where uuid=:uuid")
    Task getTask(String uuid);

    @Query("delete from TaskTable")
    void clear();

    @Query("select * from TaskTable order by id desc limit 1")
    Task lastTask();
}
