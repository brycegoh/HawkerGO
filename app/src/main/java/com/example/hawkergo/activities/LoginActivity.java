package com.example.hawkergo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hawkergo.R;
import com.example.hawkergo.services.UserService;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.utils.Debouncer;
import com.example.hawkergo.utils.TextValidatorHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;

    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setOnClickListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void initViews(){
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setOnClickListeners(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean[] validations = new Boolean[]{
                        validateEmail(),
                        validatePassword()
                };
                if(!Arrays.asList(validations).contains(false)){
                    String email = etLoginEmail.getText().toString();
                    String password = etLoginPassword.getText().toString();
                    debouncer.debounce(
                            view,
                            new Runnable() {
                                @Override
                                public void run() {
                                    loginUser(email, password);
                                }
                            }
                    );
                }


            }
        });

        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private boolean validateEmail(){
        String email = etLoginEmail.getText().toString();
        boolean isValid = !TextValidatorHelper.isNullOrEmpty(email) &&
                TextValidatorHelper.isValidEmail(email);
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

    private void loginUser(String email, String password){
            UserService.loginUser(
                    email,
                    password,
                    new DbEventHandler<String>() {
                        @Override
                        public void onSuccess(String o) {
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

