package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
        startActivity(toHawkerCentreIntent);
        setContentView(R.layout.activity_main);

    }
}