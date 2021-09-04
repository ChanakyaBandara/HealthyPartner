package com.synnlabz.scanme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Dashbord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
    }

    public void gotoLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Dashbord.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(Dashbord.this, Profile.class);
        startActivity(intent);
    }

    public void goToStatistics(View view) {
        Intent intent = new Intent(Dashbord.this, Statistics.class);
        startActivity(intent);
    }

    public void goToRecord(View view) {
        Intent intent = new Intent(Dashbord.this, Records.class);
        startActivity(intent);
    }

    public void goToScan(View view) {
        Intent intent = new Intent(Dashbord.this, MainActivity.class);
        startActivity(intent);
    }


}