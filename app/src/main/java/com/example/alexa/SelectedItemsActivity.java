package com.example.alexa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexa.Adapter.CategoryAdapter;
import com.example.alexa.Adapter.FetchDataAdapter;
import com.example.alexa.Adapter.OrderTableAdapter;
import com.example.alexa.Adapter.ProfileAdapter;
import com.example.alexa.Adapter.SupplierAdapter;
import com.example.alexa.Model.Order;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.Model.Site;
import com.example.alexa.Model.Supplier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectedItemsActivity extends AppCompatActivity{

    final Calendar myCalendar = Calendar.getInstance();
    RecyclerView recyclerView;
    SweetAlertDialog mDialog;
    ArrayList<SelectedItems> selectedList;
    OrderTableAdapter myAdapter;
    ImageView back;
    ArrayList<Order> orderList;
    String supplierName,supplierId,siteId,siteAddress;
    TextView total,checkout,dateText;
    CardView profile;
    Calendar calendar ;
    private DatePickerDialog.OnDateSetListener date;
    int Year, Month, Day ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_items);


        mDialog = new SweetAlertDialog(SelectedItemsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        recyclerView = findViewById(R.id.selectedList);
        total = findViewById(R.id.total);
        checkout = findViewById(R.id.confirm);
        dateText = findViewById(R.id.date);

        profile = findViewById(R.id.profile);

        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAdapter adapter = new ProfileAdapter(SelectedItemsActivity.this,SelectedItemsActivity.this);

                adapter.displayProfile();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        checkout.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectedItemsActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.setCancelable(false);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        int m = month+1;
                        final String sDate1=day+"/"+m+"/"+year;

                        try {
                            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

                            System.out.println(sDate1);
                            System.out.println(date2);
                            System.out.println(new Date());
                            System.out.println(date2.before(new Date()));


                            if(date2.before(new Date()) && !checkForSameDate(date2,new Date())){
                                final SweetAlertDialog pDialog = new SweetAlertDialog(SelectedItemsActivity.this, SweetAlertDialog.WARNING_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Invalid date");
                                pDialog.setCancelable(false);
                                pDialog.show();
                            }else{
                                if (myAdapter.getTotal() > 100000) {
                                    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(SelectedItemsActivity.this)
                                            .setTitle("Want to order?")
                                            .setMessage("Your order is above the Rs. 100,000 limit.\nOrder should wait for approval\nDelivery date: ("+day+" / "+m+" / "+year+")")
                                            .setCancelable(false)
                                            .setPositiveButton("Confirm", R.drawable.ic_add_shopping_cart_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    SharedPreferences sharedPreferences = getSharedPreferences("order", 0);
                                                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                                    sharedPreferencesEditor.putString("selectedList", null);
                                                    sharedPreferencesEditor.apply();
                                                    Toast.makeText(getApplicationContext(), "Confirmed!", Toast.LENGTH_SHORT).show();
                                                    dialogInterface.dismiss();

                                                    final SweetAlertDialog pDialog = new SweetAlertDialog(SelectedItemsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                    pDialog.setTitleText("Confirming");
                                                    pDialog.setCancelable(false);
                                                    pDialog.show();

                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
                                                    Order order = new Order();

                                                    String key = uniqueId();
                                                    order.setItemList(selectedList);
                                                    order.setDeliveryDate(sDate1);
                                                    order.setEstimatedValue(myAdapter.getTotal());
                                                    order.setOrderStatus("Pending");
                                                    order.setOrderReference(key);
                                                    order.setSupplierCode(supplierId);
                                                    order.setSupplierName(supplierName);
                                                    order.setCompanyCode(siteId);
                                                    order.setDeliveryAddress(siteAddress);
                                                    order.setManagerId(FirebaseAuth.getInstance().getUid());

                                                    reference.child(key).setValue(order);

                                                    new Timer().schedule(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            pDialog.dismiss();
                                                            startActivity(new Intent(SelectedItemsActivity.this, DashboardActivity.class));
                                                            finish();

                                                        }
                                                    }, 2000);
                                                }
                                            })
                                            .setNegativeButton("Cancel", R.drawable.ic_close_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .build();

                                    // Show Dialog
                                    mBottomSheetDialog.show();
                                } else {
                                    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(SelectedItemsActivity.this)
                                            .setTitle("Want to Confirm?")
                                            .setMessage("Your order is Rs. " + myAdapter.getTotal() + " and you want to confirm?\n\nDelivery date: ( "+day+" / "+m+" / "+year+" )")
                                            .setCancelable(false)
                                            .setPositiveButton("Confirm", R.drawable.ic_add_shopping_cart_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                    SharedPreferences sharedPreferences = getSharedPreferences("order", 0);
                                                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                                    sharedPreferencesEditor.putString("selectedList", null);
                                                    sharedPreferencesEditor.apply();
                                                    dialogInterface.dismiss();


                                                    final SweetAlertDialog pDialog = new SweetAlertDialog(SelectedItemsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                    pDialog.setTitleText("Confirming");
                                                    pDialog.setCancelable(false);
                                                    pDialog.show();

                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
                                                    Order order = new Order();

                                                    String key = uniqueId();
                                                    order.setItemList(selectedList);
                                                    order.setDeliveryDate(sDate1);
                                                    order.setEstimatedValue(myAdapter.getTotal());
                                                    order.setOrderStatus("Approved");
                                                    order.setOrderReference(key);
                                                    order.setSupplierCode(supplierId);
                                                    order.setSupplierName(supplierName);
                                                    order.setCompanyCode(siteId);
                                                    order.setDeliveryAddress(siteAddress);
                                                    order.setManagerId(FirebaseAuth.getInstance().getUid());

                                                    reference.child(key).setValue(order);
                                                    new Timer().schedule(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            pDialog.dismiss();
                                                            FetchDataAdapter adapter = new FetchDataAdapter(SelectedItemsActivity.this);
                                                            startActivity(new Intent(SelectedItemsActivity.this, DashboardActivity.class));
                                                            finish();

                                                        }
                                                    }, 1000);

                                                }
                                            })
                                            .setNegativeButton("Cancel", R.drawable.ic_close_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .build();

                                    // Show Dialog
                                    mBottomSheetDialog.show();

                                }
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });



        back = findViewById(R.id.back);
        SharedPreferences sharedPreferences = this.getSharedPreferences("order", 0);
        final Gson gson = new Gson();

        String response= sharedPreferences.getString("selectedList" , "");

        String sites= sharedPreferences.getString("siteList" , "");

        String orders= sharedPreferences.getString("orderList" , "");

        supplierName = sharedPreferences.getString("SupplierName" , "");

        supplierId = sharedPreferences.getString("SupplierId" , "");

        siteId = sharedPreferences.getString("userSite" , "");


        System.out.println(response);
        System.out.println(supplierId);
        System.out.println(supplierName);

        selectedList = gson.fromJson(response,new TypeToken<List<SelectedItems>>(){}.getType());

        ArrayList<Site> siteList = gson.fromJson(sites,new TypeToken<List<Site>>(){}.getType());


        orderList = gson.fromJson(orders,new TypeToken<List<Order>>(){}.getType());


        for(Site site:siteList){
            if (site.getId().equals(siteId)) {
                siteAddress = site.getLocation();
                break;
            }
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("refresh");
                pullToRefresh.setRefreshing(false);
            }
        });



        setupRecyclerview(selectedList);

    }

    private void setupRecyclerview(ArrayList<SelectedItems> selectedList) {
        myAdapter = new OrderTableAdapter(this,selectedList,mDialog,total);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        //total.setText(""+myAdapter.getTotal());
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
    }
    public boolean checkForSameDate(Date date1,Date date2){

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

        return sameDay;
    }


    public  String uniqueId(){
        String pattern = "yy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date1 = simpleDateFormat.format(new Date());


        String timePattern = "hh-mm";
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timePattern);
        String time = simpleTimeFormat.format(new Date());
        String[] time1=time.split("-");

        //Toast.makeText(this,"Time"+date1,Toast.LENGTH_SHORT).show();
        System.out.println(date1);
        String[] date2=date1.split("-");
        StringBuffer date3 = new StringBuffer();
        date3.append("AL");
        date3.append("-");
        date3.append(date2[0]);
        date3.append(date2[1]);
        date3.append(date2[2]);
        date3.append(time1[0]);
        date3.append(time1[1]);
        date3.append("-");
        int random = (int )(Math.random() * 99999 + 10000);
        date3.append(random);
        System.out.println(date3);

        return  date3.toString();

    }

    public boolean checkForNegotiation(){
        for(Order order:orderList){
            if(order.getManagerId().equals(FirebaseAuth.getInstance().getUid())){




            }
        }
        return false;
    }

}
