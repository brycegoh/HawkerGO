package com.example.hawkergo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.activities.AddHawkerStall;
import com.example.hawkergo.activities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.TagsService;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.example.hawkergo.utils.K;
import com.example.hawkergo.utils.adapters.HawkerStallAdapter;
import com.example.hawkergo.utils.ui.DebouncedOnClickListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HawkerStallActivity extends AuthenticatedActivity implements FilterDialogFragment.MyDialogListener, View.OnClickListener {
    private static final String TAG = "HawkerStallActivity";
    private List<HawkerStall> hawkerStallList = new ArrayList<>();
    private HawkerStallAdapter mHawkerStallAdapter;
    private String hawkerCentreId;
    private ImageButton filterButton;
    private ChipGroup filterChipGroup;
    private TextView header;
    private String headerString;
    private FloatingActionButton floatingActionButton;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences mPreferences;
    private List<String> filterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);

        mPreferences = getSharedPreferences(K.GLOBAL_SHARED_PREFS, MODE_PRIVATE);
        // Ensure that header is not null when clicking back from AddHawkerStall.java
        headerString = mPreferences.getString(K.HAWKER_CENTRE_NAME, K.HAWKER_CENTRE_NAME);

        // Set FilterButton
        this.filterButton = findViewById(R.id.filter_button);
        this.header = findViewById(R.id.header);
        this.floatingActionButton = findViewById(R.id.floatingActionButton);
        this.filterChipGroup = findViewById(R.id.chipGroup);
        this.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a FragmentManager
                FragmentManager fm = getSupportFragmentManager();

                TagsService.getAllTags(new DbEventHandler<Tags>() {
                    @Override
                    public void onSuccess(Tags o) {
                        String[] categoriesArray = o.getCategoriesArray();
                        new FilterDialogFragment(categoriesArray).show(fm, FilterDialogFragment.TAG);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(HawkerStallActivity.this,
                                "Unable to retrieve filter tags. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        Intent intent = getIntent();
        hawkerCentreId = intent.getStringExtra(K.HAWKER_CENTRE_ID);
        String hawkerCentreName = intent.getStringExtra(K.HAWKER_CENTRE_NAME);

        if (hawkerCentreName != null) {
            headerString = hawkerCentreName;

        }

        this.header.setText(headerString);


        Query stallColRef;

        if (hawkerCentreId != null) {
            stallColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerCentreId", hawkerCentreId);

        } else {
            stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_STALLS);
        }

        updateRecyclerView(stallColRef);

        floatingActionButton.setOnClickListener(
                new DebouncedOnClickListener() {

                    @Override
                    public void onDebouncedClick(View view) {
                        Intent intent = new Intent(HawkerStallActivity.this, AddHawkerStall.class);
                        intent.putExtra("id", hawkerCentreId);
                        startActivity(intent);

                    }
                }
        );


    }

    @Override
    public void onClick(View view) {
        Chip chip = (Chip) view;
        filterChipGroup.removeView(chip);
        filterList.remove(chip.getText().toString());
        Query filteredColRef;
        if (filterList.size() > 0) {
            filteredColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereArrayContainsAny("tags", filterList);
        } else {
            // If there are no filters, retrieve all hawker stalls
            filteredColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerCentreId", hawkerCentreId);
        }
        updateRecyclerView(filteredColRef);

    }

    @Override
    public void finish(List<String> result) {
        Log.d(TAG, "finish: " + result);

        filterChipGroup.removeAllViews();
        for (String el : result) {
            filterList.add(el);
            Chip chip = new Chip(this);
            chip.setId(View.generateViewId());
            chip.setText(el);
            chip.setChipBackgroundColorResource(R.color.grey);
            chip.setCloseIconVisible(true);
            chip.setTextColor(getResources().getColor(R.color.black));
            chip.setOnCloseIconClickListener(this);
            filterChipGroup.addView(chip);

        }

        Query filteredColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereArrayContainsAny("tags", result);

        updateRecyclerView(filteredColRef);
    }

    private void updateRecyclerView(Query colRef) {
        Log.d(TAG, "updateRecyclerView: starts");
        colRef.get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        hawkerStallList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            HawkerStall newHawkerStall = document.toObject(HawkerStall.class);
                            hawkerStallList.add(newHawkerStall);
                        }

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