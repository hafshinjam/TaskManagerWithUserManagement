package com.example.taskmanager.control.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.taskmanager.control.fragment.signInFragment;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepository;
import com.example.taskmanager.repository.TaskDBRepository;
import com.example.taskmanager.repository.UserDBRepository;

public class SignInSignUpActivity extends SingleFragmentActivity {
    private IRepository<Task> mTaskIRepository;
    private IRepository<User> mUserIRepository;

    @Override
    public Fragment createFragment() {

        return signInFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserIRepository = UserDBRepository.getInstance(this);
        mTaskIRepository = TaskDBRepository.getInstance(this);
    }
}