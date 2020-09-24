package com.example.taskmanager.control.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskDBRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.taskmanager.control.fragment.TaskCreateFragment.EXTRA_TASK_CREATED;


public class TaskListFragment extends Fragment {
    protected TaskDBRepository mTaskRepository;
    public static final String NUMBER_OF_TASKS = "numberOfTasks";
    protected String DIALOG_CREATE_TASK = "com.example.taskmanager.control.fragment.DialogCreateTask";
    public static final String NAME = "Name";
    protected String mName;
    protected List<Task> mTasks;
    protected RecyclerView mTaskView;
    protected ImageView mImageEmptyList;
    protected User CurrentUser;
    protected TextView mTextEmptyList;
    protected final int EDIT_TASK_REQUEST_CODE = 3;
    protected static final int CREATE_NEW_TASK_REQUEST_CODE = 0;
    protected final int CHOSSE_SOURCE_FROM_FRAGMENT_REQUEST_CODE = 4;
    public static final String FILEPROVIDER_AUTHORITY = "com.example.taskmanager.fileprovider";
    protected List<Task> mTaskList;
    protected FloatingActionButton mSearchButton;
    protected FloatingActionButton mAccountManagementButton;

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null)
            if (requestCode == CREATE_NEW_TASK_REQUEST_CODE && data.getExtras() != null) {
                Task tempTask = (Task) data.getExtras().getSerializable(EXTRA_TASK_CREATED);
                mTaskRepository.insert(tempTask);
            }
    }

    public static TaskListFragment newInstance() {
        TaskListFragment taskListFragment = new TaskListFragment();
        return taskListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            mTaskRepository = TaskDBRepository.getInstance(getActivity());
        /* mName = mTaskRepository.getTaskStartingName();*/
    }
}