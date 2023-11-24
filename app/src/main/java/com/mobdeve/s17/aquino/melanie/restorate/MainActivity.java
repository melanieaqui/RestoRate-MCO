package com.mobdeve.s17.aquino.melanie.restorate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity  {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RestoData[] myRestoData = new RestoData[]{
                new RestoData("Jollibee","Fast Food",R.drawable.jollibee,6.6),
                new RestoData("ChowKing","Chinese Inspired Foods.",R.drawable.chowking,9.7),
                new RestoData("Mang Inasal","Grilled Foods",R.drawable.manginasal,7.4),
                new RestoData("Greewnwich","Pizza and Pasta",R.drawable.greenwich,6.4),

        };

        RestoAdapter myRestoAdapter = new RestoAdapter(myRestoData,MainActivity.this);
        recyclerView.setAdapter(myRestoAdapter);

        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.home);
    }



    private boolean onItemSelectedListener(MenuItem item) {
        Intent intent;
        if(item.getItemId() == R.id.home) {

            return true;


        } else if(item.getItemId() == R.id.bookmark) {
            intent = new Intent(this,BookmarkActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            return true;


        } else if(item.getItemId() == R.id.profile) {
            intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            return true;
        }else if(item.getItemId() == R.id.add) {
            intent = new Intent(this,AddRestoActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            return true;
        }

        else if (item.getItemId() == R.id.review) {
            intent = new Intent(this, AddRestoReview.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        return false;
    }
}