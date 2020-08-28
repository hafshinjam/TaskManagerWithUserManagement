package com.example.taskmanager.control.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.util.Date;

import static com.example.taskmanager.control.fragment.DatePickerFragment.EXTRA_USER_SELECTED_DATE;
import static com.example.taskmanager.control.fragment.TimePickerFragment.EXTRA_USER_SELECTED_TIME;


public class TaskCreateFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static final String ARGS_NEW_TASK = "NewTask";
    private final int DATE_PICKER_REQUEST_CODE = 1;
    private final int TIME_PICKER_REQUEST_CODE = 2;
    public static final String EXTRA_TASK_CREATED = "extraTaskCreated";
    private TextView mTitleText;
    private TextView mDescriptionText;
    private Spinner mTaskSpinner;
    private ArrayAdapter<CharSequence> mTaskSpinnerArrayAdapter;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
    private Button mCancelButton;
    private Button mSaveButton;
    private Task mTask;


    public TaskCreateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TaskCreateFragment newInstance(Task task) {
        TaskCreateFragment fragment = new TaskCreateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_NEW_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTask = (Task) getArguments().getSerializable(ARGS_NEW_TASK);
        }
    }

 /*   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_create, container, false);
        initViews(view);
        setListeners();
        return view;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_task_create,null);
        initViews(view);
        setListeners();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
/*        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height=WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(layoutParams);*/
        return alertDialog;

    }

    private void initViews(View view) {
        mTitleText = view.findViewById(R.id.title_text_input);
        mDescriptionText = view.findViewById(R.id.description_text_Input);
        mTaskSpinner = view.findViewById(R.id.state_spinner);
        mDatePickerButton = view.findViewById(R.id.date_picker_button);
        mTimePickerButton = view.findViewById(R.id.time_picker_button);
        mCancelButton=view.findViewById(R.id.cancel_button);
        mSaveButton=view.findViewById(R.id.save_button);
        mTaskSpinnerArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.states,
                android.R.layout.simple_spinner_dropdown_item);
        mTaskSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskSpinner.setAdapter(mTaskSpinnerArrayAdapter);
        mTaskSpinner.setOnItemSelectedListener(this);
        mTitleText.setHint(mTask.getTaskName());
        mDescriptionText.setHint(mTask.getTaskDescription());
        mDatePickerButton.setText(mTask.getDateString());
        mTimePickerButton.setText(mTask.getTimeString());
    }

    public void setListeners() {
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.
                        newInstance(mTask.getTaskDate());
                datePickerFragment.setTargetFragment(TaskCreateFragment.this,
                        DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), "DialogDatePicker");
            }
        });
        mTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mTask.getTaskDate());
                timePickerFragment.setTargetFragment(TaskCreateFragment.this,
                        TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), "DialogTimePicker");
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTask.setTaskName(mTitleText.getText().toString());
                mTask.setTaskDescription(mDescriptionText.getText().toString());
                Fragment fragment = getTargetFragment();
                Intent intent =new Intent();
                intent.putExtra(EXTRA_TASK_CREATED,mTask);
                fragment.onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);
                fragment.onResume();
                dismiss();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getTargetFragment();
                Intent intent =new Intent();
                fragment.onActivityResult(getTargetRequestCode(),Activity.RESULT_CANCELED,intent);
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK)
            if (requestCode == 1) {
                Date datePicked = (Date) data.getSerializableExtra(EXTRA_USER_SELECTED_DATE);
                mTask.setTaskDate(datePicked);
                mDatePickerButton.setText(mTask.getDateString());
            } else if (requestCode == 2) {
                Date timePicked = (Date) data.getSerializableExtra(EXTRA_USER_SELECTED_TIME);
                mTask.setTaskDate(timePicked);
                mTimePickerButton.setText(mTask.getTimeString());
            }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 1:
                mTask.setTaskState(State.DOING);
                break;
            case 2:
                mTask.setTaskState(State.DONE);
                break;
            default:
                mTask.setTaskState(State.TODO);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mTask.setTaskState(State.TODO);
    }
}