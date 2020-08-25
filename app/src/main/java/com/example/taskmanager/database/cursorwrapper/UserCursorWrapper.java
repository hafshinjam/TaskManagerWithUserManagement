package com.example.taskmanager.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.database.UserDBSchema;
import com.example.taskmanager.model.User;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String stringUUID = getString(getColumnIndex(UserDBSchema.UserTable.COLS.UUID));
        String userName = getString(getColumnIndex(UserDBSchema.UserTable.COLS.USERNAME));
        String password = getString(getColumnIndex(UserDBSchema.UserTable.COLS.PASSWORD));
        return new User(UUID.fromString(stringUUID), userName, password);
    }
}
