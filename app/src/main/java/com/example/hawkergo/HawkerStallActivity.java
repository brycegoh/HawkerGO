package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.TagsRepository;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.example.hawkergo.utils.adapters.HawkerStallAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HawkerStallActivity extends AppCompatActivity {
    private static final String TAG = "HawkerStallActivity";
    private List<HawkerStall> hawkerStallList = new ArrayList<>();
    private HawkerStallAdapter mHawkerStallAdapter;
    private String hawkerCentreId;
    private ImageButton filterButton;
    private TextView filterTag;
    private TextView header;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);

        // Set FilterButton
        this.filterButton = findViewById(R.id.filter_button);
        this.filterTag = findViewById(R.id.filter_tag);
        this.header = findViewById(R.id.header);
        this.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a FragmentManager
                FragmentManager fm = getSupportFragmentManager();

                TagsRepository.getAllTags(new DbEventHandler<Tags>() {
                    @Override
                    public void onSuccess(Tags o) {
                        String[] categoriesArray = o.getCategoriesArray();
                        new FilterDialogFragment(categoriesArray).show(fm, FilterDialogFragment.TAG);
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                });


            }
        });

        Intent intent = getIntent();
        hawkerCentreId = intent.getStringExtra("hawkerCentreId");
        String hawkerCentreName = intent.getStringExtra("hawkerCentreName");
        this.header.setText(hawkerCentreName);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query stallColRef;

        if(hawkerCentreId != null) {
            stallColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerCentreId", hawkerCentreId);

        } else {
            stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_STALLS);
        }

        stallColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("TAG", document.getId() + " => " + document.getData());

                    HawkerStall newHawkerStall = document.toObject(HawkerStall.class);
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



    }
}