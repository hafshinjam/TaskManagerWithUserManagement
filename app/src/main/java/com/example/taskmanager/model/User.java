package com.example.taskmanager.model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private UUID mID;
    private String mUserName;
    private String mPassword;

    public UUID getID() {
        return mID;
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

    public User(UUID id, String userName , String password){
        mID = UUID.randomUUID();
        mUserName=userName;
        mPassword=password;
    }
}
