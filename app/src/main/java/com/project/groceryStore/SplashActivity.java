package com.project.groceryStore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    ImageView i;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        i = findViewById(R.id.logo);
        t = findViewById(R.id.text1);
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);


        i.startAnimation(fade);
        t.startAnimation(fade);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, 3000);
    }
}
