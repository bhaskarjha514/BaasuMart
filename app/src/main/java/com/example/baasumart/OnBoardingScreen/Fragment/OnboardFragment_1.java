package com.example.baasumart.OnBoardingScreen.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baasumart.R;


public class OnboardFragment_1 extends Fragment {
    private View view;
    public OnboardFragment_1() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_onboard_1, container, false);
        return view;
    }
}