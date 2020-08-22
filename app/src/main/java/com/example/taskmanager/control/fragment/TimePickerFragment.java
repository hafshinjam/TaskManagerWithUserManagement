package com.example.taskmanager.control.fragment;

import com.example.taskmanager.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimePickerFragment extends DialogFragment {
    public static final String ARG_TIME = "currentTime";
    public static final String EXTRA_USER_SELECTED_TIME = "timePicked";
    private Date mCurrentTme;

    private TimePicker mTimePicker;

    public TimePickerFragment() {
        // Required empty public constructor
    }


    public static TimePickerFragment newInstance(Date currentTme) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, currentTme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentTme = (Date) getArguments().getSerializable(ARG_TIME);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time_picker, null);

        mTimePicker = view.findViewById(R.id.time_picker_task);
        initTimePicker();

        return new AlertDialog.Builder(getActivity()).setTitle("Time Of Crime").
                setView(view).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = getInstance();
                calendar.setTime(mCurrentTme);
                calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                calendar.set(Calendar.MINUTE, mTimePicker.getMinute());
                Date date = calendar.getTime();
                setResult(date);
            }
        }).create();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTimePicker() {
        Calendar calendar = getInstance();
        calendar.setTime(mCurrentTme);
        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));
    }

    private void setResult(Date timePicked) {
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_TIME, timePicked);
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false);

    }
}