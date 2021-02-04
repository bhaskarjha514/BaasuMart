package com.example.baasumart.BottomNav.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductActivity extends AppCompatActivity {
    private ImageView prodIv;
    private MaterialTextView prodTitle, prodDes,prodPrice;
    private TextView ratingText, discountTv;
    private MaterialCardView addTocart;
    private String prodId, userId;
    private TextView textView;
    private MaterialButton materialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        bindId();
        userId = FirebaseAuth.getInstance().getUid();
        fetchData();
        isOnCart();
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getTag().equals("yes")){
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child("cart").child(prodId).removeValue();
                    isOnCart();
                }else{
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            .child("cart").child(prodId).setValue(true);
                    isOnCart();
                }
            }
        });
    }

    private void isOnCart() {
        try {
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                    .child("cart");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(prodId).exists()){
                        textView.setTag("yes");
                        textView.setText("Already in Cart");
                    }else{
                        textView.setTag("no");
                        textView.setText("Add To Cart");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    private void handleCart() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("cart").child(prodId).exists()){
                    reference.child("cart").child(prodId).setValue(true);
                    textView.setText("Added to cart");
                }else{

                   textView.setText("Already Added");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchData() {
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("products").child(prodId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Product product = dataSnapshot.getValue(Product.class);
                    Glide.with(getApplicationContext()).load(product.getImageurl()).into(prodIv);
                    prodDes.setText(product.getDesc());
                    prodTitle.setText(product.getProductName());
                    prodPrice.setText(product.getPrice());
                    ratingText.setText(product.getRating());
                    discountTv.setText(product.getDiscount()+"%");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bindId() {
        materialButton = findViewById(R.id.buyBtn);
        ImageView backBtn = findViewById(R.id.backBtn);
        textView = findViewById(R.id.text);
        ImageView likeBtn = findViewById(R.id.ic_like);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        prodId = getIntent().getStringExtra("productId");
        prodIv = findViewById(R.id.prodImage);
        prodTitle = findViewById(R.id.productTv);
        prodDes = findViewById(R.id.proddesTv);
        ratingText = findViewById(R.id.textView4);
        prodPrice = findViewById(R.id.prodPrice);
        addTocart = findViewById(R.id.addToCart);
        discountTv = findViewById(R.id.discountTv);
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductActivity.this, prodId, Toast.LENGTH_SHORT).show();
            }
        });
        isSaved(likeBtn,userid);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeBtn.getTag().equals("yes")){
                    FirebaseDatabase.getInstance().getReference("Admins").child("products")
                            .child(prodId).child("favourite").child(userid).removeValue();
                    FirebaseDatabase.getInstance().getReference("Users").child(userid)
                            .child("favourite").child(prodId).removeValue();
                }else{
                    FirebaseDatabase.getInstance().getReference("Admins").child("products")
                            .child(prodId).child("favourite").child(userid).setValue(true);
                    FirebaseDatabase.getInstance().getReference("Users").child(userid)
                            .child("favourite").child(prodId).setValue(true);
                }
            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, LocationActivity.class);
                intent.putExtra("prodId",prodId);
                startActivity(intent);
            }
        });
    }

    private void isSaved(ImageView likeBtn, String userid) {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid)
                .child("favourite");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(prodId).exists()){
                    likeBtn.setImageResource(R.drawable.ic_like_red);
                    likeBtn.setTag("yes");
                }else {
                    likeBtn.setImageResource(R.drawable.ic_remove_like);
                    likeBtn.setTag("no");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}