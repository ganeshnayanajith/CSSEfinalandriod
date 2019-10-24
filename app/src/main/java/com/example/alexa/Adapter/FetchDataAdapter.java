package com.example.alexa.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alexa.Model.Item;
import com.example.alexa.Model.Order;
import com.example.alexa.Model.Site;
import com.example.alexa.Model.Supplier;
import com.example.alexa.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class FetchDataAdapter {


    Context context;
    FirebaseDatabase database;
    public FetchDataAdapter(Context context) {

        database = FirebaseDatabase.getInstance();
        this.context = context;
        getData();

    }

    private void getData(){

        getSuppliers();
        getItems();
        getOrders();
        getUsers();
        getSite();
    }

    private void getSite() {
        DatabaseReference siteRef = database.getReference("Sites");
        siteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Site> siteList = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    Site site = ds.getValue(Site.class);
                    site.setId(ds.getKey());
                    siteList.add(site);

                }
                saveData("siteList",siteList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsers() {
        DatabaseReference orderRef = database.getReference("User");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<User> UserList = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    User user = ds.getValue(User.class);
                    user.setId(ds.getKey());
                    UserList.add(user);

                }
                saveData("userList",UserList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOrders() {




        DatabaseReference orderRef = database.getReference("Orders");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<Order> OrderList = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    Order order = ds.getValue(Order.class);

                    System.out.println(order.getOrderReference());
                    OrderList.add(order);

                }
                saveData("orderList",OrderList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getItems() {

//        ArrayList<Item> ItemsList = new ArrayList<>();
//
//        Item item1 = new Item("IT00001","Sand","cubic",true,1200.0);
//
//        item1.setChecked(false);
//        item1.setQuantity(0.0);
//        item1.setIndex(0);
//
//        ItemsList.add(item1);
//
//
//        Item item2 = new Item("IT00002","Cement","piece",true,5200.0);
//
//        item2.setChecked(false);
//        item2.setQuantity(0.0);
//        item2.setIndex(1);
//
//
//        ItemsList.add(item2);
//
//
//        Item item3 = new Item("IT00003","Tap","piece",true,150.0);
//
//        item3.setChecked(false);
//        item3.setQuantity(0.0);
//        item3.setIndex(2);
//
//
//        ItemsList.add(item3);
//
//        Item item4 = new Item("IT00001","Rocks","cubic",true,7400.0);
//
//        item4.setChecked(false);
//        item4.setQuantity(0.0);
//        item4.setIndex(3);
//
//        ItemsList.add(item4);
//
//        saveData("itemList",ItemsList);
    }

    private void getSuppliers() {

        final DatabaseReference orderRef = database.getReference("Suppliers");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Supplier> SupplierList = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    final Supplier supplier = ds.getValue(Supplier.class);
                    supplier.setSupplierId(ds.getKey());
                    System.out.println(supplier.getSupplierName());
                    SupplierList.add(supplier);

                    orderRef.child(supplier.getSupplierId()).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Item> ItemList = new ArrayList<>();
                            if(dataSnapshot.exists()){
                                System.out.println("items exist");
                                int x = 0;

                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    Item item = ds.getValue(Item.class);
                                    item.setItemCode(ds.getKey());
                                    item.setChecked(false);
                                    item.setQuantity(0.0);
                                    item.setIndex(x++);
                                    ItemList.add(item);
                                }

                                SharedPreferences sharedPreferences = context.getSharedPreferences("order", 0);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.putString(supplier.getSupplierId()+"Count", String.valueOf(x));
                                sharedPreferencesEditor.apply();

                                saveData(supplier.getSupplierId(),ItemList);

                            }else{
                                SharedPreferences sharedPreferences = context.getSharedPreferences("order", 0);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.putString(supplier.getSupplierId()+"Count", String.valueOf(0));
                                sharedPreferencesEditor.apply();
                                saveData(supplier.getSupplierId(),ItemList);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                saveData("supplierList",SupplierList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void saveData(String name,ArrayList list){

        SharedPreferences sharedPreferences = context.getSharedPreferences("order", 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);
        sharedPreferencesEditor.putString(name, json);
        sharedPreferencesEditor.apply();

    }

}
