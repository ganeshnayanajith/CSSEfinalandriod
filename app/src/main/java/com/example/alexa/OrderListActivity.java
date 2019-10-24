package com.example.alexa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alexa.Adapter.OrderAdapter;
import com.example.alexa.Adapter.ProfileAdapter;
import com.example.alexa.Model.Order;
import com.example.alexa.Model.SelectedItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SweetAlertDialog mDialog;
    EditText search;
    ArrayList<Order> OrderList;
    ImageView back;
    Button inventory;

    CardView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        inventory = findViewById(R.id.inventory);


        mDialog = new SweetAlertDialog(OrderListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        recyclerView = findViewById(R.id.orderList);

        back = findViewById(R.id.back);

        search = findViewById(R.id.search);

        profile = findViewById(R.id.profile);

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAdapter adapter = new ProfileAdapter(OrderListActivity.this,OrderListActivity.this);

                adapter.displayProfile();
            }
        });


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderListActivity.this,DashboardActivity.class));
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

        startActivity(new Intent(OrderListActivity.this,DashboardActivity.class));

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
        OrderList = gson.fromJson(response,new TypeToken<List<Order>>(){}.getType());

        if(OrderList.isEmpty()){
            mDialog.dismiss();
        }

        setupRecyclerview(OrderList);
    }
    private void setupRecyclerview(ArrayList<Order> OrderList) {
        OrderAdapter myAdapter = new OrderAdapter(this,OrderList,mDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}