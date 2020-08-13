package com.example.taskmanager.control.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoingTaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoingTaskListFragment extends TaskListFragment {
    private TaskAdapter mAdapter;
    private FloatingActionButton mButtonFloating;


    public DoingTaskListFragment() {
        // Required empty public constructor
    }

    public static DoingTaskListFragment newInstance() {

        Bundle args = new Bundle();

        DoingTaskListFragment fragment = new DoingTaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasks = new ArrayList<Task>();
        List<Task> taskArrayList = mTaskRepository.getList();
        for (int i = 0; i < taskArrayList.size(); i++) {
            if (taskArrayList.get(i).getTaskState() == State.DOING)
                mTasks.add(taskArrayList.get(i));
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
        ArrayList<Task> tasks = (ArrayList<Task>) mTaskRepository.getList();
        if (tasks != null && tasks.size() > 0)
            for (int i = 0; i < tasks.size(); i++) {
                if (!(mTasks.contains(tasks.get(i))) && tasks.get(i).getTaskState() == State.DOING)
                    mTasks.add(tasks.get(i));
            }
    }

    private void setClickListener() {
        mButtonFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mTaskRepository.getList().size();
                int randomState = (int) (1 + Math.random() * 3);
                State rand;
                switch (randomState) {
                    case 1:
                        rand = State.TODO;
                        break;
                    case 2:
                        rand = State.DONE;
                        break;
                    default:
                        rand = State.DOING;
                        break;
                }
                Task task = new Task(mName + " " + (position + 1), rand);
                mTaskRepository.insert(task);
                if (task.getTaskState() == State.DOING)
                    mTasks.add(task);
                isListEmpty();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void findViews(View view) {
        mButtonFloating = view.findViewById(R.id.floatingAddButton);
        mTaskView = view.findViewById(R.id.recycler_view_task_list);
        mImageEmptyList=view.findViewById(R.id.emptyListImage);
        mTextEmptyList=view.findViewById(R.id.emptyListText);
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

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTaskName = itemView.findViewById(R.id.name_row);
            mTextViewTaskStatus = itemView.findViewById(R.id.status_row);

        }

        public void bindTask(Task task) {
            mTask = task;
            mTextViewTaskName.setText(task.getTaskName());
            mTextViewTaskStatus.setText(task.getTaskState().toString());
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