package com.example.fitness.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.R;
import com.example.fitness.helpers.Session;
import com.example.fitness.activities.LoginActivity;
import com.example.fitness.activities.MainActivity;


public class SettingFragment extends Fragment {
    String username;
    Session session;
    RecyclerView.Adapter adapter;


    public SettingFragment(Session session) {
        this.session = session;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.setting, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.recordSetting("setting", adapter);

        //get the username from main activity
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        TextView title = view.findViewById(R.id.setting_text);
        title.setText("Hi " + username + "! This is setting");

        //finish activity and go back to login activity when the "log out" button is clicked
        Button logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoggedin(false);
                getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
