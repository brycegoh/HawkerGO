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
        //Intent toHawkerCentreIntent = new Intent(MainActivity.this, HawkerCentreActivity.class);
        //startActivity(toHawkerCentreIntent);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // CollectionReference docRef = FirebaseCollectionRef.getReference(FirebaseConstants.DocumentIds.HAWKER_CENTRES);

        /**
        docRef
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
         **/

        Button submitReviewbtn = findViewById(R.id.submitReviewBtn);

        submitReviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitReviewIntent = new Intent(MainActivity.this, ReviewSubmissionActivity.class);
                // intent.putExtra(KEY,value); pass in the hawkerStallID of current stall
                startActivity(submitReviewIntent);
            }
        });


    }
}