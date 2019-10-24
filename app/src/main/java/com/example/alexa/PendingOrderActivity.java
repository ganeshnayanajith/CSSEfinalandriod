package com.example.alexa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alexa.Adapter.CategoryAdapter;
import com.example.alexa.Adapter.ProfileAdapter;
import com.example.alexa.Adapter.PendingOrderAdapter;
import com.example.alexa.Model.Order;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.Model.Supplier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PendingOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SweetAlertDialog mDialog;
    EditText search;
    ArrayList<Order> OrderList;
    ImageView back;
    CardView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);


        mDialog = new SweetAlertDialog(PendingOrderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        recyclerView = findViewById(R.id.pendingList);

        back = findViewById(R.id.back);

        search = findViewById(R.id.search);

        profile = findViewById(R.id.profile);



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAdapter adapter = new ProfileAdapter(PendingOrderActivity.this,PendingOrderActivity.this);

                adapter.displayProfile();
            }
        });


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PendingOrderActivity.this,DashboardActivity.class));
            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                filterOrders(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayOrderList(); // your code
//                search.setText(null);
                System.out.println("refresh");
                pullToRefresh.setRefreshing(false);
            }
        });

        displayOrderList();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PendingOrderActivity.this,DashboardActivity.class));

    }

    private void filterOrders(CharSequence filter) {
        int size= filter.length();
        ArrayList<Order> selectedOrderList = new ArrayList<>();

        for(Order order:OrderList){
                if(order.getSupplierName().toLowerCase().contains(filter.toString().toLowerCase())){
                    System.out.println(filter);

                    selectedOrderList.add(order);
                }
        }
        setupRecyclerview(selectedOrderList);
    }

    private void displayOrderList() {
        OrderList = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getSharedPreferences("order", 0);
        final Gson gson = new Gson();

        String response= sharedPreferences.getString("orderList" , "");
        System.out.println(response);
        ArrayList<Order> orders = gson.fromJson(response,new TypeToken<List<Order>>(){}.getType());


        for(Order order:orders){
            if(order.getOrderStatus().equals("Pending")){
                OrderList.add(order);
            }
        }

        if(OrderList.isEmpty()){
            mDialog.dismiss();
        }

        setupRecyclerview(OrderList);
    }
    private void setupRecyclerview(ArrayList<Order> OrderList) {
        PendingOrderAdapter myAdapter = new PendingOrderAdapter(this,OrderList,mDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
