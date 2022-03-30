package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.AuthService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etRegName;
    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;
    ProgressBar progressbar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);
        progressbar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });
    }

    private void createUser(){
        String name = etRegName.getText().toString();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else if (TextUtils.isEmpty(name)){
            etRegName.setError("Name cannot be empty");
            etRegName.requestFocus();
        }else{
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(etRegName.toString()).build();
            AuthService.createUserAndUpdateUserProfile(
                    email,
                    password,
                    profileUpdates,
                    new DbEventHandler<String>() {
                        @Override
                        public void onSuccess(String o) {
                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(RegisterActivity.this, "Registration Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

}