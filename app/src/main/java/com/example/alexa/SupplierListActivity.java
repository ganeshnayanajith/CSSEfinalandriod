package com.example.alexa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexa.Adapter.CategoryAdapter;
import com.example.alexa.Adapter.FetchDataAdapter;
import com.example.alexa.Adapter.ProfileAdapter;
import com.example.alexa.Adapter.SupplierAdapter;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.Model.Supplier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SupplierListActivity extends AppCompatActivity {

    MaterialSpinner category;
    RecyclerView recyclerView,categoryList;
    SweetAlertDialog mDialog;
    EditText search;
    ArrayList<Supplier> SupplierList;
    CategoryAdapter recyclerAdapter;
    ImageView back;
    Dialog myDialog;
    CardView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_list);

        mDialog = new SweetAlertDialog(SupplierListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

//        category = findViewById(R.id.category);
        recyclerView = findViewById(R.id.supplierList);
        categoryList = findViewById(R.id.catList);

        back = findViewById(R.id.back);

        search = findViewById(R.id.search);

        profile = findViewById(R.id.profile);



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAdapter adapter = new ProfileAdapter(SupplierListActivity.this,SupplierListActivity.this);

                adapter.displayProfile();
            }
        });


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupplierListActivity.this,DashboardActivity.class));
            }
        });


//        category.setItems("All","Tools","Construction","Pipe","Paint");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterSuppliers(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displaySupplierList(); // your code
                search.setText(null);
                System.out.println("refresh");
                pullToRefresh.setRefreshing(false);
            }
        });

//        category.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                filterCategories(item.toString());
//                cat = item.toString();
//            }
//        });

        displaySupplierList();
        setCategories();


    }

    private void setCategories() {


        recyclerAdapter = new CategoryAdapter(this, new ArrayList<>(Arrays.asList("All","Tools","Construction","Pipe","Paint")),"All",SupplierList, recyclerView, mDialog);
        categoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryList.setAdapter(recyclerAdapter);

    }


    private void filterSuppliers(CharSequence filter) {
        int size= filter.length();
        ArrayList<Supplier> selectedSupplierList = new ArrayList<>();

        for(Supplier supplier:SupplierList){
            System.out.println(supplier.getSupplierName().substring(0,size));

            if(recyclerAdapter.getCategory().equals("All")) {
                if(supplier.getSupplierName().toLowerCase().contains(filter.toString().toLowerCase())){
                    System.out.println(filter);

                    selectedSupplierList.add(supplier);
                }
            }else if(supplier.getSupplierCategories().contains(recyclerAdapter.getCategory())){
                if(supplier.getSupplierName().toLowerCase().contains(filter.toString().toLowerCase())){
                    System.out.println(filter);

                    selectedSupplierList.add(supplier);
                }
            }

        }
        setupRecyclerview(selectedSupplierList);

    }

    private void displaySupplierList() {
        SupplierList = new ArrayList<>();
        ArrayList<Supplier> selectedSupplierList = new ArrayList<>();



        SharedPreferences sharedPreferences = this.getSharedPreferences("order", 0);
        final Gson gson = new Gson();

        String response= sharedPreferences.getString("supplierList" , "");
        System.out.println(response);
        selectedSupplierList = gson.fromJson(response,new TypeToken<List<Supplier>>(){}.getType());
        SupplierList = gson.fromJson(response,new TypeToken<List<Supplier>>(){}.getType());


        if(SupplierList.isEmpty()){
            mDialog.dismiss();
        }
        setupRecyclerview(SupplierList);
    }
    private void setupRecyclerview(ArrayList<Supplier> SupplierList) {
        SupplierAdapter myAdapter = new SupplierAdapter(this,SupplierList,mDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
