package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AddRestoReview extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    BottomNavigationView bottomNavigationView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    EditText txt_resto_name;
    EditText txt_review_env;
    EditText txt_review_service;
    EditText txt_food_quality;
    RatingBar rating_star;
    TextInputLayout textInputLayer_name;
    String id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        txt_resto_name= (EditText) findViewById(R.id.txt_resto_name);
        textInputLayer_name = findViewById(R.id.textInputLayer_name);
        txt_review_env = findViewById(R.id.txt_review_env);
        txt_review_service = findViewById(R.id.txt_review_service);
        txt_food_quality = findViewById(R.id.txt_food_quality);
        Intent intent=getIntent();
        if(intent!= null && intent.getStringExtra("RESTO_NAME")!=null){
            txt_resto_name.setText(intent.getStringExtra("RESTO_NAME"));
        }
        rating_star = findViewById(R.id.rating_star);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);


        bottomNavigationView.setSelectedItemId(R.id.review);
    }

    public void check(View v){
        FirestoreHelper.getDocumentByField(db, "restaurants", "name", txt_resto_name.getText().toString().toUpperCase(), new FirestoreHelper.OnDocumentSnapshotListener() {
            @Override
            public void onDocumentSnapshot(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Document exists, and you can access its data
                    id = documentSnapshot.getId();
                    Log.d("Firestore", "Document data: " + documentSnapshot.getData());

                } else {
                    // No document found or there was an error
                    Log.d("Firestore", "No document found or there was an error");
                    textInputLayer_name.setError("Restaurant does not exist, consider adding it first");
                }
            }
        });
    }
    public void addReview(){
        Map<String, Object> reviews = new HashMap<>();
        reviews.put("user",user.getUid());
        //if()
        //reviews.put("")
        //restaurant.put("name", txt_view_name.getText().toString());
       // restaurant.put("foodtype", txt_type.getText().toString());
        //restaurant.put("image",restoImage);

        db.collection("/restaurants/"+id+"/reviews")
                .add(reviews)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //progressDialog.setCanceledOnTouchOutside(true);
                        // progressDialog.setMessage("Success!");
                        // progressDialog.dismiss();
                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //progressDialog.setCanceledOnTouchOutside(true);
                        //progressDialog.setMessage("Error occurred. Please try again.");
                        //progressDialog.dismiss();

                        Log.w("Failure", "Error adding document", e);
                    }
                });
        Intent back =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(back);
        finish();
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
