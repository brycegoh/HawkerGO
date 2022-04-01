package com.example.hawkergo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
//import com.example.hawkergo.services.firebase.utils.FirebaseCollectionRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submitReviewBtn = findViewById(R.id.submitReviewBtn);

        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReviewSubmissionsIntent = new Intent(MainActivity.this, ReviewSubmissionActivity.class);
                startActivity(toReviewSubmissionsIntent);
            }
        });

        Button viewStallsBtn = findViewById(R.id.goToHawkerStallsBtn);

        viewStallsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
                startActivity(toHawkerCentreIntent);
            }
        });



    }
}