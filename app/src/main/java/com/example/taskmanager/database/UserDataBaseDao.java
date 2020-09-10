package com.example.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.List;

@Dao
public interface UserDataBaseDao {

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User...users);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("select * from UserTable")
    List<User> getUsers();

    @Query("select * from UserTable where uuid=:uuid")
    User getUser(String uuid);

    @Query("delete from UserTable")
    void clear();

    @Query("select * from UserTable order by id desc limit 1")
    User lastUser();
}
