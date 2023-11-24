package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddRestoReview extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    EditText txt_resto_name;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        txt_resto_name= (EditText) findViewById(R.id.txt_resto_name);
        Intent intent=getIntent();
        if(intent!= null && intent.getStringExtra("RESTO_NAME")!=null){
            txt_resto_name.setText(intent.getStringExtra("RESTO_NAME"));
        }
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);


        bottomNavigationView.setSelectedItemId(R.id.review);
    }

    private boolean onItemSelectedListener(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;


        } else if (item.getItemId() == R.id.bookmark) {
            intent = new Intent(this, BookmarkActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;


        } else if (item.getItemId() == R.id.profile) {
            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;
        } else if (item.getItemId() == R.id.add) {
            intent = new Intent(this, AddRestoActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.review) {

            return true;
        }
        return false;
    }

}
