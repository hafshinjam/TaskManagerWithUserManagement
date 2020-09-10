package com.example.taskmanager.repository;


import android.content.Context;

import androidx.room.Room;

import com.example.taskmanager.database.UserDataBase;
import com.example.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

public class UserDBRepository implements IRepository<User> {
    private static UserDBRepository sUserDBRepository;

    private static Context mContext;

    private UserDataBase mDatabase;

    public static UserDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sUserDBRepository == null)
            sUserDBRepository = new UserDBRepository();
        return sUserDBRepository;
    }

    public UserDBRepository() {
        mDatabase = Room.databaseBuilder(mContext,
                UserDataBase.class,
                "UserDB.db").
                allowMainThreadQueries().build();
        //TODO add admin to users
/*        User admin = new User(UUID.randomUUID(), "admin", "12345");
        ContentValues values = getUserContentValues(admin);
        mDatabase.insert(UserDBSchema.UserTable.NAME, null, values);*/
    }

    @Override
    public List<User> getList() {
        return mDatabase.UserDao().getUsers();
    }

    /*   public UserCursorWrapper userQuery(String selection, String[] whereArgs) {
           Cursor cursor = mDatabase.query(UserDBSchema.UserTable.NAME,
                   null,
                   selection,
                   whereArgs,
                   null,
                   null,
                   null
           );
           return new UserCursorWrapper(cursor);

       }
   */
    @Override
    public User get(UUID uuid) {
        return mDatabase.UserDao().getUser(uuid.toString());
    }

    @Override
    public void update(User user) {
        mDatabase.UserDao().updateUser(user);
    }

    @Override
    public void delete(User user) {
        mDatabase.UserDao().deleteUser(user);

    }

    @Override
    public void insert(User user) {
    mDatabase.UserDao().insertUser(user);

    }

    @Override
    public void insertList(List<User> list) {
        User[] userArray =new User[list.size()];
        userArray =  list.toArray(userArray);
        mDatabase.UserDao().insertUsers(userArray);
    }

    @Override
    public int getPosition(UUID uuid) {
        List<User> userList = getList();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserID().equals(uuid))
                return i;
        }

        return -1;
    }

/*    public ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(COLS.UUID, user.getUserID().toString());
        values.put(COLS.USERNAME, user.getUserName());
        values.put(COLS.PASSWORD, user.getPassword());
        return values;
    }*/
}
