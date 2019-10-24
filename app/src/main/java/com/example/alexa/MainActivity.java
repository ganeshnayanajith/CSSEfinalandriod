package com.example.alexa;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.example.alexa.Adapter.FetchDataAdapter;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 1500;
    ProgressBar mprogressBar;
    ObjectAnimator anim ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchDataAdapter data = new FetchDataAdapter(this);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(3000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        splash();

    }

    private void splash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
