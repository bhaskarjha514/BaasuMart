package com.example.baasumart.OnBoardingScreen.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baasumart.LoginActivity;
import com.example.baasumart.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class OnboardFragment_3 extends Fragment {
    private View view;
    private FloatingActionButton floatingActionButton;
    public OnboardFragment_3() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_onboard_3, container, false);
        floatingActionButton = view.findViewById(R.id.fabNext);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return view;
    }
}