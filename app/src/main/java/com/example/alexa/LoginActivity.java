package com.example.alexa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexa.Model.Supplier;
import com.example.alexa.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    Button login;
    TextView reset;
    EditText email,password;
    ArrayList<User> UserList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reset = findViewById(R.id.reset);

        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        UserList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,PasswordResetActivity.class));
            }
        });

        SharedPreferences sharedPreferences = this.getSharedPreferences("order", 0);
        final Gson gson = new Gson();

        String response= sharedPreferences.getString("userList" , "");
        System.out.println("login "+response);
        UserList = gson.fromJson(response,new TypeToken<List<User>>(){}.getType());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Insert your email", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Insert your password", Toast.LENGTH_SHORT).show();
                }else if(!isValidEmailAddress(email.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }else {
                    login(email.getText().toString(), password.getText().toString());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void login(String email, final String pass) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Processing..");
        pDialog.setCancelable(false);
        pDialog.show();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            boolean status = false;
                            FirebaseUser loggedUser = mAuth.getCurrentUser();

                            System.out.println("Logged");
                            for(User user:UserList){
                                if(user.getId().equals(loggedUser.getUid())){
                                    if(user.getRole().equals("site_manager")){
                                        status = true;
                                        SharedPreferences sharedPreferences = getSharedPreferences("order", 0);

                                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                                        sharedPreferencesEditor.putString("userName", user.getName());
                                        sharedPreferencesEditor.putString("userEmail", user.getEmail());
                                        sharedPreferencesEditor.putString("userPhone", user.getPhoneNo());
                                        sharedPreferencesEditor.putString("userSite", user.getSite());
                                        sharedPreferencesEditor.putString("userSiteName", user.getSiteName());
                                        sharedPreferencesEditor.putString("userAddress", user.getAddress());
                                        sharedPreferencesEditor.putString("userImage", user.getImage());

                                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                            @Override
                                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                                String deviceToken = instanceIdResult.getToken();
                                                DatabaseReference refx= FirebaseDatabase.getInstance().getReference("User/"+ FirebaseAuth.getInstance().getUid());
                                                refx.child("Token").setValue(deviceToken);
                                            }
                                        });

                                        sharedPreferencesEditor.apply();
                                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                                        break;
                                    }
                                }
                            }
                            if(!status){
                                pDialog.dismiss();
                                password.setText("");
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }




                        } else {

                            pDialog.dismiss();
                            password.setText("");
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            for(User user:UserList){
                if(user.getId().equals(currentUser.getUid())){
                    if(user.getRole().equals("site_manager")){
                        SharedPreferences sharedPreferences = getSharedPreferences("order", 0);

                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                        sharedPreferencesEditor.putString("userName", user.getName());
                        sharedPreferencesEditor.putString("userEmail", user.getEmail());
                        sharedPreferencesEditor.putString("userPhone", user.getPhoneNo());
                        sharedPreferencesEditor.putString("userSite", user.getSite());
                        sharedPreferencesEditor.putString("userSiteName", user.getSiteName());
                        sharedPreferencesEditor.putString("userAddress", user.getAddress());
                        sharedPreferencesEditor.putString("userImage", user.getImage());

                        sharedPreferencesEditor.apply();
                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        break;
                    }
                }
            }


        }
    }
}
