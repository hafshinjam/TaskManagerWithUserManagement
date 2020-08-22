package com.example.taskmanager.control.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepository;
import com.example.taskmanager.repository.TaskRepository;

import java.net.StandardSocketOptions;
import java.util.Date;

import static com.example.taskmanager.control.fragment.DatePickerFragment.EXTRA_USER_SELECTED_DATE;
import static com.example.taskmanager.control.fragment.TimePickerFragment.EXTRA_USER_SELECTED_TIME;

public class EditTaskDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static final String CURRENT_TASK_TO_EDIT = "taskToEdit";
    private TextView mTitleText;
    private TextView mDescriptionText;
    private Spinner mTaskSpinner;
    private ArrayAdapter<CharSequence> mTaskSpinnerArrayAdapter;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
    private Button mSaveButton;
    private Button mDeleteButton;
    private Button mEditButton;
    private Task mCurrentTask;
    IRepository<Task> taskRepository = TaskRepository.getInstance();


    private final int DATE_PICKER_REQUEST_CODE = 1;
    private final int TIME_PICKER_REQUEST_CODE = 2;

    public EditTaskDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditTaskDialogFragment newInstance(Task task) {
        EditTaskDialogFragment fragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_TASK_TO_EDIT, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentTask = (Task) getArguments().getSerializable(CURRENT_TASK_TO_EDIT);
        }
    }

    /*   @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
           // Inflate the layout for this fragment
           View view = inflater.inflate(R.layout.fragment_edit_task_dialog, container, false);
           return view;
       }*/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_edit_task_dialog, null);
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
        mTitleText.setText(mCurrentTask.getTaskName());
        mTitleText.setEnabled(false);
        mDescriptionText = view.findViewById(R.id.description_text_Input);
        mDescriptionText.setText(mCurrentTask.getTaskDescription());
        mDescriptionText.setEnabled(false);
        mTaskSpinner = view.findViewById(R.id.state_spinner);
        mDatePickerButton = view.findViewById(R.id.date_picker_button);
        mTimePickerButton = view.findViewById(R.id.time_picker_button);
        mSaveButton = view.findViewById(R.id.save_button);
        mDeleteButton = view.findViewById(R.id.delete_button);
        mEditButton = view.findViewById(R.id.edit_button);
        mTaskSpinnerArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.states,
                android.R.layout.simple_spinner_dropdown_item);
        mTaskSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskSpinner.setAdapter(mTaskSpinnerArrayAdapter);
        mTaskSpinner.setOnItemSelectedListener(this);
        mTaskSpinner.setEnabled(false);

        mTimePickerButton.setText(mCurrentTask.getTimeString());
        mDatePickerButton.setText(mCurrentTask.getDateString());
    }

    private void setListeners() {
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.
                        newInstance(mCurrentTask.getTaskDate());
                datePickerFragment.setTargetFragment(EditTaskDialogFragment.this,
                        DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), "DialogDatePicker");
            }
        });
        mTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCurrentTask.getTaskDate());
                timePickerFragment.setTargetFragment(EditTaskDialogFragment.this,
                        TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), "DialogTimePicker");
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleText.setEnabled(true);
                mDescriptionText.setEnabled(true);
                mDatePickerButton.setClickable(true);
                mTimePickerButton.setClickable(true);
                mTaskSpinner.setEnabled(true);
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taskRepository.delete(mCurrentTask);
                getTargetFragment().onResume();
                dismiss();
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentTask.setTaskName(mTitleText.getText().toString());
                mCurrentTask.setTaskDescription(mDescriptionText.getText().toString());
                taskRepository.update(mCurrentTask);
                getTargetFragment().onResume();
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK)
            if (requestCode == DATE_PICKER_REQUEST_CODE) {
                Date datePicked = (Date) data.getSerializableExtra(EXTRA_USER_SELECTED_DATE);
                mCurrentTask.setTaskDate(datePicked);
                mDatePickerButton.setText(mCurrentTask.getDateString());
            } else if (requestCode == TIME_PICKER_REQUEST_CODE) {
                Date timePicked = (Date) data.getSerializableExtra(EXTRA_USER_SELECTED_TIME);
                mCurrentTask.setTaskDate(timePicked);
                mTimePickerButton.setText(mCurrentTask.getTimeString());
            }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 1:
                mCurrentTask.setTaskState(State.DOING);
                break;
            case 2:
                mCurrentTask.setTaskState(State.DONE);
                break;
            default:
                mCurrentTask.setTaskState(State.TODO);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}