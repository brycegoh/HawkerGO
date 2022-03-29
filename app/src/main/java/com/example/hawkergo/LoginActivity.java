package com.example.hawkergo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.AuthRepository;
import com.example.hawkergo.utils.textValidator.TextValidatorHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private boolean validateEmail(){
        String email = etLoginEmail.getText().toString();
        boolean isValid = !TextValidatorHelper.isNullOrEmpty(email);
        if (!isValid){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }
        return isValid;
    }
    private boolean validatePassword(){
        String password = etLoginPassword.getText().toString();
        boolean isValid = !TextValidatorHelper.isNullOrEmpty(password);
        if (!isValid){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }
        return isValid;
    }

    private void loginUser(){
        Boolean[] validations = new Boolean[]{
                validateEmail(),
                validatePassword()
        };

        if(!Arrays.asList(validations).contains(false)){
            String email = etLoginEmail.getText().toString();
            String password = etLoginPassword.getText().toString();

            AuthRepository.loginUser(
                    email,
                    password,
                    new DbEventHandler<String>() {
                        @Override
                        public void onSuccess(String o) {
                            System.out.println("success");
                            Toast.makeText(LoginActivity.this, "MAKAN LO!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HawkerCentreActivity.class));
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(LoginActivity.this, "Log in Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        }
    }

}

