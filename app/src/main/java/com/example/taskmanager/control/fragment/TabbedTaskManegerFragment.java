package com.example.taskmanager.control.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;


public class TabbedTaskManegerFragment extends Fragment {




    public TabbedTaskManegerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TabbedTaskManegerFragment newInstance() {
        TabbedTaskManegerFragment fragment = new TabbedTaskManegerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabbed_task_maneger, container, false);
    }
}