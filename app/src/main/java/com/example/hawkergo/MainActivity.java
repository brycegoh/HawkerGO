package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hawkergo.activities.AddHawkerCentre;
import com.example.hawkergo.services.firebase.repositories.AuthService;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = AuthService.getAuthenticatedUser();
        if (user != null){
//            Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
//            startActivity(toHawkerCentreIntent);

//            bryce uses this to redirect to his screen for testing
            Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
//            toHawkerCentreIntent.putExtra("id", "8Esh2FzcoCwxNJPmjdYB");
            startActivity(toHawkerCentreIntent);

        } else {
            Intent toLoginScreen = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(toLoginScreen);
        }
    }


}