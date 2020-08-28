package com.example.taskmanager.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.database.UserBaseHelper;
import com.example.taskmanager.database.UserDBSchema;
import com.example.taskmanager.database.UserDBSchema.UserTable.COLS;
import com.example.taskmanager.database.cursorwrapper.UserCursorWrapper;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDBRepository implements IRepository<User> {
    private static UserDBRepository sUserDBRepository;

    private static Context mContext;

    private SQLiteDatabase mDatabase;

    public static UserDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sUserDBRepository == null)
            sUserDBRepository = new UserDBRepository();
        return sUserDBRepository;
    }

    public UserDBRepository() {
        UserBaseHelper userBaseHelper = new UserBaseHelper(mContext);
        mDatabase = userBaseHelper.getWritableDatabase();
        User admin = new User(UUID.randomUUID(),"admin","12345");
        ContentValues values = getUserContentValues(admin);
        mDatabase.insert(UserDBSchema.UserTable.NAME,null,values);
    }

    @Override
    public List<User> getList() {
        List<User> users = new ArrayList<>();
        UserCursorWrapper cursor = userQuery(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                users.add(cursor.getUser());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return users;
    }

    public UserCursorWrapper userQuery(String selection, String[] whereArgs) {
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

    @Override
    public User get(UUID uuid) {
        String selection = UserDBSchema.UserTable.COLS.UUID + "=?";
        String[] whereArgs = new String[]{uuid.toString()
        };
        UserCursorWrapper cursor = userQuery(selection, whereArgs);
        try {
            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void update(User user) {
        ContentValues values = getUserContentValues(user);
        String where = COLS.UUID + "=?";
        String[] whereArgs = new String[]{user.getID().toString()};
        mDatabase.update(UserDBSchema.UserTable.NAME, values, where, whereArgs);
    }

    @Override
    public void delete(User user) {
        String where = COLS.UUID + "=?";
        String[] whereArgs = new String[]{user.getID().toString()};
        mDatabase.delete(UserDBSchema.UserTable.NAME, where, whereArgs);

    }

    @Override
    public void insert(User user) {
        ContentValues values = getUserContentValues(user);
        mDatabase.insert(UserDBSchema.UserTable.NAME,null, values);

    }

    @Override
    public void insertList(List<User> list) {
    }

    @Override
    public int getPosition(UUID uuid) {
        List<User> userList = getList();
        for (int i = 0; i <userList.size() ; i++) {
            if (userList.get(i).getID().equals(uuid))
                return i;
        }

        return -1;
    }

    public ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(COLS.UUID, user.getID().toString());
        values.put(COLS.USERNAME, user.getUserName());
        values.put(COLS.PASSWORD, user.getPassword());
        return values;
    }
}
