package com.synnlabz.scanme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private TextView txtName,txtEmail,txtGender,txtAge;
    private EditText txtBP,txtS;
    DatabaseReference currentUserDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtAge = (TextView) findViewById(R.id.txtAge);

        txtBP = (EditText) findViewById(R.id.edtxtBP);
        txtS = (EditText) findViewById(R.id.edtxtS);

        String userId = mAuth.getCurrentUser().getUid();
        currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        currentUserDb.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){

                txtName.setText(dataSnapshot.child("name").getValue(String.class));
                txtEmail.setText(dataSnapshot.child("email").getValue(String.class));
                txtGender.setText(dataSnapshot.child("Gender").getValue(String.class));
                txtAge.setText(dataSnapshot.child("Age").getValue(String.class));

                txtBP.setText(dataSnapshot.child("Blood").getValue(String.class));
                txtS.setText(dataSnapshot.child("Suger").getValue(String.class));
            }else{
                Toast.makeText(Profile.this, "Data doesn't exist", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void Update(View view) {
        Map userInfo = new HashMap<>();
        userInfo.put("Blood", txtBP.getText().toString());
        userInfo.put("Suger", txtS.getText().toString());
        currentUserDb.updateChildren(userInfo);
    }

    public void goBack(View view) {
        Intent intent = new Intent(Profile.this, Dashbord.class);
        startActivity(intent);
        finish();
    }

}