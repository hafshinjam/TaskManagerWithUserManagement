package com.example.taskmanager.control.activity;


import androidx.fragment.app.Fragment;

import com.example.taskmanager.control.fragment.StartManagerFragment;

public class StartManagerActivity extends SingleFragmentActivity {


    public Fragment createFragment() {
        return StartManagerFragment.newInstance();
    }
}