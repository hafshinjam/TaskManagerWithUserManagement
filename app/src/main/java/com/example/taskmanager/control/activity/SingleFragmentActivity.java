package com.example.taskmanager.control.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;


import com.example.taskmanager.R;
/**
 * If we have activity that has only one fullscreen fragment we must
 * extend this Activity
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    public static final String FRAGMENT_TAG = "FragmentActivity";

    public abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, createFragment(), FRAGMENT_TAG)
                    .commit();
        }
    }
}