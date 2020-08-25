package com.example.taskmanager.repository;


import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository implements IRepository<User> {
    private static UserRepository sUserRepository;

    public static UserRepository getInstance() {

        if (sUserRepository == null)
            sUserRepository = new UserRepository();
        return sUserRepository;
    }

    private List<User> mUsers;

    public UserRepository() {
        mUsers = new ArrayList<>();
        User admin = new User(UUID.randomUUID(),"admin","12345");
        mUsers.add(admin);
    }

    @Override
    public List<User> getList() {
        return mUsers;
    }

    @Override
    public User get(UUID uuid) {
        for (User user : mUsers) {
            if (user.getID().equals(uuid))
                return user;
        }
        return null;
    }


    public void setList(List<User> users) {
        mUsers = users;
    }

    @Override
    public void update(User user) {
        mUsers.set(mUsers.indexOf(user), user);
        setList(mUsers);
    }

    @Override
    public void delete(User user) {
        for (int i = 0; i < mUsers.size(); i++) {
            if (mUsers.get(i).getID().equals(user.getID())) {
                mUsers.remove(i);
                return;
            }
        }
    }

    @Override
    public void insert(User user) {
        mUsers.add(user);
    }

    @Override
    public void insertList(List<User> users) {
        mUsers.addAll(users);
    }

    @Override
    public int getPosition(UUID uuid) {
        List<User> userList = getList();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getID().equals(uuid))
                return i;
        }

        return -1;
    }

}
