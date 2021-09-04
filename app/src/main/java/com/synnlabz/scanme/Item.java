package com.synnlabz.scanme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Item extends AppCompatActivity {

    private TextView txtName,txtPrice,txtWeight,txtSugar,txtFat,txtSalt,txtVeg,txtBadge,txtPercentage;
    private ImageView imageView;
    private Product product;
    private String productID;
    private String age,gender;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userId;
    private DatabaseReference currentUserDb;
    private DatabaseReference parametersDb;
    private SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference();

        product = (Product) getIntent().getExtras().getSerializable("Product");
        productID = getIntent().getStringExtra("ProductID");
        setContentView(R.layout.activity_item);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Products/"+productID+".png");
        imageView = (ImageView) findViewById(R.id.imageView);
        txtName = (TextView) findViewById(R.id.txtName);
        txtWeight = (TextView) findViewById(R.id.txtWeight);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtSugar = (TextView) findViewById(R.id.txtSugar);
        txtFat = (TextView) findViewById(R.id.txtFat);
        txtSalt = (TextView) findViewById(R.id.txtSalt);
        txtVeg = (TextView) findViewById(R.id.txtVeg);
        txtBadge = (TextView) findViewById(R.id.txtbadge);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        LoadProduct();


        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.incrementProgressBy(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                //Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
                txtPercentage.setText(progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });


        String userId = mAuth.getCurrentUser().getUid();
        currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        currentUserDb.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){

                age = dataSnapshot.child("Age").getValue(String.class);
                gender = dataSnapshot.child("Gender").getValue(String.class);
                int ageint = Integer.valueOf(age);
                if (gender.equals("male")){
                    gender = "MA";
                }else {
                    gender = "FE";
                }

                if(ageint>59){
                    age = "SE";
                }else if(ageint>18){
                    age = "AU";
                }else if(ageint>12){
                    age = "AD";
                }else {
                    age = "CH";
                }

                parametersDb = FirebaseDatabase.getInstance().getReference().child("Parameters").child(gender).child(age);
                parametersDb.addListenerForSingleValueEvent(valueEventListener1);


            }else{
                Toast.makeText(Item.this, "Data doesn't exist", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void LoadProduct() {
        txtName.setText(product.getName());
        txtPrice.setText("Rs. "+String.valueOf(product.getPrice()));
        txtSugar.setText(String.valueOf(product.getSugar())+"g");
        txtFat.setText(String.valueOf(product.getFat())+"g");
        txtSalt.setText(String.valueOf(product.getSalt())+"g");
        txtVeg.setText(product.getVeg());
        txtWeight.setText(product.getNet());

        Glide.with(this)
                .load(mStorageRef)
                .into(imageView);
    }

    public void gotoLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Item.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToCam(View view) {
        Intent intent = new Intent(Item.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.d("ABC",dataSnapshot.toString());
                String salt = dataSnapshot.child("SA").getValue().toString();
                String sugar = dataSnapshot.child("SU").getValue().toString();
                String fat = dataSnapshot.child("FA").getValue().toString();
                Log.d("ABC", sugar + " - " + salt + " - " + fat);
                if(Double.parseDouble(sugar)<product.getSugar()){
                    txtBadge.setText("Too much Sugar");
                }else if(Double.parseDouble(fat)<product.getFat()){
                    txtBadge.setText("Too much Fat");
                }else if(Double.parseDouble(salt)<product.getSalt()){
                    txtBadge.setText("Too much Salt");
                }else {
                    txtBadge.setText("Healthy");
                }
            }else{
                Toast.makeText(Item.this, "No Records", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void Consume(View view) {
        Map userInfo = new HashMap<>();
        userInfo.put("UserID", userId);
        userInfo.put("ProductID", productID);
        userInfo.put("ProductName", product.getName());
        userInfo.put("ProductImgURL", product.getImgURL());
        userInfo.put("ProductSugar", product.getSugar());
        userInfo.put("ProductFat", product.getFat());
        userInfo.put("ProductSalt", product.getSalt());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        int strMonth = date.getMonth()+1;
        userInfo.put("Date", String.valueOf(strMonth));
        userInfo.put("DateTime", strDate);
        mUserDatabase.child("Records").child(UUID.randomUUID().toString()).updateChildren(userInfo);
        Toast.makeText(Item.this, "Recorded !", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Item.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
