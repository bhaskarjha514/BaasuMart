package com.example.baasumart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.baasumart.OnBoardingScreen.Fragment.OnboardFragment_1;
import com.example.baasumart.OnBoardingScreen.Fragment.OnboardFragment_2;
import com.example.baasumart.OnBoardingScreen.Fragment.OnboardFragment_3;

public class SplashActivity extends AppCompatActivity {
    ImageView logo, splashImg;
    TextView logoText;
    LottieAnimationView lottieAnimationView;
    private static final int NUM_PAGES = 3;
    private ViewPager viewPager;
    private ScreenSlidePageAdapter pageAdapter;
    private Animation animation;
    private static final int SPLASH_TIMEOUT = 3000;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bindID();
        splashImg.animate().translationY(-3200).setDuration(1000).setStartDelay(2500);
        logo.animate().translationY(2500).setDuration(1000).setStartDelay(2500);
        logoText.animate().translationY(1900).setDuration(1000).setStartDelay(2500);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("sharedPref",MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);
                if(isFirstTime){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime",false);
                    editor.apply();
                    pageAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
                    viewPager.setAdapter(pageAdapter);
                    animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.onboarding_fadin);
                    viewPager.startAnimation(animation);
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        },SPLASH_TIMEOUT);


    }

    private void bindID() {
        logo = findViewById(R.id.logo);
        splashImg = findViewById(R.id.splash_bg);
        logoText = findViewById(R.id.logo_title);
        lottieAnimationView = findViewById(R.id.lottie);
        viewPager = findViewById(R.id.pager);

    }
    private class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnboardFragment_1 tab1 = new OnboardFragment_1();
                    return tab1;
                case 1:
                    OnboardFragment_2 tab2 = new OnboardFragment_2();
                    return tab2;
                case 2:
                    OnboardFragment_3 tab3 = new OnboardFragment_3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}