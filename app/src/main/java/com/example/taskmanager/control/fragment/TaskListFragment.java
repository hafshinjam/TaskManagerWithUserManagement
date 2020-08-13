package com.example.taskmanager.control.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class TaskListFragment extends Fragment {
    protected TaskRepository mTaskRepository;
    public static final String NUMBER_OF_TASKS = "numberOfTasks";
    public static final String NAME = "Name";
    protected String mName;
    protected List<Task> mTasks;
    protected RecyclerView mTaskView;
    protected ImageView mImageEmptyList;
    protected TextView mTextEmptyList;

    public TaskListFragment() {
        // Required empty public constructor
    }



    public static TaskListFragment newInstance() {
        TaskListFragment taskListFragment = new TaskListFragment();
        return taskListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskRepository = TaskRepository.getInstance();
        mName = mTaskRepository.getTaskStartingName();


    }
}