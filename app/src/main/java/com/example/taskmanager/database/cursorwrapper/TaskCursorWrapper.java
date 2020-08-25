package com.example.taskmanager.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.database.TaskDBSchema.TaskTable.COLS;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(COLS.TASK_ID));
        String taskName = getString(getColumnIndex(COLS.TASK_NAME));
        String taskDescription = getString(getColumnIndex(COLS.TASK_DESCRIPTION));
        String taskState = getString(getColumnIndex(COLS.TASK_STATE));
        Date taskDate = new Date(getLong(getColumnIndex(COLS.TASK_DATE)));
        String taskInitiator = getString(getColumnIndex(COLS.TASK_INITIATOR));
        State state;
        switch (taskState){
            case "TODO":
                state=State.TODO;
                break;
            case "DONE":
                state= State.DONE;
                break;
            default:
                state=State.DOING;
        }
        return new Task(UUID.fromString(uuidString),taskName,taskDescription, state, taskDate,taskInitiator);
    }
}
