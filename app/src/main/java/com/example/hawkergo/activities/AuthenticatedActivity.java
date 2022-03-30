package com.example.hawkergo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.services.firebase.repositories.AuthService;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticatedActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = AuthService.getAuthenticatedUser();
        if (user == null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}