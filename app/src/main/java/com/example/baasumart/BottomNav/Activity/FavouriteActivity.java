package com.example.baasumart.BottomNav.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.baasumart.Adapter.ProductAdapter;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> productIdList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        bindId();
        fetchFav();
    }
    private void fetchFav() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid())
                .child("favourite");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productIdList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    productIdList.add(snapshot.getKey());
                }
                readProductByID();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readProductByID() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(String productId : productIdList){
                        if(snapshot.getKey().equals(productId)){
                            Product product = snapshot.getValue(Product.class);
                            productList.add(product);
                        }
                    }
                }
                productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Product product = productList.get(position);
                        Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                        intent.putExtra("title", product.getCategory());
                        intent.putExtra("productId", product.getProductId());
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
        recyclerView = findViewById(R.id.productRV);
        recyclerView.setHasFixedSize(true);
        productAdapter = new ProductAdapter(getApplicationContext(), productList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(productAdapter);
    }
}