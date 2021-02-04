package com.example.baasumart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductHolder>{
    private Context mContext;
    private List<Product> productsList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onClick(int position);
        void onDelete(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public CartAdapter(Context mContext, List<Product> mPost) {
        this.mContext = mContext;
        this.productsList = mPost;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart, parent,false);
        return new CartAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        setValue(holder, position);
    }

    private void setValue(ProductHolder holder, int position) {
        String userId = FirebaseAuth.getInstance().getUid();
        Product product = productsList.get(position);
        Glide.with(mContext).load(product.getImageurl()).into(holder.imageView1);
        holder.textView2.setText("$"+product.getPrice());
        holder.textView1.setText(product.getProductName());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        private TextView textView1, textView2;
        private ImageView imageView1, imageView2;
        private MaterialCardView materialCardView;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageView);
            imageView2 = itemView.findViewById(R.id.imageView2);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            materialCardView = itemView.findViewById(R.id.cardCat);
            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onClick(position);
                        }
                    }
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onDelete(position);
                        }
                    }
                }
            });
        }
    }
}
