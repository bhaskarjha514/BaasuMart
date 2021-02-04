package com.example.baasumart.BottomNav.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.baasumart.Adapter.CartAdapter;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    List<Product> productList = new ArrayList<>();
    private ImageView backBtn;
    private MaterialButton checkOut;
    private MaterialTextView price;
    private List<String> prodIdList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private double priceDouble=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bindID();
        fetchCart();
    }

    private void fetchCart() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("cart");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    prodIdList.add(key);
                }
                fetchProd();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void fetchProd() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for(String key: prodIdList){
                        if(snapshot.getKey().equals(key)){
                            Product product = snapshot.getValue(Product.class);
                            priceDouble += Double.parseDouble(product.getPrice());
                            productList.add(product);
                        }
                    }
                }
                cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Product product = productList.get(position);
                        Intent in = new Intent(CartActivity.this, ProductActivity.class);
                        in.putExtra("productId",product.getProductId());
                        startActivity(in);
                    }

                    @Override
                    public void onDelete(int position) {
                        removeItem(position);
                    }
                });
                cartAdapter.notifyDataSetChanged();
                price.setText("$"+String.valueOf(priceDouble));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void removeItem(int position) {
        final Product product = productList.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("cart")
                .child(product.getProductId());
        databaseReference.removeValue();
    }

    private void bindID() {
        backBtn = findViewById(R.id.backBtn);
        checkOut = findViewById(R.id.checkOut);
        price = findViewById(R.id.price);
        recyclerView = findViewById(R.id.cartRv);
        recyclerView.setHasFixedSize(true);
        cartAdapter = new CartAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
    }
}