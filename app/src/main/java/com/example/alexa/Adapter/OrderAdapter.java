package com.example.alexa.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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

import com.example.alexa.Model.Order;
import com.example.alexa.Model.SelectedItems;
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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {


    private Context mContext;
    private List<Order> mData;
    SweetAlertDialog mDialog;

    public OrderAdapter(Context mContext, ArrayList<Order> mData, SweetAlertDialog bar) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDialog = bar;

    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.OrderViewHolder(view, mContext, mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderAdapter.OrderViewHolder ViewHolder, final int i) {

//        ViewHolder.name.setText(mData.get(i).getSupplierName());

        ViewHolder.supplier.setText(mData.get(i).getSupplierName());
        ViewHolder.date.setText(mData.get(i).getDeliveryDate());
        ViewHolder.total.setText("Rs. " + mData.get(i).getEstimatedValue());
        ViewHolder.id.setText(mData.get(i).getOrderReference());
        ViewHolder.status.setText(mData.get(i).getOrderStatus());

        if(mData.get(i).getOrderStatus().equals("Approved")){
            ViewHolder.status.setTextColor(Color.parseColor("#258f51"));
        }else if(mData.get(i).getOrderStatus().equals("Pending")){
            ViewHolder.status.setTextColor(Color.parseColor("#d4590d"));
        }else{
            ViewHolder.status.setTextColor(Color.parseColor("#255e8f"));
        }
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


        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder((AppCompatActivity) mContext)
                        .setTitle("Do you want to get a call?")
                        .setMessage("Supplier : " + mData.get(i).getSupplierName())
                        .setCancelable(false)
                        .setPositiveButton("  Call", R.drawable.new_ic_call_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int which) {

                                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Processing..");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();

                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + "773306450"));
                                        if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    Activity#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for Activity#requestPermissions for more details.
                                            return;
                                        }
                                        mContext.startActivity(callIntent);
                                        dialogInterface.dismiss();
                                    }
                                }, 1000);

                                //use supplier mobile number



                            }
                        })
                        .setNegativeButton("  Cancel", R.drawable.ic_call_end_black_24dp, new BottomSheetMaterialDialog.OnClickListener() {
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

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView items,supplier,total,date,id,status,quantity;
        ImageView delete,phone;

        public OrderViewHolder(@NonNull View itemView, final Context context, final List<Order> item) {
            super(itemView);

            id = itemView.findViewById(R.id.orderId);
            items = itemView.findViewById(R.id.items);
            supplier = itemView.findViewById(R.id.supplier);
            total = itemView.findViewById(R.id.total);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            quantity = itemView.findViewById(R.id.quantity);

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