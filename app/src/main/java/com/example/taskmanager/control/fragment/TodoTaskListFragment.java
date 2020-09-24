package com.example.taskmanager.control.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.PictureSourceChooseFragmentDialog;
import com.example.taskmanager.PictureUtils;
import com.example.taskmanager.R;
import com.example.taskmanager.control.activity.TaskSearchActivity;
import com.example.taskmanager.control.activity.UserManagementActivity;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
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
            if (CurrentUser != null)
                if (CurrentUser.getUserName().equals("admin"))
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
        if (!CurrentUser.getUserName().equals("admin"))
            mAccountManagementButton.setVisibility(View.GONE);
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
        if (mTasks.size() == 0) {
            mTextEmptyList.setVisibility(View.VISIBLE);
            mImageEmptyList.setVisibility(View.VISIBLE);
        } else {
            mTextEmptyList.setVisibility(View.GONE);
            mImageEmptyList.setVisibility(View.GONE);
        }

    }

    private void updateList() {
        if (CurrentUser.getUserName().equals("admin"))
            mTasks = mTaskRepository.getStateList(State.TODO);
        else mTasks = mTaskRepository.getStateList(State.TODO, CurrentUser);
    }

    private void setClickListener() {
        mButtonFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task("new Task", "Description", State.TODO,
                        Calendar.getInstance().getTime(), CurrentUser);
                TaskCreateFragment taskCreateFragment = TaskCreateFragment.newInstance(task);

                taskCreateFragment.setTargetFragment(TodoTaskListFragment.this, CREATE_NEW_TASK_REQUEST_CODE);
                if (getFragmentManager() != null)
                    taskCreateFragment.show(getFragmentManager(), DIALOG_CREATE_TASK);
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TaskSearchActivity.newIntent(getActivity(), CurrentUser);
                startActivity(intent);
            }
        });
        mAccountManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UserManagementActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    private void findViews(View view) {
        mButtonFloating = view.findViewById(R.id.floatingAddButton);
        mTaskView = view.findViewById(R.id.recycler_view_task_list);
        mImageEmptyList = view.findViewById(R.id.emptyListImage);
        mTextEmptyList = view.findViewById(R.id.emptyListText);
        mSearchButton = view.findViewById(R.id.floatingSearchButton);
        mAccountManagementButton = view.findViewById(R.id.account_managment_button);
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
        private ImageButton mTaskIcon;
        private ImageButton mShareTaskButton;
        private File mPhotoFile;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTaskName = itemView.findViewById(R.id.name_row);
            mTextViewTaskStatus = itemView.findViewById(R.id.status_row);
            mTaskDateText = itemView.findViewById(R.id.task_date);
            mTaskIcon = itemView.findViewById(R.id.icon_image);
            mShareTaskButton = itemView.findViewById(R.id.share_button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(mTask);
                    editTaskDialogFragment.setTargetFragment(TodoTaskListFragment.this, EDIT_TASK_REQUEST_CODE);
                    editTaskDialogFragment.show(getFragmentManager(), "DialogEditTask");
                }
            });
            mShareTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        Intent sendIntent = ShareCompat.IntentBuilder.from(getActivity()).
                                setType("text/plain").
                                setSubject("share task").
                                setText(mTask.getTaskTextToShare()).getIntent();
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
                            startActivity(shareIntent);
                    }
                }
            });
            mTaskIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSourceChooseFragmentDialog fragment = PictureSourceChooseFragmentDialog.newInstance(mTask);
                    fragment.setTargetFragment(TodoTaskListFragment.this,
                            CHOSSE_SOURCE_FROM_FRAGMENT_REQUEST_CODE);
                    if (getFragmentManager() != null)
                        fragment.show(getFragmentManager(), "ImageSourceChoose");
                }
            });
        }

        public void bindTask(Task task) {
            mTask = task;
            mTextViewTaskName.setText(task.getTaskName());
            mTextViewTaskStatus.setText(task.getTaskState().toString());
            mTaskDateText.setText(task.getTaskDate().toString());
            if (mTask.getTaskPicturePath() != null) {
                mPhotoFile = new File(mTask.getTaskPicturePath());

                mTaskIcon.setImageURI(Uri.parse(mPhotoFile.getPath()));
            } else {
                mPhotoFile = mTaskRepository.generatePhotoFilesDir(getActivity(), task);
                if (mPhotoFile != null && mPhotoFile.exists()) {
                    Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                    mTaskIcon.setImageBitmap(bitmap);
                }
            }
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
            return new TaskHolder(view);
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