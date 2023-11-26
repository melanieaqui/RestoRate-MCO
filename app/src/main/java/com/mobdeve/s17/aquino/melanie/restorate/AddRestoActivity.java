package com.mobdeve.s17.aquino.melanie.restorate;


import static java.lang.String.valueOf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddRestoActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText txt_food_type;
    EditText txt_restoname;
    Button btn_submitted;
    Button btn_choose;
    Button btn_take;
    ImageView img_resto;
    private Uri imageUri = null;

    // A reference for a dialog box (for uploading progress)
    private ProgressDialog progressDialog = null;

    /* Handles the return intent from the image selector. Stores the imageUri and extracts the image
     * and inserts it into the ImageView.
     * */


    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("OUT",valueOf(result.getData().getData()));
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                imageUri = result.getData().getData();
                                Picasso.with(AddRestoActivity.this).load(imageUri).into(img_resto);
                            }
                        } catch(Exception exception){
                            Log.d("TAG",""+exception.getLocalizedMessage());
                        }
                    }
                }
               /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        img_resto.setImageBitmap(photo);
                    }
                }*/




            });
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resto);
        btn_submitted = findViewById(R.id.btn_submit);
        txt_food_type = findViewById(R.id.txt_food_type);
        txt_restoname = findViewById(R.id.txt_restoname);
        btn_choose= findViewById(R.id.btn_choose);
        btn_take = findViewById(R.id.btn_take);
        img_resto = findViewById(R.id.img_resto);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);


        bottomNavigationView.setSelectedItemId(R.id.add);
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });

        btn_take.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent();
                //cameraIntent.setType("image/*");
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
                imagesFolder.mkdirs(); // <----
                File image = new File(imagesFolder, txt_restoname+".jpg");
                Uri uriSavedImage = Uri.fromFile(image);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)   ;             //cameraIntent.putExtra("REQ_CODE",1888);
                //cameraIntent.setAction()
               // startActivityForResult(cameraIntent, 1888);
               myActivityResultLauncher.launch(cameraIntent);


                //myActivityResultLauncher.launch(Intent.createChooser(cameraIntent,1888));
            }


        });

    }







    public void submit(View v){
        // Create a new user with a first and last name
        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("name", txt_restoname.getText().toString());
        restaurant.put("food_type", txt_food_type.getText().toString());
        //user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("restaurant")
                .add(restaurant)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failure", "Error adding document", e);
                    }
                });

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
