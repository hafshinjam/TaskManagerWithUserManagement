package com.example.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.database.TaskBaseHelper;
import com.example.taskmanager.database.TaskDBSchema;
import com.example.taskmanager.database.cursorwrapper.TaskCursorWrapper;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.taskmanager.database.TaskDBSchema.TaskTable.NAME;

public class TaskDBRepository implements IRepository<Task> {
    //memory leak -handle later
    private static TaskDBRepository sTaskRepository;
    private static Context mContext;

    private SQLiteDatabase mDatabase;

    public static TaskDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sTaskRepository == null)
            sTaskRepository = new TaskDBRepository();
        return sTaskRepository;
    }

    public TaskDBRepository() {
        TaskBaseHelper taskBaseHelper = new TaskBaseHelper(mContext);
        mDatabase = taskBaseHelper.getWritableDatabase();

    }

    public List<Task> searchByName(String name) {
        List<Task> taskList = new ArrayList<>();
        for (Task tempTask : getList()) {
            if (tempTask.getTaskName().contains(name))
                taskList.add(tempTask);
        }
        return taskList;
    }

    public List<Task> searchByDescription(String description) {
        List<Task> taskList = new ArrayList<>();
        for (Task tempTask : getList()) {
            if (tempTask.getTaskDescription().contains(description))
                taskList.add(tempTask);
        }
        return taskList;
    }

    public List<Task> searchByDate(String description) {
        List<Task> taskList = new ArrayList<>();
        for (Task tempTask : getList()) {
            if (tempTask.getTaskDescription().contains(description))
                taskList.add(tempTask);
        }
        return taskList;
    }

    public List<Task> searchByHour(String description) {
        List<Task> taskList = new ArrayList<>();
        for (Task tempTask : getList()) {
            if (tempTask.getTaskDescription().contains(description))
                taskList.add(tempTask);
        }
        return taskList;
    }

    @Override
    public List<Task> getList() {
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = mDatabase.query(NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        try {
            TaskCursorWrapper cursorWrapper = new TaskCursorWrapper(cursor);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                taskList.add(cursorWrapper.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return taskList;
    }

    public List<Task> getUserTasks(User user) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : getList()) {
            if (task.getTaskInitiatorUserName().equals(user.getUserName())) {
                tasks.add(task);
            }
        }
        return tasks;
    }


    public List<Task> getStateList(State state) {
        List<Task> todoList = new ArrayList<>();
        for (Task task : getList()) {
            if (task.getTaskState() == state)
                todoList.add(task);
        }
        return todoList;
    }

    /**
     * user this method to get list of task with this:
     *
     * @param state and for this :
     * @param user  user
     * @return it returns the lst of tasks with the state passed here
     */
    public List<Task> getStateList(State state, User user) {
        List<Task> taskList = new ArrayList<>();
        for (Task task : getList()) {
            if (task.getTaskState() == state && task.getTaskInitiatorUserName().equals(user.getUserName()))
                taskList.add(task);
        }
        return taskList;
    }

    @Override
    public Task get(UUID uuid) {
        String selection = TaskDBSchema.TaskTable.COLS.TASK_ID + "=?";
        String[] whereArgs = new String[]{uuid.toString()};
        Cursor cursor = mDatabase.query(NAME, null,
                selection, whereArgs,
                null,
                null,
                null);
        TaskCursorWrapper cursorWrapper = new TaskCursorWrapper(cursor);
        try {
            cursorWrapper.moveToFirst();
            return cursorWrapper.getTask();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void update(Task task) {
        ContentValues values = getTaskContentValue(task);
        String[] whereArgs = new String[]{task.getTaskID().toString()};
        mDatabase.update(NAME, values, TaskDBSchema.TaskTable.COLS.TASK_ID + "=?", whereArgs);
    }

    @Override
    public void delete(Task task) {
        String[] whereARGS = new String[]{task.getTaskID().toString()};
        String WhereClause = TaskDBSchema.TaskTable.COLS.TASK_ID + "=?";
        mDatabase.delete(NAME, WhereClause, whereARGS);
    }

    @Override
    public void insert(Task task) {
        ContentValues values = getTaskContentValue(task);
        mDatabase.insert(NAME, null, values);
    }

    @Override
    public void insertList(List<Task> list) {

    }

    @Override
    public int getPosition(UUID uuid) {
        List<Task> taskList = getList();
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getTaskID().equals(uuid))
                return i;
        }
        return -1;
    }

    private ContentValues getTaskContentValue(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDBSchema.TaskTable.COLS.TASK_ID, task.getTaskID().toString());
        values.put(TaskDBSchema.TaskTable.COLS.TASK_NAME, task.getTaskName());
        values.put(TaskDBSchema.TaskTable.COLS.TASK_DESCRIPTION, task.getTaskDescription());
        values.put(TaskDBSchema.TaskTable.COLS.TASK_STATE, task.getTaskState().name());
        values.put(TaskDBSchema.TaskTable.COLS.TASK_DATE, task.getTaskDate().getTime());
        values.put(TaskDBSchema.TaskTable.COLS.TASK_INITIATOR, task.getTaskInitiatorUserName());
        return values;
    }
}
