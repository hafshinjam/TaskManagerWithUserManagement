package com.example.taskmanager.control.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.control.activity.TaskManagerActivity;


public class StartManagerFragment extends Fragment {
    public static final String NUMBER_OF_TASKS = "com.example.taskmanager.control.fragment.numberOfTasks";
    public static final String NAME = "com.example.taskmanager.control.fragment.name";
    private TextView mNameText;
    private TextView mTaskNumber;
    private Button mStartMangerButton;


    public StartManagerFragment() {
        // Required empty public constructor
    }

    public static StartManagerFragment newInstance() {
        StartManagerFragment fragment = new StartManagerFragment();
     /*   Bundle args = new Bundle();
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_start_manager, container, false);
        findViews(view);
        setClickListeners();
        return view;
    }

    private void findViews(View view) {
        mNameText = view.findViewById(R.id.PersonName);
        mTaskNumber = view.findViewById(R.id.numberOfTasks);
        mStartMangerButton = view.findViewById(R.id.startTaskManagmentButton);
    }

    private void setClickListeners() {
        mStartMangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startingIntent = TaskManagerActivity.newIntent(getActivity());
                startingIntent.putExtra(NUMBER_OF_TASKS,
                        Integer.parseInt(mTaskNumber.getText().toString()));
                startingIntent.putExtra(NAME,mNameText.getText().toString());
                startActivity(startingIntent);
            }
        });
    }
}