package com.example.taskmanager.control.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskDBRepository;
import com.example.taskmanager.repository.UserDBRepository;

import java.util.ArrayList;
import java.util.List;


public class UserManagementFragment extends Fragment {
    private RecyclerView mUserRecycler;
    private UserDBRepository mUserDBRepository;
    private TaskDBRepository mTaskDBRepository;
    private List<User> mUserList;
    private UserAdapter mAdapter;

    public UserManagementFragment() {
        // Required empty public constructor
    }

    public static UserManagementFragment newInstance() {
        return new UserManagementFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mUserDBRepository = UserDBRepository.getInstance(getActivity());
            mTaskDBRepository = TaskDBRepository.getInstance(getActivity());
            mUserList = mUserDBRepository.getList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_management, container, false);
        mUserRecycler = view.findViewById(R.id.user_list_recycler);
        updateUI();
        mUserRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new UserAdapter(mUserList);
            mUserRecycler.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    private class UserHolder extends RecyclerView.ViewHolder {
        private User mUser;
        private TextView mUserName;
        private TextView mRegDate;
        private TextView mTaskCount;
        private Button mDeleteUserButton;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.user_name_managment);
            mRegDate = itemView.findViewById(R.id.sign_up_date_text);
            mTaskCount = itemView.findViewById(R.id.user_Task_count);
            mDeleteUserButton = itemView.findViewById(R.id.delete_user_button);
            mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User tempUser = mUser;
                    mUserDBRepository.delete(mUser);
                    mUserList = mUserDBRepository.getList();
                    mAdapter.notifyDataSetChanged();
                    ArrayList<Task> tasks= (ArrayList<Task>) mTaskDBRepository.getUserTasks(tempUser);
                    for (Task task:tasks) {
                        mTaskDBRepository.delete(task);
                    }
                }
            });
        }

        public void bindUser(User user) {
            mUser = user;
            mUserName.setText(user.getUserName());
            mRegDate.setText(user.getRegDate().toString());
            mTaskCount.setText(getString(R.string.task_count, mTaskDBRepository.getUserTasks(user).size()));
        }

    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {

        public List<User> getUsers() {
            return mUserList;
        }

        public void setUsers(List<User> users) {
            mUserList = users;
        }

        public UserAdapter(List<User> users) {
            mUserList = users;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.user_list_row, parent, false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            User user = mUserList.get(position);
            holder.bindUser(user);
        }

        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }
}