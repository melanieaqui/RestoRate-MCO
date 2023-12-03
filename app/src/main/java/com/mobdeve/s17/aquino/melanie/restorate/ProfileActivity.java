package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView txt_user_name;
    ImageView img_prof_img;
    TextView txt_email_display;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_email_display =findViewById(R.id.txt_email_display);
        img_prof_img =findViewById(R.id.img_prof_img);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);
        if (user!=null){
            txt_user_name.setText(user.getDisplayName());
            txt_email_display.setText(user.getEmail());
            if(user.getPhotoUrl()!=null)
                Picasso.with(ProfileActivity.this).load(user.getPhotoUrl()).into(img_prof_img);
            else
                img_prof_img.setImageResource(R.drawable.ic_profile);
            bottomNavigationView.setSelectedItemId(R.id.profile);

        }


    }
    public void logout (View v){
        FirebaseAuth.getInstance().signOut();
        Intent log = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(log);

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
            intent = new Intent(this,BookmarkActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            return true;


        } else if(item.getItemId() == R.id.profile) {

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
