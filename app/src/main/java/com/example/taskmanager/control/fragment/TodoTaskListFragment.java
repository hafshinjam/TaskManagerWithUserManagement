package com.example.taskmanager.control.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.control.activity.TaskSearchActivity;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoTaskListFragment extends TaskListFragment {
    private TaskAdapter mAdapter;
    private FloatingActionButton mButtonFloating;


    public TodoTaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public static TodoTaskListFragment newInstance(User user) {
        Bundle args = new Bundle();

        TodoTaskListFragment fragment = new TodoTaskListFragment();
        args.putSerializable("CurrentUser", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasks = new ArrayList<>();
        if (getArguments() != null) {
            CurrentUser = (User) getArguments().getSerializable("CurrentUser");
            if (CurrentUser.getUserName() .equals("admin"))
                mTasks = mTaskRepository.getStateList(State.TODO);
            else mTasks = mTaskRepository.getStateList(State.TODO, CurrentUser);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        findViews(view);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            mTaskView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            mTaskView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        setClickListener();
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
        isListEmpty();
        mAdapter.notifyDataSetChanged();
    }
    protected void isListEmpty() {
        if (mTasks.size() == 0 ) {
            mTextEmptyList.setVisibility(View.VISIBLE);
            mImageEmptyList.setVisibility(View.VISIBLE);
        } else {
            mTextEmptyList.setVisibility(View.GONE);
            mImageEmptyList.setVisibility(View.GONE);
        }

    }
    private void updateList() {
        if (CurrentUser.getUserName() .equals("admin"))
            mTasks = mTaskRepository.getStateList(State.TODO);
        else mTasks = mTaskRepository.getStateList(State.TODO, CurrentUser);
    }

    private void setClickListener() {
        mButtonFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonFloating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Task task = new Task("new Task", "Description", State.TODO,
                                Calendar.getInstance().getTime(),CurrentUser);
                        TaskCreateFragment taskCreateFragment = TaskCreateFragment.newInstance(task);

                        taskCreateFragment.setTargetFragment(TodoTaskListFragment.this, CREATE_NEW_TASK_REQUEST_CODE);

                        taskCreateFragment.show(getFragmentManager(), DIALOG_CREATE_TASK);
                    }
                });
                isListEmpty();
                mAdapter.notifyDataSetChanged();
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = TaskSearchActivity.newIntent(getActivity(),CurrentUser);
            startActivity(intent);
            }
        });
    }

    private void findViews(View view) {
        mButtonFloating = view.findViewById(R.id.floatingAddButton);
        mTaskView = view.findViewById(R.id.recycler_view_task_list);
        mImageEmptyList=view.findViewById(R.id.emptyListImage);
        mTextEmptyList=view.findViewById(R.id.emptyListText);
        mSearchButton = view.findViewById(R.id.floatingSearchButton);
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TaskAdapter(mTasks);
            mTaskView.setAdapter(mAdapter);
        }
        isListEmpty();
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
            mTaskIcon=itemView.findViewById(R.id.icon_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(mTask);
                    editTaskDialogFragment.setTargetFragment(TodoTaskListFragment.this, EDIT_TASK_REQUEST_CODE);
                    editTaskDialogFragment.show(getFragmentManager(),"DialogEditTask");
                }
            });

        }

        public void bindTask(Task task) {
            mTask = task;
            mTextViewTaskName.setText(task.getTaskName());
            mTextViewTaskStatus.setText(task.getTaskState().toString());
            mTaskDateText.setText(task.getTaskDate().toString());
            mTaskIcon.setText(task.getTaskName().substring(0,1));

        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.task_list_row, parent, false);
            TaskHolder taskHolder = new TaskHolder(view);
            return taskHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            if (position % 2 == 0)
                holder.itemView.setBackgroundColor(Color.YELLOW);
            else
                holder.itemView.setBackgroundColor(Color.WHITE);

            holder.bindTask(task);
        }
    }

}