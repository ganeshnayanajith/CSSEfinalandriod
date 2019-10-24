package com.example.alexa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexa.Model.Order;
import com.example.alexa.Model.Supplier;
import com.example.alexa.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DashboardOrderAdapter extends RecyclerView.Adapter<DashboardOrderAdapter.DashboardOrderViewHolder> {


    private Context mContext;
    private List<Order> mData;
    CardView cardView;


    public DashboardOrderAdapter(Context mContext, ArrayList<Order> mData,CardView cardView) {
        this.mContext = mContext;
        this.mData = mData;

        this.cardView = cardView;
    }

    @NonNull
    @Override
    public  DashboardOrderAdapter.DashboardOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.dashboard_order,parent,false);
        return new DashboardOrderAdapter.DashboardOrderViewHolder(view,mContext,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardOrderAdapter.DashboardOrderViewHolder ViewHolder, final int i) {

        ViewHolder.id.setText(mData.get(i).getOrderReference());
        ViewHolder.name.setText(mData.get(i).getSupplierName());
        ViewHolder.total.setText(""+mData.get(i).getEstimatedValue());
        ViewHolder.date.setText(mData.get(i).getDeliveryDate());
        ViewHolder.recieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder((AppCompatActivity) mContext)
                        .setTitle("Is it received?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", R.drawable.new_ic_delete_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(mData.get(i).getOrderReference());
                                reference.child("orderStatus").setValue("Received");

                                FetchDataAdapter dataAdapter = new FetchDataAdapter(mContext);

                                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Processing..");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();

                                    }
                                }, 500);
                                mData.remove(i);
                                if(mData.isEmpty()){
                                    cardView.setVisibility(View.VISIBLE);
                                }
                                notifyItemRemoved(i);

                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", R.drawable.ic_close_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();

                mBottomSheetDialog.show();
            }
        });

    }



    @Override
    public int getItemCount() {

        return mData.size();
    }



    public static class DashboardOrderViewHolder extends RecyclerView.ViewHolder{


        TextView name,total,id,date;
        Button recieved;

        public DashboardOrderViewHolder(@NonNull View itemView, final Context context, final List<Order> item) {
            super(itemView);

            name = itemView.findViewById(R.id.supplier);
            total = itemView.findViewById(R.id.total);
            id = itemView.findViewById(R.id.orderId);
            date = itemView.findViewById(R.id.date);
            recieved = itemView.findViewById(R.id.recieved);


        }
    }

}