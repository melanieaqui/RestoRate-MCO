package com.mobdeve.s17.aquino.melanie.restorate;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    EditText txt_name;
    EditText txt_email;
    EditText txt_pass;
    EditText txt_confirm_pass;
    TextView txt_error;
    FirebaseAuth mAuth;
    TextInputLayout textInputLayout_email;
    TextInputLayout textInputLayout_pass;
    TextInputLayout textInputLayout_confirm;

    String email;
    String name;
    String pass;
    String confirm;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_pass = (EditText) findViewById(R.id.txt_pass);
        txt_confirm_pass = (EditText) findViewById(R.id.txt_confirm_pass);

        txt_error =findViewById(R.id.txt_error);
        textInputLayout_email = (TextInputLayout) findViewById(R.id.textInputLayout_email);
        textInputLayout_pass = (TextInputLayout) findViewById(R.id.textInputLayout_pass);
        textInputLayout_confirm = (TextInputLayout) findViewById(R.id.textInputLayout_confirm);


    }

   public void onStart() {
      super.onStart();
      // Check if user is signed in (non-null) and update UI accordingly.
      FirebaseUser currentUser = mAuth.getCurrentUser();
      if(currentUser != null){
          Intent logged = new Intent (RegisterActivity.this, MainActivity.class);
          startActivity(logged);
      }
  }

    public void register(View v) {
        this.email= txt_email.getText().toString();
        this.pass= txt_pass.getText().toString();
        this.name= txt_name.getText().toString();
        this.confirm = txt_confirm_pass.getText().toString();
        if (pass.length()<8){
            txt_pass.setText("");
            textInputLayout_pass.setError("Password should be at least 8 characters");

        }else if(!(pass.equals(confirm))){
            textInputLayout_confirm.setError("Password not match");
            txt_confirm_pass.setText("");

        }
        else{
            mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,v);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null,v);

                        }
                    }
                });

        }
    }


    public void redirect(View v){
        Intent log = new Intent(RegisterActivity.this, UploadImageProfile.class);
        startActivity(log);
    }
    private void updateUI(FirebaseUser user, View v) {
        if (user!=null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(txt_name.getText().toString()).build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("UPDATED", "User profile updated.");
                            }
                        }
                    });
            redirect(v);

        }
       else {
            txt_name.setText("");
            txt_pass.setText("");
            txt_confirm_pass.setText("");
            txt_email.setText("");
            txt_error.setVisibility(v.VISIBLE);
            txt_error.setText("Account Already Exists");
            //textInputLayout_confirm.setError("Account Already");
        }
    }

}
