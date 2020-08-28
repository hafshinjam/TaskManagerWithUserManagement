package com.example.taskmanager.control.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.control.fragment.SearchTasksFragment;
import com.example.taskmanager.model.User;

public class TaskSearchActivity extends SingleFragmentActivity {
    private User CurrentUser;

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, TaskSearchActivity.class);
        intent.putExtra("CurrentUser", user);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return SearchTasksFragment.newInstance(CurrentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CurrentUser = (User) getIntent().getSerializableExtra("CurrentUser");
        super.onCreate(savedInstanceState);

    }
}