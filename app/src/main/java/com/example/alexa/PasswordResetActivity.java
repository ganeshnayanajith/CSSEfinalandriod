package com.example.alexa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    Button reset;
    EditText email;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        reset = findViewById(R.id.reset);

        email = findViewById(R.id.email);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PasswordResetActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PasswordResetActivity.this,LoginActivity.class));
                                }else {
                                    Toast.makeText(PasswordResetActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
