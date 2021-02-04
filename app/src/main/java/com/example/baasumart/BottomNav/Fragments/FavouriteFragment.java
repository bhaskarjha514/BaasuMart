package com.example.baasumart.BottomNav.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baasumart.Adapter.ProductAdapter;
import com.example.baasumart.BottomNav.Activity.CategoryActivity;
import com.example.baasumart.BottomNav.Activity.ProductActivity;
import com.example.baasumart.BottomNav.Activity.ProfileActivity;
import com.example.baasumart.Model.Categories;
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

public class FavouriteFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<String> productIdList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        bindId();
        fetchFav();
        return view;
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
                        Intent intent = new Intent(getContext(), ProductActivity.class);
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
            recyclerView = view.findViewById(R.id.productRV);
            recyclerView.setHasFixedSize(true);
            productAdapter = new ProductAdapter(getContext(), productList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(productAdapter);
    }
}