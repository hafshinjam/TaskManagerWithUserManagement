package com.example.taskmanager.control.activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.control.fragment.TodoTaskListFragment;

import static com.example.taskmanager.control.fragment.StartManagerFragment.NAME;
import static com.example.taskmanager.control.fragment.StartManagerFragment.NUMBER_OF_TASKS;

public class TaskManagerActivity extends SingleFragmentActivity {
  static String taskName;
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TaskManagerActivity.class);
        intent.putExtra(NAME,taskName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        taskName = intent.getStringExtra(NAME);
    }

    @Override
    public Fragment createFragment() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(NAME);
        int numberOfTasks = intent.getIntExtra(NUMBER_OF_TASKS, 0);
        return TodoTaskListFragment.newInstance();
    }

}