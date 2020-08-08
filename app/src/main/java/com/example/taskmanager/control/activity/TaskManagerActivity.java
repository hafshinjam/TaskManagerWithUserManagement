package com.example.taskmanager.control.activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.taskmanager.control.fragment.TaskListFragment;

import static com.example.taskmanager.control.fragment.StartManagerFragment.NAME;
import static com.example.taskmanager.control.fragment.StartManagerFragment.NUMBER_OF_TASKS;

public class TaskManagerActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TaskManagerActivity.class);
        return intent;
    }


    @Override
    public Fragment createFragment() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(NAME);
        int numberOfTasks = intent.getIntExtra(NUMBER_OF_TASKS, 0);
        return TaskListFragment.newInstance(name, numberOfTasks);
    }

}