package com.example.baasumart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baasumart.Model.Categories;
import com.example.baasumart.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{
    private Context mContext;
    private List<Categories> mCategory;
    private OnItemClickListener mListener;

    public CategoryAdapter(Context mContext, List<Categories> mCategory) {
        this.mContext = mContext;
        this.mCategory = mCategory;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categories, parent, false);
        return new CategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
       setData(holder, position);
    }

    private void setData(CategoryHolder holder, int position) {
        Categories categories = mCategory.get(position);
        Glide.with(mContext).load(categories.getIcon()).into(holder.icon);
        if (categories.getTitle() == null){
            holder.title.setVisibility(View.GONE);
        }else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(categories.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView icon;
        CardView cardView;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleCat);
            icon = itemView.findViewById(R.id.iconCat);
            cardView = itemView.findViewById(R.id.cardCat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
