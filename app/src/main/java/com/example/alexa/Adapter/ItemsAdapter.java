package com.example.alexa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.alexa.Model.Item;
import com.example.alexa.Model.SelectedItems;
import com.example.alexa.R;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {


    private Context mContext;
    private List<Item> mData;
    SweetAlertDialog mDialog;

    public ItemsAdapter(Context mContext, ArrayList<Item> mData, SweetAlertDialog bar) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDialog = bar;

    }

    @NonNull
    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item,parent,false);
        return new ItemsAdapter.ItemsViewHolder(view,mContext,mData);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsAdapter.ItemsViewHolder ViewHolder, final int i) {



        if(mData.get(i).isChecked()) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ViewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.selected_item));
            } else {
                ViewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_item));

            }
            ViewHolder.checkbox.setVisibility(View.VISIBLE);

        }else{
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ViewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.item_background));
            } else {
                ViewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_background));

            }
            ViewHolder.checkbox.setVisibility(View.INVISIBLE);

        }

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(mData.get(i).isChecked());
                System.out.println(mData.get(i).getQuantity());
            }
        });
        ViewHolder.name.setText(mData.get(i).getItemName());
//        ViewHolder.items.setText("4 items");
        mDialog.dismiss();
    }



    public ArrayList<SelectedItems> getSelectedItems(){

        ArrayList<SelectedItems> list = new ArrayList<>();
        for (Item item:mData){
            if(item.isChecked()){

                SelectedItems selectedItems = new SelectedItems();
                selectedItems.setItemId(item.getItemCode());
                selectedItems.setItemName(item.getItemName());
                selectedItems.setQuantity(item.getQuantity());
                selectedItems.setTotal(item.getQuantity()*Double.parseDouble(item.getItemUnitPrice()));

                list.add(selectedItems);
            }
        }

        return list;

    }


    public List<Item> getmData() {
        return mData;
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public void restoreItem(int position) {

        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder{


        TextView name,checkbox;






        public ItemsViewHolder(@NonNull View itemView, final Context context, final List<Item> item) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            checkbox = itemView.findViewById(R.id.checkbox);

            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };


        }
    }
}