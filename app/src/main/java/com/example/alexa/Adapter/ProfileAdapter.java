package com.example.alexa.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.alexa.DashboardActivity;
import com.example.alexa.LoginActivity;
import com.example.alexa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileAdapter {

    Context mContext;
    Dialog MYDialog;
    String username,useremail,useraddress,userphone,userid,userImage;
    ImageView image;
    EditText name,phoneNo,address;
    TextView email;
    CardView profileText,home,logout;
    int i = 0;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    public static final int PICK_IMAGE = 1;
    private String userID;
    private Uri resourceUri;
    Activity activity;

    public ProfileAdapter(Context context,Activity activity) {
        mContext = context;
        this.activity = activity;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("order", 0);

        username = sharedPreferences.getString("userName" , "");
        useremail = sharedPreferences.getString("userEmail" , "");
        useraddress = sharedPreferences.getString("userAddress" , "");
        userphone = sharedPreferences.getString("userPhone" , "");
        userImage = sharedPreferences.getString("userImage" , "");
        userid = FirebaseAuth.getInstance().getUid();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userID);

    }

    public void displayProfile(){
        MYDialog =  new Dialog(mContext);


        final GridLayout settings;

        final LinearLayout profile;
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                pDialog.dismiss();
            }
        }, 1000);

        TextView cancel, select;
        MYDialog.setContentView(R.layout.profile_edit);
        MYDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MYDialog.setCancelable(true);

        profileText = MYDialog.findViewById(R.id.profileText);
        profile = MYDialog.findViewById(R.id.profile);

        profile.setVisibility(View.GONE);
        logout = MYDialog.findViewById(R.id.logout);
        home = MYDialog.findViewById(R.id.home);
        image = MYDialog.findViewById(R.id.image);
        name = MYDialog.findViewById(R.id.siteManName);
        phoneNo = MYDialog.findViewById(R.id.siteManPhone);
        email = MYDialog.findViewById(R.id.siteManEmail);
        address = MYDialog.findViewById(R.id.address);


//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//
//            }
//        });
        settings = MYDialog.findViewById(R.id.settings);


        name.setText(username);
        address.setText(useraddress);
        email.setText(useremail);
        phoneNo.setText(userphone);
        //Glide.with(activity.getApplication()).load(userImage).into(image);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, DashboardActivity.class));
                MYDialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                MYDialog.dismiss();
            }
        });
        cancel = MYDialog.findViewById(R.id.cancel);

            profileText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(i++%2==0){
                        profile.setVisibility(View.VISIBLE);
                        settings.setVisibility(View.GONE);
                        MYDialog.setCancelable(false);
                    }else {
                        profile.setVisibility(View.GONE);
                        settings.setVisibility(View.VISIBLE);
                    }
                }
            });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setVisibility(View.GONE);
                settings.setVisibility(View.VISIBLE);
                MYDialog.setCancelable(true);
            }
        });
        select = MYDialog.findViewById(R.id.update);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map userinfo = new HashMap();
                userinfo.put("name", name.getText().toString());
                userinfo.put("phone", phoneNo.getText().toString());
                userinfo.put("address", address.getText().toString());


                mUserDatabase.updateChildren(userinfo);
                profile.setVisibility(View.GONE);
                settings.setVisibility(View.VISIBLE);
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Successfully Updated");
                pDialog.setCancelable(false);
                pDialog.show();
                MYDialog.setCancelable(true);
            }
        });
        MYDialog.show();
    }



}
