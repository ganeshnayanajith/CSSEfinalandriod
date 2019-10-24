package com.example.alexa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alexa.ItemListActivity;
import com.example.alexa.R;
import com.example.alexa.Model.Supplier;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {


    private Context mContext;
    private List<Supplier> mData;
    SweetAlertDialog mDialog;

    public SupplierAdapter(Context mContext, ArrayList<Supplier> mData, SweetAlertDialog bar) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDialog = bar;

    }

    @NonNull
    @Override
    public  SupplierAdapter.SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.supplier_item,parent,false);
        return new SupplierAdapter.SupplierViewHolder(view,mContext,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final SupplierAdapter.SupplierViewHolder ViewHolder, final int i) {

        mDialog.dismiss();
        ViewHolder.name.setText(mData.get(i).getSupplierName());
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order", 0);

        String count= sharedPreferences.getString(mData.get(i).getSupplierId()+"Count" , "");
        ViewHolder.items.setText(count+" items");

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("order", 0);

                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                sharedPreferencesEditor.putString("SupplierName", mData.get(i).getSupplierName());

                sharedPreferencesEditor.putString("SupplierId", mData.get(i).getSupplierId());

                sharedPreferencesEditor.apply();

                mContext.startActivity(new Intent(mContext, ItemListActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public static class SupplierViewHolder extends RecyclerView.ViewHolder{
        TextView name,items;

        public SupplierViewHolder(@NonNull View itemView, final Context context, final List<Supplier> item) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            items = itemView.findViewById(R.id.items);


        }
    }
}