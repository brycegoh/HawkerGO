package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresRepository;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.example.hawkergo.utils.RecyclerItemClickListener;
import com.example.hawkergo.utils.adapters.HawkerCentreAdapter;
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

public class HawkerCentreActivity extends AppCompatActivity {
    private static final String TAG = "HawkerCentreActivity";
    private List<HawkerCentre> hawkerCentreList = new ArrayList<>();
    private HawkerCentreAdapter mHawkerCentreAdapter;
    private ImageButton filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);

        // Set FilterButton

        this.filterButton = findViewById(R.id.filter_button);
        this.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a FragmentManager
                FragmentManager fm = getSupportFragmentManager();
                new FilterDialogFragment().show(fm, FilterDialogFragment.TAG);

            }
        });


        HawkerCentresRepository.getAllHawkerCentres(
                new DbEventHandler<List<HawkerCentre>>() {
                    @Override
                    public void onSuccess(List<HawkerCentre> o) {
                        hawkerCentreList = o;
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hawker_stall_recycler_view);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        mHawkerCentreAdapter = new HawkerCentreAdapter(getApplicationContext(), hawkerCentreList);
                        recyclerView.setAdapter(mHawkerCentreAdapter);


                        recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(getApplicationContext(), recyclerView,new RecyclerItemClickListener.OnItemClickListener() {


                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(HawkerCentreActivity.this, HawkerStallActivity.class);
                                        String centreId = hawkerCentreList.get(position).id;
                                        intent.putExtra("hawkerCentreId", centreId);
                                        startActivity(intent);


                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {

                                    }
                                })


                        );
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }
        );


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_CENTRES);
        CollectionReference stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_CENTRES);

//        stallColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//                        {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Log.d("TAG", document.getId() + " => " + document.getData());
//                            Map<String, Object> docData = document.getData();
//                            String id = document.getId();
//                            String address = (String) docData.get("address");
//                            String name = (String) docData.get("name");
//                            OpeningHours openingHours = (OpeningHours) docData.get("openingHours");
//                            String hawkerCentre = (String) docData.get("hawkerCentre");
//                            String imageUrl = (String) docData.get("imageUrl");
//                            List<String> stallsId = (List<String>) docData.get("stallsId");
//
//                            HawkerCentre newHawkerCentre = new HawkerCentre(id, address, name, openingHours, imageUrl, stallsId);
//                            hawkerCentreList.add(newHawkerCentre);
//                        }
//
//                        Log.d(TAG, "onComplete: " + hawkerCentreList);
//
//
//                }
//            }
//        );




    }
}