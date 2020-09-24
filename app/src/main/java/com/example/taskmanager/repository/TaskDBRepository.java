package com.example.taskmanager.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.taskmanager.database.TaskDataBase;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDBRepository implements IRepository<Task> {
    //memory leak -handle later
    private static TaskDBRepository sTaskRepository;
    private static Context mContext;

    private TaskDataBase mDatabase;

    public static TaskDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sTaskRepository == null)
            sTaskRepository = new TaskDBRepository();
        return sTaskRepository;
    }

    public TaskDBRepository() {
        mDatabase = Room.databaseBuilder(mContext,
                TaskDataBase.class,
                "TaskDB.db")
                .allowMainThreadQueries()
                .build();
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
        return mDatabase.TaskDAO().getTasks();
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
        return mDatabase.TaskDAO().getTask(uuid.toString());
    }

    @Override
    public void update(Task task) {
        mDatabase.TaskDAO()
                .updateTask(task);
    }

    @Override
    public void delete(Task task) {
        mDatabase.TaskDAO()
                .deleteTask(task);
    }

    @Override
    public void insert(Task task) {
        mDatabase.TaskDAO().insertTask(task);
    }

    @Override
    public void insertList(List<Task> list) {
        Task[] tasks = new Task[list.size()];
        tasks = list.toArray(tasks);
        mDatabase.TaskDAO().insertTasks(tasks);
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

    public File generatePhotoFilesDir(Context context, Task task) {
            return new File(context.getFilesDir(), task.generatePhotoFileName());
    }
}
