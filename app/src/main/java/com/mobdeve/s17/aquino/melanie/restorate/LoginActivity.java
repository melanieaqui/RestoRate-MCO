package com.mobdeve.s17.aquino.melanie.restorate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText txt_email;
    EditText txt_pass;

    FirebaseAuth mAuth;
    TextInputLayout textInputLayout_email;
    TextInputLayout textInputLayout_pass;


    String email;
    String name;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            txt_email = (EditText) findViewById(R.id.txt_email);
            txt_pass = (EditText) findViewById(R.id.txt_pass);
            textInputLayout_email = (TextInputLayout) findViewById(R.id.textInputLayout_email);
            textInputLayout_pass = (TextInputLayout) findViewById(R.id.textInputLayout_login_pass);
            mAuth = FirebaseAuth.getInstance();



    }

    public void login_process(View v){
        email= txt_email.getText().toString();
        pass= txt_pass.getText().toString();

        if (pass.length()<8){
            txt_pass.setText("");
            textInputLayout_pass.setError("Password should be at least 8 characters");

        }
        else {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Success", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user, v);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Failed", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null, v);

                            }
                        }
                    });

        }
    }
    public void register_redirect(View v) {
        Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(reg);

    }
    private void updateUI(FirebaseUser user,View v) {
        if (user!=null){

            Intent logged = new Intent (LoginActivity.this, MainActivity.class);
            logged.putExtra("USER",user);
            startActivity(logged);
            finish();


        }
        else{
            textInputLayout_pass.setError("Error: Wrong email or password");
        }
    }
}
