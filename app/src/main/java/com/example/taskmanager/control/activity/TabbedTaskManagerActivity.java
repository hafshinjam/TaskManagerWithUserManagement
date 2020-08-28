package com.example.taskmanager.control.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanager.R;
import com.example.taskmanager.control.fragment.DoingTaskListFragment;
import com.example.taskmanager.control.fragment.DoneTaskListFragment;
import com.example.taskmanager.control.fragment.TaskListFragment;
import com.example.taskmanager.control.fragment.TodoTaskListFragment;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepository;
import com.example.taskmanager.repository.TaskDBRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class TabbedTaskManagerActivity extends AppCompatActivity {
    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabsLayout;
    private IRepository mTaskRepository;
    private User mCurrentUser;


    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, TabbedTaskManagerActivity.class);
        intent.putExtra("CurrentUser", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_task_manager);
        mCurrentUser = (User) getIntent().getSerializableExtra("CurrentUser");
        findViews();
        initUI();
    }

    private void initUI() {
        mTaskRepository = TaskDBRepository.getInstance(this);
        FragmentStateAdapter adapter = new TaskPagerAdapter(this);
        mTaskViewPager.setAdapter(adapter);
        new TabLayoutMediator(mTaskTabsLayout, mTaskViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText(State.TODO.toString());
                                break;
                            case 1:
                                tab.setText(State.DONE.toString());
                                break;
                            case 2:
                                tab.setText(State.DOING.toString());
                                break;
                        }
                    }
                }).attach();
    }

    public class TaskPagerAdapter extends FragmentStateAdapter {

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        private List<TaskListFragment> mTaskListFragments;

        public List<TaskListFragment> getTaskListFragments() {
            return mTaskListFragments;
        }

        public void setTaskListFragments(List<TaskListFragment> taskListFragments) {
            mTaskListFragments = taskListFragments;
        }

        public TaskPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return TodoTaskListFragment.newInstance(mCurrentUser);
                case 1:
                    return DoneTaskListFragment.newInstance(mCurrentUser);
                default:
                    return DoingTaskListFragment.newInstance(mCurrentUser);
            }
         /*   Fragment fragment = new TaskListFragment();
            Bundle args = new Bundle();
            args.putInt("position", position + 1);
            fragment.setArguments(args);*/
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    private void findViews() {
        mTaskViewPager = findViewById(R.id.task_view_pager);
        mTaskTabsLayout = findViewById(R.id.tab_layout_status);
    }
}