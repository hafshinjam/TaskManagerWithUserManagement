package com.example.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "UserTable")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long ID;
    @ColumnInfo(name = "uuid")
    private UUID mUserID;
    @ColumnInfo(name = "userName")
    private String mUserName;
    @ColumnInfo(name = "password")
    private String mPassword;
    @ColumnInfo(name = "regDate")
    private Date mRegDate;

    public Date getRegDate() {
        return mRegDate;
    }

    public void setRegDate(Date regDate) {
        mRegDate = regDate;
    }

    public User() {
        mUserID = UUID.randomUUID();
    }

    public void setUserID(UUID userID) {
        mUserID = userID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public UUID getUserID() {
        return mUserID;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public User(UUID id, String userName, String password) {
        mUserID = UUID.randomUUID();
        mUserName = userName;
        mPassword = password;
        mRegDate= new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return mUserName.equals(user.mUserName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUserName);
    }
}
