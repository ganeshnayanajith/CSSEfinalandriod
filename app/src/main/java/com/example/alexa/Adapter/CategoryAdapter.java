package com.example.alexa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.alexa.Model.Supplier;
import com.example.alexa.R;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    private Context mContext;
    private List<String> mData;
    String category;
    ArrayList<Supplier> suppliers;
    SweetAlertDialog mDialog;
    RecyclerView recyclerView;
    int previous = 0;
    View prev;

    public CategoryAdapter(Context mContext, ArrayList<String> mData, String cat,ArrayList<Supplier> suppliers,RecyclerView recyclerView,SweetAlertDialog mDialog) {
        this.mContext = mContext;
        this.mData = mData;
        this.category = cat;
        this.suppliers = suppliers;
        this.mDialog = mDialog;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public  CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.category,parent,false);
        return new CategoryAdapter.CategoryViewHolder(view,mContext,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.CategoryViewHolder ViewHolder, final int i) {

        ViewHolder.name.setText(mData.get(i));

        if(i == 0){
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ViewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.cat_selected) );
            } else {
                ViewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cat_selected));

            }
            prev = ViewHolder.itemView;
        }

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                System.out.println(mData.get(i));

                if(i != previous){
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ViewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.cat_selected) );
                        prev.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.cat_default) );
                    } else {
                        ViewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cat_selected));
                        prev.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cat_default));

                    }
                    prev = ViewHolder.itemView;
                }
                category = mData.get(i);
                    ArrayList<Supplier> selectedSupplierList = new ArrayList<>();

                    for(Supplier supplier:suppliers){

                        if(mData.get(i).equals("All")) {
                            System.out.println(mData.get(i));
                            selectedSupplierList.add(supplier);
                        }else if(supplier.getSupplierCategories().contains(mData.get(i))){
                            System.out.println(mData.get(i));
                            selectedSupplierList.add(supplier);
                        }
                    }
                SupplierAdapter myAdapter = new SupplierAdapter(mContext,selectedSupplierList,mDialog);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(myAdapter);

                previous = i;

            }
        });
    }



    @Override
    public int getItemCount() {

        return mData.size();
    }

    public String getCategory(){
        return category;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{


        TextView name,items;






        public CategoryViewHolder(@NonNull View itemView, final Context context, final List<String> item) {
            super(itemView);

            name = itemView.findViewById(R.id.name);


        }
    }

}
