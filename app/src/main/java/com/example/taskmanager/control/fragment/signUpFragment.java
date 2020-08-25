package com.example.taskmanager.control.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserDBRepository;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.UUID;


public class signUpFragment extends Fragment {
    private TextView mUserNameText;
    private TextView mPasswordText;
    private Button mSignUp;
    private ArrayList<User> mUsers = new ArrayList<>();
    private CoordinatorLayout mSignUpCoordinatorLayout;


    public signUpFragment() {
        // Required empty public constructor
    }

    public static signUpFragment newInstance() {
        signUpFragment fragment = new signUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsers = (ArrayList<User>) UserDBRepository.getInstance(getActivity()).getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        findViews(view);
        setOnClickListeners();
        return view;
    }

    public void findViews(View view) {
        mUserNameText = view.findViewById(R.id.user_name_sign_up);
        mPasswordText = view.findViewById(R.id.password_text_sign_up);
        mSignUp = view.findViewById(R.id.signup_button);
        mSignUpCoordinatorLayout = view.findViewById(R.id.coordinator_layout_sign_up);
    }

    private void setOnClickListeners() {
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUsers.size() > 0)
                    for (int i = 0; i < mUsers.size(); i++) {
                        if (mUsers.get(i).getUserName().equals(mUserNameText.getText().toString())) {
                            Snackbar.make(mSignUpCoordinatorLayout,
                                    R.string.user_name_exists,
                                    BaseTransientBottomBar.LENGTH_LONG).show();
                            break;
                        } else {
                            if (i == mUsers.size() - 1 &&
                                    !mUsers.get(i).getUserName().equals(mUserNameText.getText().toString())) {
                                UserDBRepository.getInstance(getActivity()).insert(
                                        new User(UUID.randomUUID(), mUserNameText.getText().toString(),
                                                mPasswordText.getText().toString()));
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        signInFragment.newInstance(), "SignInFragment").commit();
                            }
                        }
                    }
                else {
                    UserDBRepository.getInstance(getActivity()).insert(
                            new User(UUID.randomUUID(), mUserNameText.getText().toString(),
                                    mPasswordText.getText().toString()));
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            signInFragment.newInstance(), "SignInFragment").commit();

                }
            }
        });
    }
}