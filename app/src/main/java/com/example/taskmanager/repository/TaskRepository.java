package com.example.taskmanager.repository;

import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository implements IRepository<Task> {
    private static TaskRepository sTaskRepository;
    private static int NUMBER_OF_TASKS;



    /*public static void setNumberOfTasks(int numberOfTasks) {
        NUMBER_OF_TASKS = numberOfTasks;
    }*/

    public static TaskRepository newInstance() {

        sTaskRepository = new TaskRepository();
        return sTaskRepository;
    }

    public static TaskRepository getInstance() {
        if (sTaskRepository == null)
            sTaskRepository = TaskRepository.newInstance();
        return sTaskRepository;
    }

    private List<Task> mTasks;

    public TaskRepository() {
        mTasks = new ArrayList<>();
/*        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            Task task = new Task();
            task.setTaskName( " " + (i + 1));
            if (i % 3 == 0)
                task.setTaskState(State.DOING);
            else if (i % 3 == 1)
                task.setTaskState(State.TODO);
            else task.setTaskState(State.DONE);
            mTasks.add(task);
        }*/
    }

    public List<Task> getUserTasks(User user) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : mTasks) {
            if (task.getTaskInitiator().getID() == user.getID()) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    @Override
    public List<Task> getList() {
        return mTasks;
    }

    public List<Task> getStateList(State state) {
        List<Task> todoList = new ArrayList<>();
        for (Task task : mTasks) {
            if (task.getTaskState() == state)
                todoList.add(task);
        }
        return todoList;
    }

    /**
     * user this method to get list of task with this:
     * @param state and for this :
     * @param user user
     * @return
     */
    public List<Task> getStateList(State state, User user) {
        List<Task> taskList = new ArrayList<>();
        for (Task task : mTasks) {
            if (task.getTaskState() == state && task.getTaskInitiator().getUserName() .equals(user.getUserName()) )
                taskList.add(task);
        }
        return taskList;
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
        mTasks = TaskRepository.getInstance().getList();
        mTasks.set(mTasks.indexOf(task), task);
        setList(mTasks);
    /*    Task updateTask = get(task.getTaskID());
        updateTask.setTaskID(task.getTaskID());
        updateTask.setTaskName(task.getTaskName());
        updateTask.setTaskState(task.getTaskState());
        updateTask.setTaskDescription(task.getTaskDescription());
        updateTask.setTaskDate(task.getTaskDate());*/

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
