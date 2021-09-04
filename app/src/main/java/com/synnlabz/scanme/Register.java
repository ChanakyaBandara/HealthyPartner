package com.synnlabz.scanme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText mEmail, mPassword, mName, mAge, mGender, mSuger, mBlood, mVeg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //mAge, mGender, mSuger, mBlood, mVeg
        mName = (EditText) findViewById(R.id.Name);
        mEmail = (EditText) findViewById(R.id.Email);
        mPassword = (EditText) findViewById(R.id.password);
        mAge = (EditText) findViewById(R.id.age);
        mGender = (EditText) findViewById(R.id.gender);
        mSuger = (EditText) findViewById(R.id.sugar);
        mBlood = (EditText) findViewById(R.id.blood);
        mVeg = (EditText) findViewById(R.id.veg);

        mAuth = FirebaseAuth.getInstance();

    }


    public void register(View view) {


        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        final String name = mName.getText().toString();
        final String Age = mAge.getText().toString();
        final String Gender = mGender.getText().toString();
        final String Suger = mSuger.getText().toString();
        final String Blood = mBlood.getText().toString();
        final String veg = mVeg.getText().toString();
        if (email.equals("") || password.equals("") || name.equals("")) {  //Validation
            Toast.makeText(getApplicationContext(), "Please fill the empty fields", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("email", email);
                                userInfo.put("name", name);
                                userInfo.put("Age", Age);
                                userInfo.put("Gender", Gender);
                                userInfo.put("Suger", Suger);
                                userInfo.put("Blood", Blood);
                                userInfo.put("veg", veg);

                                currentUserDb.updateChildren(userInfo);
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this, "Error :" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}
