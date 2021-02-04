package com.example.baasumart.BottomNav.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.baasumart.Adapter.ProductAdapter;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String categoryId;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<String> prodIdList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        bindId();
        fetchData();
    }

    private void fetchData() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("product").child("category").child(categoryId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prodIdList.clear();
                for(DataSnapshot snapshot : dataSnapshot.child("products").getChildren()){
                    prodIdList.add(snapshot.getKey());
                }
                readProduct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readProduct() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("products");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(String prodId : prodIdList){
                        if(snapshot.getKey().equals(prodId)){
                            Product product = snapshot.getValue(Product.class);
                            productList.add(product);
                        }
                    }
                }
                productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Product product = productList.get(position);
                        Intent intent = new Intent(CategoryActivity.this, ProductActivity.class);
                        intent.putExtra("productId",product.getProductId());
                        startActivity(intent);
                    }
                });
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bindId() {
        categoryId = getIntent().getStringExtra("title");
        recyclerView = findViewById(R.id.myCartRv);
        recyclerView.setHasFixedSize(true);
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }
}