package com.mobdeve.s17.aquino.melanie.restorate;


import static java.lang.String.valueOf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    TextInputLayout textInputLayer_name;
    TextInputLayout textInputLayout_food;
    String currentPhotoPath;
    String downloadUri;
  //  FirebaseStorage storage;
  boolean exists;

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
                   // Log.i("OUT", valueOf(imageUri));
                    if(imageUri!= null){
                        Picasso.with(AddRestoActivity.this).load(imageUri).into(img_resto);
                        btn_submitted.setVisibility(View.VISIBLE);
                    }
                    else if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            if (result.getData() != null) {
                                imageUri = result.getData().getData();
                                Picasso.with(AddRestoActivity.this).load(imageUri).into(img_resto);
                                btn_submitted.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception exception) {
                            Log.d("TAG", "" + exception.getLocalizedMessage());
                        }
                    }
                }


            });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resto);
        btn_submitted = findViewById(R.id.btn_submit);
        txt_food_type = findViewById(R.id.txt_food_quality);
        txt_restoname = findViewById(R.id.txt_branch);
        btn_choose = findViewById(R.id.btn_choose);
        btn_take = findViewById(R.id.btn_take);
        img_resto = findViewById(R.id.img_resto);
        textInputLayer_name = findViewById(R.id.textInputLayer_name);
        textInputLayout_food = findViewById(R.id.textInputLayout_food);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onItemSelectedListener);


        bottomNavigationView.setSelectedItemId(R.id.add);
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri =null;

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });

        btn_take.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                imageUri =null;
                Intent takePictureIntent = new Intent();
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();

                } catch (IOException ex) {
                    // Error occurred wh
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    imageUri = FileProvider.getUriForFile(AddRestoActivity.this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    myActivityResultLauncher.launch(Intent.createChooser(takePictureIntent, "Select Picture"));
                }
            }

            String currentPhotoPath;

            private File createImageFile() throws IOException {
                // Create an image file name

                Log.i("MEssage", "creating image");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPG_" + timeStamp + "_" + txt_restoname.getText().toString();
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = image.getAbsolutePath();
                return image;
            }

        });

    }
    public boolean allFilled(){
        if(txt_restoname.getText().toString().isEmpty()){
            textInputLayer_name.setError("Restaurant Name must be filled");
            return false;
        }if(txt_food_type.getText().toString().isEmpty()){
            textInputLayout_food.setError("Food Type must be filled");
            return false;
        }

        return true;
    }

    public void Check (View v){
        FirestoreHelper.QueryResult result = new FirestoreHelper.QueryResult();
        FirestoreHelper.queryForItem(db, "restaurants", "name", txt_restoname.getText().toString().toUpperCase(), result, new FirestoreHelper.OnQueryCompleteListener() {
            @Override
            public void onQueryComplete(boolean found) {
                // Access the result using result.isFound()
                if (result.isFound()) {
                    // Item found
                    Log.d("Firestore", "Item found");
                    textInputLayer_name.setError("Restaurant Name found");

                } else {
                    // Item not found
                    Log.d("Firestore", "Item not found");
                    submit(v);

                }

            }
        });
    }

    public void submit(View v){
        // Create a new user with a first and last

        if (allFilled()==true && imageUri!=null && exists==false){
            progressDialog = new ProgressDialog(AddRestoActivity.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = (storage.getReference());
            StorageReference RestoImagesRef = storageRef.child("resto_images/"+imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = RestoImagesRef.putBytes(data);
            uploadTask = RestoImagesRef.putFile(imageUri);

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
                        downloadUri = task.getResult().toString();
                        Map<String, Object> restaurant = new HashMap<>();
                        restaurant.put("name", txt_restoname.getText().toString().toUpperCase());
                        restaurant.put("foodtype", txt_food_type.getText().toString());
                        restaurant.put("image",downloadUri);
                        restaurant.put("rating",0); //default values
                        db.collection("restaurants")
                                .add(restaurant)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.setCanceledOnTouchOutside(true);
                                        progressDialog.setMessage("Success!");
                                        progressDialog.dismiss();
                                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
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
                        Intent back =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(back);
                        finish();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
        else if(imageUri==null) {
            // Error message when no image was selected. We need at least an image to post.
            Toast.makeText(
                    AddRestoActivity.this,
                    "Please supply an image to add restaurant",
                    Toast.LENGTH_SHORT
            ).show();
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
