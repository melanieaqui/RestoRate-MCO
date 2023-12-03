package com.mobdeve.s17.aquino.melanie.restorate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadImageProfile extends AppCompatActivity {
    ImageView img_preview;
    Button btn_gallery;
    Button btn_camera;
    Button btn_skip;
    Button btn_upload;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Uri imageUri = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        img_preview = findViewById(R.id.img_preview);
        btn_gallery = findViewById(R.id.btn_gallery);
        btn_camera = findViewById(R.id.btn_camera);
        btn_skip = findViewById(R.id.btn_skip);
        btn_upload = findViewById(R.id.btn_review_upload);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri =null;

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                imageUri =null;
                Intent takePictureIntent = new Intent();

                Log.i("MEssage", "I am at takepicture");
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    Log.i("MEssage", "try");

                } catch (IOException ex) {
                    // Error occurred wh
                    Log.i("MEssage", "catch");
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    imageUri = FileProvider.getUriForFile(getApplicationContext(),
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    myActivityResultLauncher.launch(Intent.createChooser(takePictureIntent, "Select Picture"));

                }
            }


            private File createImageFile() throws IOException {
                // Create an image file name
                String currentPhotoPath;

                Log.i("MEssage", "creating image");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "PNG_" + timeStamp + "_" + user.getEmail().toString();
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".png",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = image.getAbsolutePath();
                return image;
            }

        });

        btn_skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent skip = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(skip);
                finish();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(imageUri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("UPDATED", "User profile updated.");
                                }
                            }
                        });
                Intent next= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(next);
                finish();
            }

        });

    }

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Log.i("OUT", valueOf(imageUri));
                    if(imageUri!= null){
                        Picasso.with(getApplicationContext()).load(imageUri).into(img_preview);
                        btn_upload.setVisibility(View.VISIBLE);

                    }
                    else if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            if (result.getData() != null) {
                                imageUri = result.getData().getData();
                                Picasso.with(getApplicationContext()).load(imageUri).into(img_preview);
                                btn_upload.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception exception) {
                            Log.d("TAG", "" + exception.getLocalizedMessage());
                        }
                    }
                }

            });
}
