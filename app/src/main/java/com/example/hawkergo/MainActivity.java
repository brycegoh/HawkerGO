package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hawkergo.activities.HawkerCentreActivity;
import com.example.hawkergo.activities.LoginActivity;
import com.example.hawkergo.services.UserService;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
        

        startActivity(toHawkerCentreIntent);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = UserService.getAuthenticatedUser();

        if (user != null){
             Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
             startActivity(toHawkerCentreIntent);
        } else {
            Intent toHawkerCentreIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(toHawkerCentreIntent);
        }
    }


}