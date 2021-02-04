package com.example.baasumart.OnBoardingScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baasumart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OnboardingAdapter extends  RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{
    private List<OnboardingItem> onboardingItemsList;

    public OnboardingAdapter(List<OnboardingItem> onboardingItemsList) {
        this.onboardingItemsList = onboardingItemsList;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboarding,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItemsList.size();
    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private TextView textDesc;
        private ImageView imageOnboarding;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            imageOnboarding = itemView.findViewById(R.id.imageOnboarding);
        }
        void setOnboardingData(OnboardingItem onboardingItem){
            textTitle.setText(onboardingItem.getTitle());
            textDesc.setText(onboardingItem.getDesc());
            Picasso.get().load(onboardingItem.getImage()).resize(260,260).centerCrop().into(imageOnboarding);
        }
    }
}
