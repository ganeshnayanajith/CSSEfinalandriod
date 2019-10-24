package com.example.alexa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.alexa.Adapter.DashboardOrderAdapter;
import com.example.alexa.Adapter.FetchDataAdapter;
import com.example.alexa.Adapter.ProfileAdapter;
import com.example.alexa.Model.Order;
import com.example.alexa.Model.SelectedItems;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<Order> OrderList;
    RecyclerView recyclerView;

    CardView orderNow,pendingOrders,order,graphCard;
    CardView profile;
    ProfileAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextClock dateClock = findViewById(R.id.dateClock);
        TextClock timeClock = findViewById(R.id.timeClock);
        TextClock hourClock = findViewById(R.id.hourClock);

        dateClock.setFormat12Hour(null);
        dateClock.setFormat24Hour("MMM dd");

        hourClock.setFormat12Hour("hh");
        hourClock.setFormat24Hour(null);

        timeClock.setFormat24Hour(null);
        timeClock.setFormat12Hour("mm:ss");

        profile = findViewById(R.id.profile);

        FetchDataAdapter data = new FetchDataAdapter(this);

        orderNow = findViewById(R.id.order);

        pendingOrders = findViewById(R.id.pendingOrders);

        order = findViewById(R.id.finishedOrders);

        recyclerView = findViewById(R.id.orders);

        graphCard = findViewById(R.id.graphCard);


        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,SupplierListActivity.class));
            }
        });

        pendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,PendingOrderActivity.class));
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,OrderListActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new ProfileAdapter(DashboardActivity.this, DashboardActivity.this);

                adapter.displayProfile();
            }
        });

        try {
            displayOrderList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {

    }

    private void displayOrderList() throws ParseException {
        OrderList = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getSharedPreferences("order", 0);
        final Gson gson = new Gson();

        String response= sharedPreferences.getString("orderList" , "");
        System.out.println(response);
        ArrayList<Order>  orders = gson.fromJson(response,new TypeToken<List<Order>>(){}.getType());

        for (Order order:orders){
            if(order.getOrderStatus().equals("Approved")){
                OrderList.add(order);
            }
        }
//        OrderList.add(new Order());
//        OrderList.add(new Order());
//        OrderList.add(new Order());
        GraphView graph = (GraphView) findViewById(R.id.graph);

        int count = 0;
        for (Order order:orders) {

            if(order.getOrderStatus().equals("Received")) {
                count++;
            }

        }
        double total = 0;
        int index = 1;
        System.out.println("Count "+count);
        DataPoint[] dataPoints = new DataPoint[count+1];

        dataPoints[0] = new DataPoint(0, 0);
        for (Order order:orders) {

            if(order.getOrderStatus().equals("Received")) {
                total += order.getEstimatedValue();
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(order.getDeliveryDate());
                dataPoints[index] = new DataPoint(index++, total);
            }

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 50),
//                new DataPoint(4, 6),
//                new DataPoint(5, 1),
//                new DataPoint(6, 5),
//                new DataPoint(7, 3),
//                new DataPoint(8, 2),
//                new DataPoint(9, 6)
//        });
        graph.setTitle("Order History");
        graph.setTitleTextSize(50f);
        graph.setTitleColor(R.color.colorPrimaryDark);
        graph.addSeries(series);

        if(OrderList.isEmpty()){
            graphCard.setVisibility(View.VISIBLE);
        }else {
            graphCard.setVisibility(View.GONE);
        }

        setupRecyclerview(OrderList);
    }

    private void setupRecyclerview(ArrayList<Order> orderList) {


        DashboardOrderAdapter adapter = new DashboardOrderAdapter(this, orderList,graphCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

    }


}
