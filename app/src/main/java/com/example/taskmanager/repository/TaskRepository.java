package com.example.taskmanager.repository;

import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository implements IRepository<Task> {
    private static TaskRepository sTaskRepository;
    private static int NUMBER_OF_TASKS;

    private static String taskStartingName;

    public static String getTaskStartingName() {
        return taskStartingName;
    }

    public static void setTaskStartingName(String taskStartingName) {
        TaskRepository.taskStartingName = taskStartingName;
    }


    public static void setNumberOfTasks(int numberOfTasks) {
        NUMBER_OF_TASKS = numberOfTasks;
    }

    public static TaskRepository newInstance(String name, int numberOfTasks){
        sTaskRepository = new TaskRepository(name,numberOfTasks);
        return sTaskRepository;
    }

    public static TaskRepository getInstance() {
        if (sTaskRepository == null)
            sTaskRepository = TaskRepository.newInstance("", 1);
        return sTaskRepository;
    }

    private List<Task> mTasks;

    //************************************************************
    //Set Repository size and task's starting name here
    //************************************************************
    public TaskRepository(String name, int numberOfTasks) {
        setNumberOfTasks(numberOfTasks);
        setTaskStartingName(name);
        mTasks = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            Task task = new Task();
            task.setTaskName(name + " " + (i + 1));
            if (i % 3 == 0)
                task.setTaskState(State.DOING);
            else if (i % 3 == 1)
                task.setTaskState(State.TODO);
            else task.setTaskState(State.DONE);
            mTasks.add(task);
        }
    }

    @Override
    public List<Task> getList() {
        return mTasks;
    }

    public List<Task> getStateList(State state) {
       List<Task> todoList = new ArrayList<>();
        for (Task task : mTasks) {
            if (task.getTaskState()==state)
                todoList.add(task);
        }
        return todoList;
    }

    @Override
    public Task get(UUID uuid) {
        for (Task task : mTasks) {
            if (task.getTaskID().equals(uuid))
                return task;
        }
        return null;
    }

    @Override
    public void setList(List<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public void update(Task task) {
        Task updateTask = get(task.getTaskID());
        updateTask.setTaskID(task.getTaskID());
        updateTask.setTaskName(task.getTaskName());
        updateTask.setTaskState(task.getTaskState());
    }

    @Override
    public void delete(Task task) {
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getTaskID().equals(task.getTaskID())) {
                mTasks.remove(i);
                return;
            }
        }
    }

    @Override
    public void insert(Task task) {
        mTasks.add(task);

    }

    @Override
    public void insertList(List<Task> tasks) {
        mTasks.addAll(tasks);
    }

    @Override
    public int getPosition(UUID uuid) {
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getTaskID().equals(uuid))
                return i;
        }
        return -1;
    }
}
