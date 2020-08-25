package com.example.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskmanager.database.TaskDBSchema.TaskTable.*;

import androidx.annotation.Nullable;


public class TaskBaseHelper extends SQLiteOpenHelper {
    public TaskBaseHelper(@Nullable Context context) {
        super(context, TaskDBSchema.NAME, null, TaskDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TaskDBSchema.TaskTable.NAME + "(" +
                COLS.ID + " integer primary key autoIncrement," +
                COLS.TASK_ID + " text," +
                COLS.TASK_NAME + " text," +
                COLS.TASK_DESCRIPTION + " text," +
                COLS.TASK_STATE + " text," +
                COLS.TASK_DATE + " long," +
                COLS.TASK_INITIATOR + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
