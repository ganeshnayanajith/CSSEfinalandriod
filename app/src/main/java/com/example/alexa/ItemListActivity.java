package com.example.alexa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexa.Adapter.ItemsAdapter;
import com.example.alexa.Adapter.ProfileAdapter;
import com.example.alexa.Adapter.SupplierAdapter;
import com.example.alexa.Model.Item;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.Model.Supplier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SweetAlertDialog mDialog;
    Dialog myDialog,MYDialog;
    EditText search;
    ImageView back;
    ArrayList<Item> ItemsList;
    Button next;
    CardView profile;

    ItemsAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mDialog = new SweetAlertDialog(ItemListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
        recyclerView = findViewById(R.id.itemList);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        search = findViewById(R.id.search);
        next = findViewById(R.id.selected);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileAdapter adapter = new ProfileAdapter(ItemListActivity.this,ItemListActivity.this);

                adapter.displayProfile();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search.setText(null);

                ArrayList<SelectedItems> list = myAdapter.getSelectedItems();

                if(list.isEmpty()){
                    final SweetAlertDialog pDialog = new SweetAlertDialog(ItemListActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Please select items");
                    pDialog.setCancelable(false);
                    pDialog.show();
//                    Toast.makeText(ItemListActivity.this, "Please select items", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences("order", 0);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    final Gson gson = new Gson();
                    String json = gson.toJson(list);
                    System.out.println(json);
                    sharedPreferencesEditor.putString("selectedList", json);
                    sharedPreferencesEditor.apply();
                    startActivity(new Intent(ItemListActivity.this, SelectedItemsActivity.class));
                }
            }
        });
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
                //displayItemsList();
                search.setText(null);
                System.out.println("refresh");
                pullToRefresh.setRefreshing(false);
            }
        });
        displayItemsList();
    }

    private void filterSuppliers(CharSequence filter) {
        int size= filter.length();
        ArrayList<Item> selectedSupplierList = new ArrayList<>();

        for(Item item:ItemsList){
            System.out.println(item.getItemName().substring(0,size));
                if(item.getItemName().toLowerCase().contains(filter.toString().toLowerCase())){
                    System.out.println(filter);

                    selectedSupplierList.add(item);
                }
        }
        setupRecyclerview(selectedSupplierList);

    }

    private void displayItemsList() {
        ItemsList = new ArrayList<>();
        ArrayList<Item> selectedItemsList = new ArrayList<>();


        SharedPreferences sharedPreferences = this.getSharedPreferences("order", 0);
        final Gson gson = new Gson();

        String supplierId= sharedPreferences.getString("SupplierId" , "");

        String response = sharedPreferences.getString(supplierId,"");
        System.out.println(response);
        ItemsList = gson.fromJson(response,new TypeToken<List<Item>>(){}.getType());
        selectedItemsList = gson.fromJson(response,new TypeToken<List<Item>>(){}.getType());


        if(ItemsList==null || ItemsList.isEmpty()){
            mDialog.dismiss();
            final SweetAlertDialog pDialog = new SweetAlertDialog(ItemListActivity.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("No Item");
            pDialog.setCancelable(false);
            pDialog.show();

            pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    startActivity(new Intent(ItemListActivity.this,SupplierListActivity.class));
                }
            });


        }else {

            setupRecyclerview(ItemsList);
        }
    }
    private void setupRecyclerview(ArrayList<Item> itemList) {

        myAdapter = new ItemsAdapter(this,itemList,mDialog);

        final double[] dx = {0};

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {

            ArrayList<String> quarters = new ArrayList<>(Arrays.asList("Full","Quarter","Half","Three Quarters"));

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                System.out.println(dX);
                super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive);

                dx[0] = dX;

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                System.out.println("onMove");

                return false;


            }


            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = myAdapter.getmData().get(viewHolder.getAdapterPosition()).getIndex();

                System.out.println("onSwiped "+position);
                System.out.println("onSwiped "+viewHolder.getAdapterPosition());

                if (direction == ItemTouchHelper.RIGHT && dx[0] == 1080) {

                    myDialog = new Dialog(ItemListActivity.this);
                    final int[] index = {0};
                    ImageView add,sub,addCubic,subCubic;
                    final EditText qnt;
                    final TextView quantityCubic;

                    TextView cancel, select;
                    myDialog.setContentView(R.layout.item_quantity);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCancelable(true);
                    addCubic = myDialog.findViewById(R.id.addCubic);
                    subCubic = myDialog.findViewById(R.id.subCubic);
                    quantityCubic = myDialog.findViewById(R.id.quantityCubic);
                    add = myDialog.findViewById(R.id.add);
                    sub = myDialog.findViewById(R.id.sub);
                    qnt = myDialog.findViewById(R.id.quantity);
                    cancel = myDialog.findViewById(R.id.cancel);
                    quantityCubic.setText(quarters.get(index[0]));

                    myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            myAdapter.restoreItem(viewHolder.getAdapterPosition());
                        }
                    });
                    if(ItemsList.get(position).getItemUnit().equals("piece")) {

                        LinearLayout layout = myDialog.findViewById(R.id.cubic);

                        layout.setVisibility(View.GONE);

                    }

                    if(ItemsList.get(position).isChecked()){
                        qnt.setText(""+(int) Math.floor(ItemsList.get(position).getQuantity()));
                        Double x = ItemsList.get(position).getQuantity() - Math.floor(ItemsList.get(position).getQuantity());
                        if(ItemsList.get(position).getItemUnit().equals("cubic")) {
                            quantityCubic.setText(quarters.get((int) Math.floor(x * 4)));
                        }
                    }else if(ItemsList.get(position).getQuantity()!=0.0){
                        qnt.setText(""+(int) Math.floor(ItemsList.get(position).getQuantity()));
                        Double x = ItemsList.get(position).getQuantity() - Math.floor(ItemsList.get(position).getQuantity());
                        if(ItemsList.get(position).getItemUnit().equals("cubic")) {
                            quantityCubic.setText(quarters.get((int) Math.floor(x * 4)));
                        }
                    }
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String x = qnt.getText().toString();
                            int y = Integer.parseInt(x)+1;
                            qnt.setText(String.valueOf(y));
                            System.out.println(y);
                        }
                    });

                    sub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String x = qnt.getText().toString();
                            int y = Integer.parseInt(x)-1;
                            if(ItemsList.get(position).getItemUnit().equals("piece")) {
                                if (y >= 1) {
                                    qnt.setText(String.valueOf(y));
                                }
                            }else{
                                if(index[0] != 0) {
                                    if (y >= 0) {
                                        qnt.setText(String.valueOf(y));
                                    }
                                }else{
                                    if (y >= 1) {
                                        qnt.setText(String.valueOf(y));
                                    }
                                }
                            }
                            System.out.println(y);
                        }
                    });

                    addCubic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index[0] += 1;

                            if(index[0]==0){
                                quantityCubic.setText(quarters.get(0));
                                if(qnt.getText().toString().equals("0")){
                                    qnt.setText("1");
                                }
                            }else if(index[0]==1){
                                quantityCubic.setText(quarters.get(1));
                            }else if(index[0]==2){
                                quantityCubic.setText(quarters.get(2));
                            }else if(index[0]==3){
                                quantityCubic.setText(quarters.get(3));
                            }else{
                                quantityCubic.setText(quarters.get(0));
                                index[0] = 0;
                                if(qnt.getText().toString().equals("0")){
                                    qnt.setText("1");
                                }
                            }

                        }
                    });

                    subCubic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index[0] -= 1;

                            if(index[0]==0){
                                quantityCubic.setText(quarters.get(0));
                                if(qnt.getText().toString().equals("0")){
                                    qnt.setText("1");
                                }
                            }else if(index[0]==1){
                                quantityCubic.setText(quarters.get(1));
                            }else if(index[0]==2){
                                quantityCubic.setText(quarters.get(2));
                            }else if(index[0]==3){
                                quantityCubic.setText(quarters.get(3));
                            }else{
                                quantityCubic.setText(quarters.get(0));
                                index[0] = 4;
                                if(qnt.getText().toString().equals("0")){
                                    qnt.setText("1");
                                }
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            myAdapter.restoreItem(viewHolder.getAdapterPosition());

                            myDialog.dismiss();
                        }
                    });
                    select = myDialog.findViewById(R.id.select);
                    select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Double value;
                            if(ItemsList.get(position).getItemUnit().equals("piece")){

                                value =  Double.parseDouble(qnt.getText().toString());

                            }else{


                                if(index[0] == 4){

                                    value = Double.parseDouble(qnt.getText().toString());

                                }else{

                                    value = Double.parseDouble(qnt.getText().toString())+0.25*index[0];

                                }


                            }
                            System.out.println(value);

                            ItemsList.get(position).setQuantity(value);
                            ItemsList.get(position).setChecked(true);
                            myAdapter.restoreItem(viewHolder.getAdapterPosition());

                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();


                }else if(direction == ItemTouchHelper.LEFT && dx[0] == -1080){

                    ItemsList.get(position).setChecked(false);

                    myAdapter.notifyItemChanged(position);

//                    System.out.println("sddddddddddddddddddddddddddddddddddddddddddddddddddd");
//                    ItemsList.get(viewHolder.getAdapterPosition()).setChecked(false);
//                    final int sdk = Build.VERSION.SDK_INT;
//                    if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                        viewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(ItemListActivity.this, R.drawable.item_background) );
//                    } else {
//                        viewHolder.itemView.setBackground(ContextCompat.getDrawable(ItemListActivity.this, R.drawable.item_background));
//
//                    }
//                    if(myDialog != null) {
//                        myDialog.dismiss();
//                    }
                }



            }

        }).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onBackPressed() {
        if(search.getText().toString().isEmpty()) {
            super.onBackPressed();
        }else{
            search.setText(null);
        }
    }
}
