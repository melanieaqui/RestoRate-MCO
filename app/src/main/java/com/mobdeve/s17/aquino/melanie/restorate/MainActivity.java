package com.mobdeve.s17.aquino.melanie.restorate;

import static java.lang.String.valueOf;
import static java.util.Locale.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<RestoData>RestaurantArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RestoAdapter myRestoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView= findViewById(R.id.searchView);
        searchView.clearFocus();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        recyclerView = findViewById(R.id.main_recyclerView);
        db = FirebaseFirestore.getInstance();
       // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRestoAdapter = new RestoAdapter(RestaurantArrayList,MainActivity.this);

        EventChange();
        recyclerView.setAdapter(myRestoAdapter);

        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
               // myRestoAdapter.getFilter().filter(queryString);
                filterList(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {

                filterList(queryString);
                return true;
            }
        });
    }


    private void filterList(String queryString) {
        ArrayList<RestoData>filteredList = new ArrayList<>();
        for(RestoData item: RestaurantArrayList) {
            if (item.getName().toLowerCase().contains(queryString.toLowerCase())) {
                filteredList.add(item);
            }
        }
        myRestoAdapter.setFilteredList(filteredList);
    }


    private void EventChange() {
        db.collection("restaurants").orderBy("rating", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("Error",error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        RestoData data = (dc.getDocument().toObject(RestoData.class));
                        RestaurantArrayList.add(data);
                    }
                    else if(dc.getType()==DocumentChange.Type.MODIFIED){
                        RestoData data = (dc.getDocument().toObject(RestoData.class));
                        //RestaurantArrayList.remove(dc.getOldIndex());
                        RestaurantArrayList.set(dc.getOldIndex(),data);

                    }
                    else if (dc.getType()==DocumentChange.Type.REMOVED){
                        RestaurantArrayList.remove(dc.getOldIndex());
                    }
                }
                myRestoAdapter.notifyDataSetChanged();
            }
        });
    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            Intent logged = new Intent (getApplicationContext(), LoginActivity.class);
            startActivity(logged);
            finish();
        }
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