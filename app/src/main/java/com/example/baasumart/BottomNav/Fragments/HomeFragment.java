package com.example.baasumart.BottomNav.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baasumart.Adapter.CategoryAdapter;
import com.example.baasumart.Adapter.ProductAdapter;
import com.example.baasumart.BottomNav.Activity.CategoryActivity;
import com.example.baasumart.BottomNav.Activity.ProductActivity;
import com.example.baasumart.BottomNav.Activity.ProfileActivity;
import com.example.baasumart.MainActivity;
import com.example.baasumart.Model.Categories;
import com.example.baasumart.Model.Product;
import com.example.baasumart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView catRv, productRv;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Categories> categoriesList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home, container, false);
        bindID();
        readCategory();
        readProductId();
        return  view;
    }

    private void readProductId() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
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

    private void readCategory() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("product").child("category");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Categories categories = snapshot.getValue(Categories.class);
                    categoriesList.add(categories);
                }

                categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getContext(), CategoryActivity.class);
                        Categories categoriesClicked = categoriesList.get(position);
                        intent.putExtra("title", categoriesClicked.getTitle());
                        intent.putExtra("imageurl", categoriesClicked.getIcon());
                        startActivity(intent);
                    }
                });
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bindID() {
        catRv = view.findViewById(R.id.catRV);
        productRv = view.findViewById(R.id.productRV);
        catRv.setHasFixedSize(true);
        productRv.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(getContext(), categoriesList);
        productAdapter = new ProductAdapter(getContext(), productList);
        productRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        catRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        catRv.setAdapter(categoryAdapter);
        productRv.setAdapter(productAdapter);
    }
}