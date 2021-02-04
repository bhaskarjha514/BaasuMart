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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private Context mContext;
    private List<Product> productsList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ProductAdapter(Context mContext, List<Product> mPost) {
        this.mContext = mContext;
        this.productsList = mPost;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product, parent,false);
        return new ProductAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        setValue(holder, position);
    }

    private void setValue(ProductHolder holder, int position) {
        String userId = FirebaseAuth.getInstance().getUid();
        Product product = productsList.get(position);
        isSaved(product.getProductId(), holder.likeIv);
        Glide.with(mContext).load(product.getImageurl()).into(holder.icon);
        holder.discountTv.setText(product.getDiscount()+"%");
        holder.priceTv.setText("$"+product.getPrice());
        holder.titleTv.setText(product.getProductName());
        holder.ratingTv.setText(product.getRating());
        holder.ratingBar.setRating(Float.parseFloat(product.getRating()));
        holder.likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likeIv.getTag().equals("yes")){
                    FirebaseDatabase.getInstance().getReference("Admins").child("products")
                            .child(product.getProductId()).child("favourite").child(userId).removeValue();
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child("favourite").child(product.getProductId()).removeValue();
                }else{
                    FirebaseDatabase.getInstance().getReference("Admins").child("products")
                            .child(product.getProductId()).child("favourite").child(userId).setValue(true);
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child("favourite").child(product.getProductId()).setValue(true);
                }
            }
        });
    }

    private void isSaved(String productId, ImageView likeIv) {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid)
                .child("favourite");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(productId).exists()){
                    likeIv.setImageResource(R.drawable.ic_like_red);
                    likeIv.setTag("yes");
                }else {
                    likeIv.setImageResource(R.drawable.ic_remove_like);
                    likeIv.setTag("no");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        private TextView discountTv, titleTv, priceTv, ratingTv;
        private ImageView likeIv, icon;
        private RatingBar ratingBar;
        private MaterialCardView materialCardView;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            discountTv = itemView.findViewById(R.id.discountTv);
            likeIv = itemView.findViewById(R.id.likeIv);
            icon = itemView.findViewById(R.id.imageView4);
            titleTv = itemView.findViewById(R.id.textView2);
            priceTv = itemView.findViewById(R.id.textView3);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingTv = itemView.findViewById(R.id.textView4);
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
        }
    }
}
