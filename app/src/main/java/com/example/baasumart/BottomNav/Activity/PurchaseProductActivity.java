package com.example.baasumart.BottomNav.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PurchaseProductActivity extends AppCompatActivity {
    String prodId, country, city, state, fullAddress;
    double latitude, longtitude;
    private ImageView prodIV;
    private MaterialTextView fullAddTv, prodPrice;
    private MaterialButton confirmOrder;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);
        bindId();
        bindData();

        isDeliveryOnProgress();
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlingConfirmButton();
                isDeliveryOnProgress();
            }
        });
    }

    private void handlingConfirmButton() {
        if(confirmOrder.getText().toString().equals("Cancel")){
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            DatabaseReference adminOrder = FirebaseDatabase.getInstance().getReference("Admins").child("orders")
                    .child(userid);
            adminOrder.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.child("prodId").getValue().toString().equals(prodId)){
                            FirebaseDatabase.getInstance().getReference("Admins").child("orders").child(userid).child(snapshot.getKey()).removeValue();
                            FirebaseDatabase.getInstance().getReference("Users").child(userid).child("orders").child(snapshot.getKey()).removeValue();
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }else if(confirmOrder.getText().toString().equals("Confirm")){
            Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();
            DatabaseReference adminOrder2 = FirebaseDatabase.getInstance().getReference("Admins").child("orders")
                    .child(userid);
            String orderId = adminOrder2.push().getKey();
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            String strDate = dateFormat.format(date).toString();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("prodId", prodId);
            hashMap.put("orderId", orderId);
            hashMap.put("date", strDate);
            hashMap.put("userId", userid);
            hashMap.put("isPlaced", false);
            adminOrder2.child(orderId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference("Users").child(userid)
                            .child("orders").child(orderId).setValue(true);
                }
            });
        }
    }

    private void handleConfirmBtn() {
        if(confirmOrder.getText().equals("Cancel")){
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            DatabaseReference adminOrder = FirebaseDatabase.getInstance().getReference("Admins").child("orders")
                    .child(userid);
            adminOrder.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.child("prodId").getValue().equals(prodId)){
                            String orderId = snapshot.getKey();
                            FirebaseDatabase.getInstance().getReference("Users").child(userid)
                                    .child("orders").child(snapshot.getKey()).removeValue();
                            adminOrder.child(snapshot.getKey()).removeValue();
                            Toast.makeText(PurchaseProductActivity.this, "Called", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

        }else {
            Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins")
                    .child("orders").child(userid);
            String orderId = reference.push().getKey();
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            String strDate = dateFormat.format(date).toString();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("prodId", prodId);
            hashMap.put("orderId", orderId);
            hashMap.put("date", strDate);
            hashMap.put("userId", userid);
            hashMap.put("isPlaced", false);
            reference.child(orderId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseDatabase.getInstance().getReference("Users").child(userid)
                            .child("orders").child(orderId).setValue(true);
                }
            });
        }

    }

    private void isDeliveryOnProgress() {
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Admins").child("orders");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(userid).exists()){
                        for(DataSnapshot snapshot : dataSnapshot.child(userid).getChildren()){
                            Log.d("Snapshot getting",snapshot.getKey());
                            if(snapshot.child("prodId").getValue().equals(prodId)){
                                confirmOrder.setText("Cancel");
                                confirmOrder.setTag("yes");
                                break;
                            }else{
                                confirmOrder.setText("Confirm");
                                confirmOrder.setTag("no");
                            }
                        }
                    }else{
                        Log.d("yeelse","yes else");
                        confirmOrder.setText("Confirm");
                        confirmOrder.setTag("no");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }catch (Exception e){e.printStackTrace();}
    }

    private void bindData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("products").child(prodId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                Glide.with(getApplicationContext()).load(product.getImageurl()).into(prodIV);
                prodPrice.setText("$"+product.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bindId() {
        prodIV = findViewById(R.id.prodIV);
        fullAddTv = findViewById(R.id.fullAddress);
        prodPrice = findViewById(R.id.prodPrice);
        confirmOrder = findViewById(R.id.confirm_button);
        prodId = getIntent().getStringExtra("prodId");
        country = getIntent().getStringExtra("country");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        fullAddress = getIntent().getStringExtra("fullAddress");
        latitude = getIntent().getDoubleExtra("latitude",latitude);
        longtitude = getIntent().getDoubleExtra("longtitude",longtitude);
        fullAddTv.setText(fullAddress);
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}