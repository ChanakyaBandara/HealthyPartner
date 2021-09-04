package com.synnlabz.scanme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class Statistics extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String userId;

    ImageButton previous , next;
    private List pieData;
    private List barData;
    private PieChartView pieChartView;
    private ColumnChartView columnChartView;
    private int Month;
    private TextView monthtxtview;
    String[] month_name = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        previous = (ImageButton)findViewById(R.id.lastMonth);
        next = (ImageButton)findViewById(R.id.nextMonth);
        monthtxtview = (TextView) findViewById(R.id.monthtxt1);

        pieChartView = findViewById(R.id.chart2);
        columnChartView = findViewById(R.id.barchart2);
        pieData = new ArrayList<SliceValue>();
        barData = new ArrayList<Column>();

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Month = Integer.valueOf(dateFormat.format(date));
        update_month();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Records")
                .orderByChild("UserID").equalTo(userId);


        query.addListenerForSingleValueEvent(valueEventListener);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Month>1){
                    Month--;
                    update_month();
                    LoadCharts();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"There are no records",Toast.LENGTH_SHORT).show();
                if(Month<12){
                    Month++;
                    update_month();
                    LoadCharts();
                }
            }
        });
    }

    private void update_month(){
        monthtxtview.setText(month_name[Month-1]);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
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
                    product.setSugar(keyNode.child("ProductSugar").getValue(Float.class));
                    product.setFat(keyNode.child("ProductFat").getValue(Float.class));
                    product.setSalt(keyNode.child("ProductSalt").getValue(Float.class));
                    product.setPrice(Integer.valueOf(keyNode.child("Date").getValue(String.class)));
                    productList.add(product);
                    Keys.add(keyNode.getKey());
                }
                LoadCharts();
                //new Recycleview_Product_config().setConfig(recyclerView,Records.this,productList,Keys);
            }else{
                Toast.makeText(Statistics.this, "No Records!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void LoadCharts() {
        float[][] twoD_arr = new float[12][3];//Month - Sugar/Fat/Salt
        for (Product product : productList){
            int temp = Integer.valueOf((int) product.getPrice());
            if(temp<13 && temp>0){
                twoD_arr[temp-1][0] += product.getSugar();
                twoD_arr[temp-1][1] += product.getFat();
                twoD_arr[temp-1][2] += product.getSalt();
            }
        }
        barData.clear();
        for (int i=0;i<12;i++){
            List<SubcolumnValue> values;
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(Integer.valueOf(Math.round(twoD_arr[i][0])), Color.rgb(255,200,63)));
            values.add(new SubcolumnValue(Integer.valueOf(Math.round(twoD_arr[i][1])), Color.rgb(255,8,12)));
            values.add(new SubcolumnValue(Integer.valueOf(Math.round(twoD_arr[i][2])), Color.rgb(0, 255, 148)));
            barData.add(new Column(values));
        }
        view_barchart();
        pieData.clear();
        String Su = getPercent(twoD_arr,0);
        String Fa = getPercent(twoD_arr,1);
        String Sa = getPercent(twoD_arr,2);
        pieData.add(new SliceValue(Integer.valueOf(Math.round(twoD_arr[Month-1][0])), Color.rgb(255,200,63)).setLabel("Sugar "+Su));
        pieData.add(new SliceValue(Integer.valueOf(Math.round(twoD_arr[Month-1][1])), Color.rgb(255,8,12)).setLabel("Fat "+Fa));
        pieData.add(new SliceValue(Integer.valueOf(Math.round(twoD_arr[Month-1][2])), Color.rgb(0, 255, 148)).setLabel("Salt "+Sa));
        view_piechart();
    }

    private String getPercent(float[][] twoD_arr , int i){
        int tot = Integer.valueOf(Math.round(twoD_arr[Month-1][0] + twoD_arr[Month-1][1] + twoD_arr[Month-1][2]));
        if (tot>0){
            if(i == 0){
                return String.valueOf(Integer.valueOf(Math.round(twoD_arr[Month-1][0]*100/tot)))+"%";
            }else if(i == 1){
                return String.valueOf(Integer.valueOf(Math.round(twoD_arr[Month-1][1]*100/tot)))+"%";
            }else{
                return String.valueOf(Integer.valueOf(Math.round(twoD_arr[Month-1][2]*100/tot)))+"%";
            }
        }else {
            return "0%";
        }

    }

    public void view_piechart(){
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setHasLabelsOutside(true).setValueLabelsTextColor(Color.BLACK);
        pieChartData.setHasCenterCircle(true).setCenterText1(month_name[Month-1]).setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#212A51"));
        pieChartView.setPieChartData(pieChartData);
    }

    public void view_barchart(){

        ColumnChartData columnChartData = new ColumnChartData(barData);
        columnChartView.setColumnChartData(columnChartData);
    }

    public void goBack(View view) {
        Intent intent = new Intent(Statistics.this, Dashbord.class);
        startActivity(intent);
        finish();
    }
}
