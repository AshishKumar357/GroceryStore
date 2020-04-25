package com.project.grocerystore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button profilebtn, applybtn;
    AdView mAdView, mAdView2;
    private static final String TAG = "MainActivty";
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String userID;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applybtn = findViewById(R.id.applyBtn);
        profilebtn = findViewById(R.id.profile_btn);
        mAdView = findViewById(R.id.adViewMain1);
        mAdView2 = findViewById(R.id.adViewMain2);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fauth.getCurrentUser()).getUid();

    }
}