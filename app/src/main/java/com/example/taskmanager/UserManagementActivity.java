package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.control.activity.SingleFragmentActivity;
import com.example.taskmanager.control.activity.TaskSearchActivity;
import com.example.taskmanager.model.User;

public class UserManagementActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent( context, UserManagementActivity.class);
    }
    @Override
    public Fragment createFragment() {
        return UserManagementFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}