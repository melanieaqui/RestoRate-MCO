package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewRestoActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView_reviews;
    ImageButton btn_review;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resto);
        btn_review = findViewById(R.id.btn_review);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recyclerView_reviews = findViewById(R.id.recyclerView_reviews);
        recyclerView_reviews.setHasFixedSize(true);
        recyclerView_reviews.setLayoutManager(new LinearLayoutManager(this));

        UserData sample = new UserData("test@gmail.com", R.drawable.jollibee);

        ReviewsData[] reviewsData = new ReviewsData[]{
                new ReviewsData(sample, "Pasig", "Tasty Foods!", "Slow service", "Makalat", "6.7", R.drawable.chickenjoy),
                new ReviewsData(sample, "Manila", "Bland :<", "Slow service", "Clean", "5.3", R.drawable.chickenjoy),
                new ReviewsData(sample, "Makati", "Everything is too salty", "Slow service", "Clean", "6.0", R.drawable.chickenjoy),

        };

        ReviewsAdapter myReviewsAdapter = new ReviewsAdapter(reviewsData, this);
        recyclerView_reviews.setAdapter(myReviewsAdapter);

        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);



    }
    public void redirect_review(View v){
        Intent intent = new Intent(this,AddRestoReview.class);
        intent.putExtra("RESTO_NAME","Jollibee"); //temp
        startActivity(intent);

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
        }
        return false;
    }
}