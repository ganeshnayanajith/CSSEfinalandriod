package com.example.alexa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexa.DashboardActivity;
import com.example.alexa.ItemListActivity;
import com.example.alexa.Model.Order;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.Model.Supplier;
import com.example.alexa.R;
import com.example.alexa.SelectedItemsActivity;
import com.example.alexa.SupplierListActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {


    private Context mContext;
    private List<Order> mData;
    SweetAlertDialog mDialog;

    public PendingOrderAdapter(Context mContext, ArrayList<Order> mData, SweetAlertDialog bar) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDialog = bar;

    }

    @NonNull
    @Override
    public  PendingOrderAdapter.PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.pending_order,parent,false);
        return new PendingOrderAdapter.PendingOrderViewHolder(view,mContext,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingOrderAdapter.PendingOrderViewHolder ViewHolder, final int i) {

        ViewHolder.supplier.setText(mData.get(i).getSupplierName());
        ViewHolder.date.setText(mData.get(i).getDeliveryDate());
        ViewHolder.total.setText("Rs. "+mData.get(i).getEstimatedValue());
        ViewHolder.id.setText(mData.get(i).getOrderReference());

        StringBuffer item = new StringBuffer();
        for (SelectedItems items : mData.get(i).getItemList()) {
            if(items.getQuantity()%1 == 0){
                System.out.println(items.getQuantity());
                double remainder = items.getQuantity() - Math.round(items.getQuantity());
                if(remainder == 0.25 || remainder == 0.75){
                    item.append((int) Math.round(items.getQuantity()) + "\t\t\t\t\t\t\t\t" + items.getItemName() + "\n");
                }else {
                    item.append((int) Math.round(items.getQuantity()) + "\t\t\t\t\t\t\t\t\t\t\t" + items.getItemName() + "\n");
                }
            }else{
                item.append( items.getQuantity() + "\t\t\t\t\t\t\t\t\t" +items.getItemName() + "\n");

            }
        }
        ViewHolder.items.setText(item);

        ViewHolder.date.setText(mData.get(i).getDeliveryDate());

        ViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder((AppCompatActivity) mContext)
                        .setTitle("Want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", R.drawable.new_ic_delete_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(mData.get(i).getOrderReference());
                                reference.setValue(null);

                                FetchDataAdapter dataAdapter = new FetchDataAdapter(mContext);

                                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Deleting..");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();

                                    }
                                }, 500);
                                mData.remove(i);
                                notifyItemRemoved(i);
                                Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", R.drawable.ic_close_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Toast.makeText(mContext, "Cancelled!", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .build();

                mBottomSheetDialog.show();



            }
        });
        mDialog.dismiss();
    }



    @Override
    public int getItemCount() {

        return mData.size();
    }

    public static class PendingOrderViewHolder extends RecyclerView.ViewHolder{

        TextView items,supplier,total,date,id;
        ImageView delete;

        public PendingOrderViewHolder(@NonNull View itemView, final Context context, final List<Order> item) {
            super(itemView);

            id = itemView.findViewById(R.id.orderId);
            items = itemView.findViewById(R.id.items);
            supplier = itemView.findViewById(R.id.supplier);
            total = itemView.findViewById(R.id.total);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);

            items.setMaxLines(2);
            items.setVerticalScrollBarEnabled(true);
            items.setMovementMethod(new ScrollingMovementMethod());
            View.OnTouchListener listener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean isLarger;

                    isLarger = ((TextView) v).getLineCount()
                            * ((TextView) v).getLineHeight() > v.getHeight();
                    if (event.getAction() == MotionEvent.ACTION_MOVE
                            && isLarger) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);

                    } else {
                        v.getParent().requestDisallowInterceptTouchEvent(false);

                    }
                    return false;
                }
            };

            items.setOnTouchListener(listener);




        }
    }
}