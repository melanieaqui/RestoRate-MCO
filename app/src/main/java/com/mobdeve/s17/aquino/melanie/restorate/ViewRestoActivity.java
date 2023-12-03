package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewRestoActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView_reviews;
    ImageButton btn_review;
    ImageButton btn_bookmark;
    ImageView img_photo;
    TextView txt_view_name;
    TextView txt_rating;
    TextView txt_type;
    String restoImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ReviewsData> reviewsList = new ArrayList<>();
    String id;
    ReviewsAdapter myReviewsAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resto);
        btn_review = findViewById(R.id.btn_review);
        txt_view_name = findViewById(R.id.txt_view_name);
        txt_rating = findViewById(R.id.txt_rating);
        txt_type = findViewById(R.id.txt_type);
        img_photo = findViewById(R.id.img_photo);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recyclerView_reviews = findViewById(R.id.recyclerView_reviews);
        recyclerView_reviews.setHasFixedSize(true);
        recyclerView_reviews.setLayoutManager(new LinearLayoutManager(this));

        /*intent.putExtra("RESTO_TYPE",RestoDataList.getType());
                intent.putExtra("RESTO_IMAGE",RestoDataList.getImage());
                intent.putExtra("RESTO_RATING",RestoDataList.getRating());*/
        Intent prev = getIntent();
        String name=prev.getStringExtra("RESTO_NAME");
        restoImage  = prev.getStringExtra("RESTO_IMAGE");
        String type = prev.getStringExtra("RESTO_TYPE");
//        String rating=prev.getStringExtra("RESTO_RATING");
        txt_view_name.setText(name);
        txt_type.setText(type);
      //  txt_rating.setText(rating);
        Picasso.with(getApplicationContext()).load(Uri.parse(restoImage)).into(img_photo);

        //UserData sample = new UserData("test@gmail.com", R.drawable.jollibee);

       // ReviewsData[] reviewsData = new ReviewsData[]{
        //        new ReviewsData(sample, "Pasig", "Tasty Foods!", "Slow service", "Makalat", "6.7", R.drawable.chickenjoy),
        //        new ReviewsData(sample, "Manila", "Bland :<", "Slow service", "Clean", "5.3", R.drawable.chickenjoy),
        //        new ReviewsData(sample, "Makati", "Everything is too salty", "Slow service", "Clean", "6.0", R.drawable.chickenjoy),

        //};

        myReviewsAdapter = new ReviewsAdapter(reviewsList, this);
        recyclerView_reviews.setAdapter(myReviewsAdapter);

        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);
        check();


    }
    public void check(){
        if(txt_view_name.getText().toString()!=null){
            FirestoreHelper.getDocumentByField(db, "restaurants", "name", (txt_view_name.getText().toString().toUpperCase()), new FirestoreHelper.OnDocumentSnapshotListener() {
                @Override
                public void onDocumentSnapshot(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Document exists, and you can access its data
                        id = documentSnapshot.getId();

                        Log.d("Firestore", "Document data: " + documentSnapshot.getData());
                        EventChange();
                    } else {
                        // No document found or there was an error
                        Log.d("Firestore", "No document found or there was an error");
                        //textInputLayer_name.setError("Restaurant does not exist, consider adding it first");
                    }
                }
            });
           // textInputLayer_name.setError("Restaurant name should be filled");

        }

    }

    private void EventChange() {
        db.collection("/restaurants/"+id+"/reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("Error",error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        // dc.getDocument().get("")
                        ReviewsData data = (dc.getDocument().toObject(ReviewsData.class));
                        //data.setFoodtype(dc.getDocument().get("foodtype").toString());
                        reviewsList.add(data);
                    }
                }
                myReviewsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addbookmark(View v){
        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("name", txt_view_name.getText().toString());
        restaurant.put("foodtype", txt_type.getText().toString());
        restaurant.put("image",restoImage);

        db.collection("/users/"+user.getUid()+"/bookmarks")
                .add(restaurant)
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
        Intent back =new Intent(getApplicationContext(),BookmarkActivity.class);
        startActivity(back);
        finish();
    }
    public void redirect_review(View v){
        Intent intent = new Intent(this,AddRestoReview.class);
        intent.putExtra("RESTO_NAME",txt_view_name.getText().toString()); //temp
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