package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.activities.AddHawkerStall;
import com.example.hawkergo.activities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerStallsService;
import com.example.hawkergo.services.firebase.repositories.TagsService;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.example.hawkergo.utils.Constants;
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
    private FloatingActionButton floatingActionButton;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> filterList = new ArrayList<>();
    private String hawkerCentreName;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM &&
                resultCode == Constants.ResultCodes.ADD_STALL_FORM_TO_HAWKER_STALL_LISTING) {
            hawkerCentreName = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME);
            hawkerCentreId = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);
        this.initViews();
        this.handleIntentExtraData();
        this.attachOnClickListeners();
        this.initViewsText();
        this.queryDbAndUpdateRecyclerView();
    }

    private void initViews(){
        this.filterButton = findViewById(R.id.filter_button);
        this.header = findViewById(R.id.header);
        this.floatingActionButton = findViewById(R.id.floatingActionButton);
        this.filterChipGroup = findViewById(R.id.chipGroup);
    }

    private void handleIntentExtraData(){
        Intent intent = getIntent();
        hawkerCentreId = hawkerCentreId == null ? intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID) : hawkerCentreId;
        hawkerCentreName = hawkerCentreName == null ? intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME) : hawkerCentreName;
    }

    private void attachOnClickListeners(){
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
        floatingActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HawkerStallActivity.this, AddHawkerStall.class);
                        intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, hawkerCentreId);
                        startActivityForResult(intent, Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM);
                    }
                }
        );
    }

    private void initViewsText(){
        if (hawkerCentreName != null) {
            this.header.setText(hawkerCentreName);
        }
    }

    private void queryDbAndUpdateRecyclerView(){
        Query stallColRef;
        if (hawkerCentreId != null) {
            stallColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerCentreId", hawkerCentreId);
        } else {
            stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_STALLS);
        }
        updateRecyclerView(stallColRef);
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

    private void updateRecyclerView(Query query) {
        HawkerStallsService.filterHawkerCentre(
                query,
                new DbEventHandler<List<HawkerStall>>() {
                    @Override
                    public void onSuccess(List<HawkerStall> hawkerStallsFromDb) {
                        hawkerStallList.clear();
                        hawkerStallList = hawkerStallsFromDb;
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hawker_stall_recycler_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mHawkerStallAdapter = new HawkerStallAdapter(getApplicationContext(), hawkerStallList);
                        recyclerView.setAdapter(mHawkerStallAdapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(HawkerStallActivity.this, "Error getting stalls", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


}