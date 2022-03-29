package com.example.hawkergo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.utils.adapters.HawkerStallAdapter;
import com.example.hawkergo.utils.adapters.IndividualStallAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
/*
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
*/

public class IndividualStallActivity extends AppCompatActivity {
    String s1[], s2[],s3[];
    int images[] = {R.drawable.test_stall_four,R.drawable.test_stall_one,R.drawable.test_stall_three};
    RecyclerView recyclerView;
    TextView stallNameTV, ratingTV, locationTV, openingTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_stall);

        recyclerView = findViewById(R.id.individual_stall_recycler);
        ratingTV = findViewById(R.id.ratingTextView);
        locationTV = findViewById(R.id.locationTextView);
        openingTV = findViewById(R.id.openingHoursTextView);
        stallNameTV = findViewById(R.id.stallNameTextView);

        //Get ID from Hawker Stall List
        //Intent intent = getIntent();
        //hawkerStallId = intent.getStringExtra("hawkerStallId");
        String hawkerStallId = "EZqqy9NYcUnVu8U2Ltxm";
        //Log.d("ID Pass in", hawkerStallId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query stallDetailsRef;

        //DocumentReference docRef = db.collection("cities").document("SF");
        stallDetailsRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerStallId", hawkerStallId);

        stallDetailsRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            Map<String, Object> docData = document.getData();
                            String id = (String) document.getId();
                            String address = (String) docData.get("address");
                            String name = (String) docData.get("name");
                            HashMap<String,String> openingHours = (HashMap<String, String>) docData.get("openingHours");
                            String hawkerCentre = (String) docData.get("hawkerCentre");
                            String imageUrl = (String) docData.get("imageUrl");
                            //HawkerStall hawkerStall = new HawkerStall(id, address, name, openingHours, hawkerCentre, imageUrl,  null);
                            stallNameTV.setText(name);
                            locationTV.setText(address);
                            openingTV.setText(openingHours.get("days"));

                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
        );

        /* Remove comment if need slider
        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.user1));
        slideModels.add(new SlideModel(R.drawable.user1));
        slideModels.add(new SlideModel(R.drawable.user1));
        imageSlider.setImageList(slideModels,true);
        */


        s1 = getResources().getStringArray(R.array.username);
        s2 = getResources().getStringArray(R.array.rating);
        s3 = getResources().getStringArray(R.array.reviews);

        //recyclerView.setNestedScrollingEnabled(false);

        //throws the card views and all into the activity main
        IndividualStallAdapter individualStallAdapter = new IndividualStallAdapter(this, s1, s2, s3, images);
        recyclerView.setAdapter(individualStallAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}