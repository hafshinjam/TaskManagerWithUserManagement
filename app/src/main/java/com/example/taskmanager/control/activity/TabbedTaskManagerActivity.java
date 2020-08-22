package com.example.taskmanager.control.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.R;
import com.example.taskmanager.control.fragment.DoingTaskListFragment;
import com.example.taskmanager.control.fragment.DoneTaskListFragment;
import com.example.taskmanager.control.fragment.TaskListFragment;
import com.example.taskmanager.control.fragment.TodoTaskListFragment;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/*import static com.example.taskmanager.control.fragment.StartManagerFragment.NAME;
import static com.example.taskmanager.control.fragment.StartManagerFragment.NUMBER_OF_TASKS;*/

public class TabbedTaskManagerActivity extends AppCompatActivity {
    private ViewPager2 mTaskViewPager;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private TabLayout mTaskTabsLayout;
    private TaskPagerAdapter mTaskPagerAdapter;
    private IRepository mTaskRepository;
    private String mName;
    private int mNumberOfTasks;
    private User mCurrentUser;


    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, TabbedTaskManagerActivity.class);
        intent.putExtra("CurrentUser",user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_task_manager);
 /*       mName = getIntent().getStringExtra(NAME);
        mNumberOfTasks = getIntent().getIntExtra(NUMBER_OF_TASKS, 1);*/
        mCurrentUser= (User) getIntent().getSerializableExtra("CurrentUser");
        findViews();
        initUI();
    }

    private void initUI() {
        mTaskRepository =  TaskRepository.getInstance();
/*        List<TaskListFragment> taskListFragmentList = new ArrayList<TaskListFragment>();
        taskListFragmentList.add(TodoTaskListFragment.newInstance());
        taskListFragmentList.add(DoneTaskListFragment.newInstance());
        taskListFragmentList.add(DoingTaskListFragment.newInstance());*/
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


    /*public Fragment CreateFragment() {
        return TodoTaskListFragment.newInstance();
    }*/

    private void findViews() {
        mTaskViewPager = findViewById(R.id.task_view_pager);
        mTaskTabsLayout = findViewById(R.id.tab_layout_status);
    }
}