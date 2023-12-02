package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RestoAdapter bRestoAdapter;
    ArrayList<RestoData> RestaurantArrayList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        RecyclerView recyclerView = findViewById(R.id.bookmark_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        //RestoData[] myRestoData = new RestoData[]{
        //        new RestoData("Jollibee","Fast Food",R.drawable.jollibee,6.6),
        //        new RestoData("Mang Inasal","Grilled Foods",R.drawable.manginasal,7.4),

       // };

        bRestoAdapter = new RestoAdapter(RestaurantArrayList,BookmarkActivity.this);
        recyclerView.setAdapter(bRestoAdapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);


        bottomNavigationView.setSelectedItemId(R.id.bookmark);
    }
    private void EventChange() {
        db.collection("bookmarks").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("Error",error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        RestaurantArrayList.add(dc.getDocument().toObject(RestoData.class));
                    }
                }
                bRestoAdapter.notifyDataSetChanged();
            }
        });
    }


    private boolean onItemSelectedListener(MenuItem item) {
        Intent intent;
        if(item.getItemId() == R.id.home) {
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            return true;


        } else if(item.getItemId() == R.id.bookmark) {

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
        }else if (item.getItemId() == R.id.review) {
            intent = new Intent(this, AddRestoReview.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        return false;
    }
}
