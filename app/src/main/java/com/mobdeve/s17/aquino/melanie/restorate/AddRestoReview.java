package com.mobdeve.s17.aquino.melanie.restorate;

import static java.lang.String.valueOf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
    EditText txt_branch;
    RatingBar rating_star;
    TextInputLayout textInputLayer_name;
    String id;
    Uri photo;
    Uri downloadUri;
    ProgressDialog progressDialog = null;

    ImageButton btn_review_upload;
    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Log.i("OUT", valueOf(imageUri));
                    if(photo!= null){
                        //Picasso.with(AddRestoActivity.this).load(imageUri).into(img_resto);
                     //   btn_submitted.setVisibility(View.VISIBLE);
                    }
                    else if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            if (result.getData() != null) {
                                photo= result.getData().getData();
                               // Picasso.with(AddRestoActivity.this).load(imageUri).into(img_resto);
                               // btn_submitted.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception exception) {
                            Log.d("TAG", "" + exception.getLocalizedMessage());
                        }
                    }
                }


            });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        txt_resto_name= (EditText) findViewById(R.id.txt_resto_name);
        textInputLayer_name = findViewById(R.id.textInputLayer_name);
        txt_review_env = findViewById(R.id.txt_review_env);
        txt_review_service = findViewById(R.id.txt_review_service);
        txt_food_quality = findViewById(R.id.txt_food_quality);
        btn_review_upload = findViewById(R.id.btn_review_upload);
        txt_branch = findViewById(R.id.txt_branch);
        Intent intent=getIntent();
        if(intent!= null && intent.getStringExtra("RESTO_NAME")!=null){
            txt_resto_name.setText(intent.getStringExtra("RESTO_NAME"));
        }
        rating_star = findViewById(R.id.rating_star);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.review);
        btn_review_upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });
    }

    public boolean allfilled(){
        if (txt_review_env.getText().toString() ==null ||
                txt_review_service.getText().toString() ==null||
                txt_branch.getText() ==null ||
                txt_food_quality.getText().toString()==null)
            return false;
        return true;

    }
    public void check(View v){
        if(txt_resto_name.getText().toString()!=null){
            FirestoreHelper.getDocumentByField(db, "restaurants", "name", txt_resto_name.getText().toString().toUpperCase(), new FirestoreHelper.OnDocumentSnapshotListener() {
                @Override
                public void onDocumentSnapshot(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Document exists, and you can access its data
                        id = documentSnapshot.getId();
                        Log.d("Firestore", "Document data: " + documentSnapshot.getData());
                        if (allfilled()==true)
                            addReview();
                    } else {
                        // No document found or there was an error
                        Log.d("Firestore", "No document found or there was an error");
                        textInputLayer_name.setError("Restaurant does not exist, consider adding it first");
                    }
                }
            });

        }else{
            textInputLayer_name.setError("Restaurant name should be filled");
        }


    }
    private void updateRating(){
        CollectionReference collection = db.collection("/restaurants/"+id+"/reviews");
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                double totalRatings = 0;
                int documentCount = 0;

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Assuming "population" is the field in your documents
                    Double rating = document.getDouble("rating");

                    if (rating != null) {
                        totalRatings+= rating;
                        documentCount++;
                    }
                }

                if (documentCount > 0) {
                    double averageRating = totalRatings / documentCount;
                    Log.i("average_rating",valueOf(averageRating));
                    DocumentReference docRef = db.collection("restaurants").document(id);
                    averageRating =  Math.round(averageRating*100.0)/100.0;
                    docRef.update("rating",averageRating);
                } else {
                    Log.i("Error","Something happened");
                }
            }
        });
    }
    public void addReview(){

        if(photo!=null){
         progressDialog = new ProgressDialog(AddRestoReview.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = (storage.getReference());
            StorageReference RestoImagesRef = storageRef.child("reviews/"+photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = RestoImagesRef.putBytes(data);
            uploadTask = RestoImagesRef.putFile(photo);

            // Register observers to listen for when the download is done or if it fails
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return RestoImagesRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        Map<String, Object> reviews = new HashMap<>();
                        reviews.put("username",user.getDisplayName());
                        reviews.put("userimg",user.getPhotoUrl());
                        if(txt_branch.getText().toString()!=null)
                            reviews.put("loc",txt_branch.getText().toString());
                        if(txt_food_quality.getText().toString()!=null)
                            reviews.put("quality",txt_food_quality.getText().toString());
                        if(txt_review_service.getText().toString()!=null)
                            reviews.put("service",txt_review_service.getText().toString());
                        if(txt_review_env.getText().toString()!=null)
                            reviews.put("environment",txt_review_env.getText().toString());

                        reviews.put("rating",rating_star.getRating());
                        reviews.put("image",downloadUri);
                        db.collection("/restaurants/"+id+"/reviews")
                            .add(reviews)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.setMessage("Success!");
                            progressDialog.dismiss();
                            updateRating();
                            Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Intent back =new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(back);
                            finish();
                        }
                    })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.setMessage("Error occurred. Please try again.");
                        progressDialog.dismiss();

                        Log.w("Failure", "Error adding document", e);
                    }
                });


                    }
                }
            });

        }else{
            Map<String, Object> reviews = new HashMap<>();
            reviews.put("username",user.getDisplayName());
            reviews.put("userimg",user.getPhotoUrl());
            if(txt_branch.getText().toString()!=null)
                reviews.put("loc",txt_branch.getText().toString());
            if(txt_food_quality.getText().toString()!=null)
                reviews.put("quality",txt_food_quality.getText().toString());
            if(txt_review_service.getText().toString()!=null)
                reviews.put("service",txt_review_service.getText().toString());
            if(txt_review_env.getText().toString()!=null)
                reviews.put("environment",txt_review_env.getText().toString());

            reviews.put("rating",rating_star.getRating());
            reviews.put("image",downloadUri);
            db.collection("/restaurants/"+id+"/reviews")
                    .add(reviews)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            updateRating();
                            Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Intent back =new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(back);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            Log.w("Failure", "Error adding document", e);
                        }
                    });
        }


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
