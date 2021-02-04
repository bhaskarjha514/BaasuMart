package com.example.baasumart.OnBoardingScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.baasumart.MainActivity;
import com.example.baasumart.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingScreen extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout linearLayout;
    private MaterialButton materialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        linearLayout = findViewById(R.id.layoutIndicator);
        materialButton = findViewById(R.id.buttonOnboarding);
        setupOnboardingItem();
        ViewPager2 viewPager2 = findViewById(R.id.onboardingViewPager);
        viewPager2.setAdapter(onboardingAdapter);
        setupIndicator();
        setCurrentIndicator(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager2.getCurrentItem()+1<onboardingAdapter.getItemCount()){
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
                }else {
                    startActivity(new Intent(OnBoardingScreen.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
    private void setupOnboardingItem(){
        List<OnboardingItem> onboardingItemsList = new ArrayList<>();
        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle(getResources().getString(R.string.title_1));
        item1.setDesc(getResources().getString(R.string.desc_1));
        item1.setImage(R.drawable.ig1);

        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle(getResources().getString(R.string.title_2));
        item2.setDesc(getResources().getString(R.string.desc_2));

        item2.setImage(R.drawable.ig2);

        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle(getResources().getString(R.string.title_3));
        item3.setDesc(getResources().getString(R.string.desc_3));
        item3.setImage(R.drawable.img3);

        onboardingItemsList.add(item1);
        onboardingItemsList.add(item2);
        onboardingItemsList.add(item3);

        onboardingAdapter = new OnboardingAdapter(onboardingItemsList);
    }
    private void setupIndicator(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            linearLayout.addView(indicators[i]);
        }
    }
    private void setCurrentIndicator(int index){
        int childCount = linearLayout.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            }
        }
        if(index == onboardingAdapter.getItemCount()-1){
            materialButton.setText("Start");
        }else{
            materialButton.setText("Next");
        }
    }
}