package com.example.taskmanager.control.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskDBRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.taskmanager.control.fragment.DatePickerFragment.EXTRA_USER_SELECTED_DATE;
import static com.example.taskmanager.control.fragment.TimePickerFragment.EXTRA_USER_SELECTED_TIME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTasksFragment extends Fragment {
    private User mCurrentUser;
    List<Task> mAllTaskList = new ArrayList<>();
    List<Task> mSearchTasks = new ArrayList<>();
    private TaskAdapter mAdapter;
    private RecyclerView mTaskView;
    private Date mPickedTime;
    private Date mPickedDate;


    private RadioGroup mSearchModelRadioGroup;
    private EditText mNameSearchText;
    private EditText mDescriptionSearchText;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
    private Button mSearchButton;
    private String mSearchMode;
    private TaskDBRepository mTaskRepository;
    private final int DATE_PICKER_REQUEST_CODE = 1;
    protected final int EDIT_TASK_REQUEST_CODE = 4;
    private final int TIME_PICKER_REQUEST_CODE = 2;

    public SearchTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK)
            if (requestCode == DATE_PICKER_REQUEST_CODE) {
                Date datePicked = (Date) data.getSerializableExtra(EXTRA_USER_SELECTED_DATE);
                if (datePicked != null) {
                    mPickedDate.setTime(datePicked.getTime());
                    mDatePickerButton.setText(getDateString());
                }
            } else if (requestCode == TIME_PICKER_REQUEST_CODE) {
                Date timePicked = (Date) data.getSerializableExtra(EXTRA_USER_SELECTED_TIME);
                if (timePicked != null) {
                    mPickedTime.setTime((timePicked.getTime()));
                    mTimePickerButton.setText(getTimeString());
                }
            }
    }


    public static SearchTasksFragment newInstance(User user) {
        SearchTasksFragment fragment = new SearchTasksFragment();
        Bundle args = new Bundle();
        args.putSerializable("currentUser", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPickedTime = new Date();
        mPickedDate = new Date();
        if (getArguments() != null) {
            mCurrentUser = (User) getArguments().getSerializable("currentUser");
        }
        if (getActivity() != null) {
            mTaskRepository = TaskDBRepository.getInstance(getActivity());
            if (!mCurrentUser.getUserName().equals("admin"))
            mAllTaskList = mTaskRepository.getUserTasks(mCurrentUser);
            else
                mAllTaskList=mTaskRepository.getList();
        }
        mSearchMode = "name";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_tasks, container, false);
        findViews(view);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            mTaskView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            mTaskView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        initUI();
        updateUI();
        setClickListeners();
        return view;
    }

    private void initUI() {
        mDatePickerButton.setText(getDateString());
        mTimePickerButton.setText(getTimeString());
    }


    private void setClickListeners() {
        mTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mPickedTime);
                timePickerFragment.setTargetFragment(SearchTasksFragment.this,
                        TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), "DialogTimePicker");
            }
        });
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.
                        newInstance(mPickedDate);
                datePickerFragment.setTargetFragment(SearchTasksFragment.this,
                        DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), "DialogDatePicker");
            }
        });
        mSearchModelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                int index = mSearchModelRadioGroup.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        mSearchMode = "name";
                        setClickable(true, false, false, false);
                        break;
                    case 1:
                        mSearchMode = "description";
                        setClickable(false, true, false, false);
                        break;
                    case 2:
                        mSearchMode = "time";
                        setClickable(false, false, true, false);
                        break;
                    case 3:
                        mSearchMode = "date";
                        setClickable(false, false, false, true);
                        break;
                }
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mSearchMode) {
                    case "name":
                        setListByName();
                        break;
                    case "description":
                        setListByDescription();
                        break;
                    case "date":
                        setListByDate();
                        break;
                    case "time":
                        setListByTime();
                        break;
                }
            }
        });
    }

    private void setClickable(boolean b, boolean b2, boolean b3, boolean b4) {
        mNameSearchText.setFocusable(b);
        mNameSearchText.setFocusableInTouchMode(b);
        mDescriptionSearchText.setFocusable(b2);
        mDescriptionSearchText.setFocusableInTouchMode(b2);
        mTimePickerButton.setClickable(b3);
        mDatePickerButton.setClickable(b4);
    }

    private void setListByTime() {
        mSearchTasks.clear();
        for (Task task : mAllTaskList) {
            if (task.getTaskDate().getHours() == mPickedTime.getHours())
                if (task.getTaskDate().getMinutes() == mPickedTime.getMinutes())
                    mSearchTasks.add(task);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListByDate() {
        mSearchTasks.clear();
        for (Task task : mAllTaskList) {
            if (task.getTaskDate().getYear() == mPickedDate.getYear())
                if (task.getTaskDate().getMonth() == mPickedDate.getMonth())
                    if (task.getTaskDate().getDate() == mPickedDate.getDate())
                        mSearchTasks.add(task);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListByDescription() {
        mSearchTasks.clear();
        for (Task task : mAllTaskList) {
            if (mDescriptionSearchText.getText().toString().equals(""))
                mSearchTasks.add(task);
            else if (task.getTaskDescription().contains(mDescriptionSearchText.getText().toString()))
                mSearchTasks.add(task);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListByName() {
        mSearchTasks.clear();
        for (Task task : mAllTaskList) {
            if (mNameSearchText.getText().toString().equals(""))
                mSearchTasks.add(task);
            else if (task.getTaskName().contains(mNameSearchText.getText().toString()))
                mSearchTasks.add(task);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void findViews(View view) {
        mSearchModelRadioGroup = view.findViewById(R.id.searchModelRadioGroup);
        mNameSearchText = view.findViewById(R.id.titleForSearch);
        mDescriptionSearchText = view.findViewById(R.id.description_search_input);
        mDescriptionSearchText.setFocusable(false);
        mDatePickerButton = view.findViewById(R.id.search_date_picker_button);
        mTimePickerButton = view.findViewById(R.id.search_time_picker_button);
        mSearchButton = view.findViewById(R.id.search_button);
        mTaskView = view.findViewById(R.id.search_task_list);
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private Task mTask;
        private TextView mTextViewTaskName;
        private TextView mTextViewTaskStatus;
        private TextView mTaskDateText;
        private Button mTaskIcon;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTaskName = itemView.findViewById(R.id.name_row);
            mTextViewTaskStatus = itemView.findViewById(R.id.status_row);
            mTaskDateText = itemView.findViewById(R.id.task_date);
            mTaskIcon = itemView.findViewById(R.id.icon_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(mTask);
                    editTaskDialogFragment.setTargetFragment(SearchTasksFragment.this, EDIT_TASK_REQUEST_CODE);
                    editTaskDialogFragment.show(getFragmentManager(), "DialogEditTask");
                }
            });

        }

        public void bindTask(Task task) {
            mTask = task;
            mTextViewTaskName.setText(task.getTaskName());
            mTextViewTaskStatus.setText(task.getTaskState().toString());
            mTaskDateText.setText(task.getTaskDate().toString());
            mTaskIcon.setText(task.getTaskName().substring(0, 1));

        }
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TaskAdapter(mSearchTasks);
            mTaskView.setAdapter(mAdapter);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<SearchTasksFragment.TaskHolder> {

        public List<Task> getTasks() {
            return mSearchTasks;
        }

        public void setTasks(List<Task> tasks) {
            mSearchTasks = tasks;
        }

        public TaskAdapter(List<Task> tasks) {
            mSearchTasks = tasks;
        }

        @Override
        public int getItemCount() {
            return mSearchTasks.size();
        }

        @NonNull
        @Override
        public SearchTasksFragment.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.task_list_row, parent, false);
            SearchTasksFragment.TaskHolder taskHolder = new SearchTasksFragment.TaskHolder(view);
            return taskHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchTasksFragment.TaskHolder holder, int position) {
            Task task = mSearchTasks.get(position);
            if (position % 2 == 0)
                holder.itemView.setBackgroundColor(Color.YELLOW);
            else
                holder.itemView.setBackgroundColor(Color.WHITE);

            holder.bindTask(task);
        }
    }

    public String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        String currentDate = formatter.format(Date.parse(mPickedDate.toString()));
        return currentDate;
    }

    public String getTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:SS");
        String currentTime = formatter.format(Date.parse(mPickedTime.toString()));
        return currentTime;
    }
}