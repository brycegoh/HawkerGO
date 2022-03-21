package com.example.hawkergo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<HawkerStall> hawkerStallList = new ArrayList<>();
    private HawkerStallAdapter mHawkerStallAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_stall_list);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_CENTRES);
        CollectionReference stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_STALLS);

        stallColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            Map<String, Object> docData = document.getData();
                            String address = (String) docData.get("address");
                            String name = (String) docData.get("name");
                            HashMap<String,String> openingHours = (HashMap<String, String>) docData.get("openingHours");
                            String hawkerCentre = (String) docData.get("hawkerCentre");

                            HawkerStall newHawkerStall = new HawkerStall(address, name, openingHours, hawkerCentre, null);
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