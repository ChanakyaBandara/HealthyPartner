package com.synnlabz.scanme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Records extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Product> productList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Query query = FirebaseDatabase.getInstance().getReference().child("Records")
                .orderByChild("UserID").equalTo(userId);

        query.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.d("ABC",dataSnapshot.toString());
                productList.clear();
                List<String> Keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("ABC",keyNode.toString());
                    Log.d("ABC temp",keyNode.getKey());
                    Product product = new Product();
                    product.setName(keyNode.child("ProductName").getValue(String.class));
                    product.setImgURL(keyNode.child("ProductImgURL").getValue(String.class));
                    product.setVeg(keyNode.child("DateTime").getValue(String.class));
                    productList.add(product);
                    Keys.add(keyNode.getKey());
                }
                new Recycleview_Product_config().setConfig(recyclerView,Records.this,productList,Keys);
            }else{
                Toast.makeText(Records.this, "No Records!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void goBack(View view) {
        Intent intent = new Intent(Records.this, Dashbord.class);
        startActivity(intent);
        finish();
    }
}