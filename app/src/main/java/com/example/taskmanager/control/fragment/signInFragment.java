package com.example.taskmanager.control.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.control.activity.TabbedTaskManagerActivity;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepository;
import com.example.taskmanager.repository.UserDBRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class signInFragment extends Fragment {
    IRepository<User> mUserIRepository;
    private TextView mUserNameText;
    private TextView mPasswordText;
    private Button mSignUpButton;
    private Button mLogInButton;
    private CoordinatorLayout mCoordinatorLayout;
    private List<User> mUserList;


    public signInFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static signInFragment newInstance() {
        signInFragment fragment = new signInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserIRepository = UserDBRepository.getInstance(getActivity());
        //temp admin add
        mUserList = mUserIRepository.getList();
        User admin = new User(UUID.randomUUID(), "admin", "12345");
        if (!mUserList.contains(admin)) {
            mUserIRepository.insert(admin);
        }
        mUserList = mUserIRepository.getList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        findViews(view);
        setOnClickListeners();
        return view;
    }

    private void findViews(View view) {
        mUserNameText = view.findViewById(R.id.user_name_sign_in);
        mPasswordText = view.findViewById(R.id.password_text_sign_in);
        mSignUpButton = view.findViewById(R.id.signup_button);
        mLogInButton = view.findViewById(R.id.login_button);
        mCoordinatorLayout = view.findViewById(R.id.coordinator_layout_sign_in);
    }

    public void setOnClickListeners() {

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mUserList.isEmpty()) {
                    for (int i = 0; i < mUserList.size(); i++) {
                        if (mUserList.get(i).getUserName().equals(mUserNameText.getText().toString())) {
                            if (mUserList.get(i).getPassword().equals(mPasswordText.getText().toString())) {
                                Snackbar.make(mCoordinatorLayout,
                                        getString(R.string.login_message), Snackbar.LENGTH_LONG).show();
                                Intent intent = TabbedTaskManagerActivity.newIntent(getActivity(), mUserList.get(i));
                                startActivity(intent);
                                break;
                            } else Snackbar.make(mCoordinatorLayout,
                                    getString(R.string.login_failed_message), Snackbar.LENGTH_LONG).show();
                            break;
                        }
                       else if (i == mUserList.size() - 1 && (!mUserList.get(i).getUserName().
                                equals(mUserNameText.getText().toString())
                                || !mUserList.get(i).getPassword().equals(mPasswordText.getText().toString())))
                            Snackbar.make(mCoordinatorLayout,
                                    getString(R.string.login_failed_message), Snackbar.LENGTH_LONG).show();
                    }
                } else Snackbar.make(mCoordinatorLayout,
                        getString(R.string.login_failed_message), Snackbar.LENGTH_LONG).show();
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, signUpFragment.newInstance(),
                                "signUpFragment").commit();
            }
        });
    }
}