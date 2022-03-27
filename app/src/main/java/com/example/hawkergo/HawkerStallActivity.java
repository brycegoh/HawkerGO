package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.example.hawkergo.utils.adapters.HawkerStallAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HawkerStallActivity extends AppCompatActivity {
    private static final String TAG = "HawkerStallActivity";
    private List<HawkerStall> hawkerStallList = new ArrayList<>();
    private HawkerStallAdapter mHawkerStallAdapter;
    private String hawkerCentreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);

        Intent intent = getIntent();
        hawkerCentreId = intent.getStringExtra("hawkerCentreId");
        Log.d(TAG, "onCreate: hawkerCentreId is " + hawkerCentreId);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query stallColRef;

        if(hawkerCentreId != null) {
            stallColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerCentreId", hawkerCentreId);

        } else {
            stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_STALLS);
        }

        CollectionReference docRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_CENTRES);


        stallColRef.get().addOnCompleteListener(task -> {
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

                    HawkerStall newHawkerStall = new HawkerStall(id, address, name, openingHours, hawkerCentre, imageUrl,  null);
                    hawkerStallList.add(newHawkerStall);
                }

                Log.d(TAG, "onComplete: " + hawkerStallList);

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hawker_stall_recycler_view);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                mHawkerStallAdapter = new HawkerStallAdapter(getApplicationContext(), hawkerStallList);
                recyclerView.setAdapter(mHawkerStallAdapter);

            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        }
        );

//        docRef
//        .get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("TAG", document.getId() + " => " + document.getData());
//                    }
//                } else {
//                    Log.d("TAG", "Error getting documents: ", task.getException());
//                }
//            }
//        });



    }
}