package com.example.alexa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexa.ItemListActivity;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.Model.Supplier;
import com.example.alexa.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderTableAdapter extends RecyclerView.Adapter<OrderTableAdapter.OrderTableViewHolder> {


    private Context mContext;
    private List<SelectedItems> mData;
    SweetAlertDialog mDialog;
    int index = 0;
    double total =0;
    TextView totalText;

    public OrderTableAdapter(Context mContext, ArrayList<SelectedItems> mData, SweetAlertDialog bar,TextView textView) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDialog = bar;
        totalText = textView;
    }

    @NonNull
    @Override
    public  OrderTableAdapter.OrderTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.table_row,parent,false);
        return new OrderTableAdapter.OrderTableViewHolder(view,mContext,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderTableAdapter.OrderTableViewHolder ViewHolder, final int i) {


        total += mData.get(i).getTotal();
        if(index++%2==0){
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ViewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.table_row1));
            } else {
                ViewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.table_row1));

            }

        }else{

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ViewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.table_row2));
            } else {
                ViewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.table_row2));

            }
        }
        ViewHolder.name.setText(mData.get(i).getItemName());
        ViewHolder.qnt.setText(""+mData.get(i).getQuantity());
        ViewHolder.total.setText(""+mData.get(i).getTotal());

        mDialog.dismiss();
        if(mData.size()-1==i){
            totalText.setText("Rs. "+total);
        }
    }



    @Override
    public int getItemCount() {

        return mData.size();
    }

    public double getTotal(){
        return total;
    }

    public static class OrderTableViewHolder extends RecyclerView.ViewHolder{


        TextView name,qnt,total;


        public OrderTableViewHolder(@NonNull View itemView, final Context context, final List<SelectedItems> item) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            qnt = itemView.findViewById(R.id.quantity);
            total = itemView.findViewById(R.id.total);


        }
    }
}