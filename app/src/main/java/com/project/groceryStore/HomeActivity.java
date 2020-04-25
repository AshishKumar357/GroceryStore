package com.project.groceryStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toast.makeText(getApplicationContext(), "Inside Home Activity", Toast.LENGTH_SHORT).show();

        Button b = findViewById(R.id.borrow);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Apply", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent a = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;
                    case R.id.nav_account:
                        Intent b = new Intent(HomeActivity.this, AccountActivity.class);
                        startActivity(b);
                        break;
//                    case R.id.nav_help:
//                        Intent c = new Intent(HomeActivity.this, faq.class);
//                        startActivity(c);
                }
                return false;
            }
        });
    }
}
