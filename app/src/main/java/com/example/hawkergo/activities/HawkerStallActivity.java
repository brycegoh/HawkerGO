package com.example.hawkergo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.R;
import com.example.hawkergo.activities.baseActivities.AuthenticatedActivity;
import com.example.hawkergo.fragments.FilterDialogFragment;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.HawkerStallsService;
import com.example.hawkergo.services.TagsService;
import com.example.hawkergo.services.utils.FirebaseHelper;
import com.example.hawkergo.utils.Constants;
import com.example.hawkergo.utils.RecyclerItemClickListener;
import com.example.hawkergo.adapters.HawkerStallAdapter;
import com.example.hawkergo.utils.Debouncer;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
    private final CollectionReference collectionRef =  db.collection(FirebaseHelper.CollectionIds.HAWKER_STALLS);


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM &&
                resultCode == Constants.ResultCodes.TO_HAWKER_STALL_LISTING) {
            hawkerCentreName = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME);
            hawkerCentreId = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.initViews();
        this.handleIntentExtraData();
        this.attachOnClickListeners();
        this.initViewsText();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        Intent intent = new Intent(HawkerStallActivity.this, AddHawkerStallActivity.class);
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
        Query stallColRef = collectionRef;
        updateRecyclerView(stallColRef);
    }

    @Override
    public void onClick(View view) {
        Chip chip = (Chip) view;
        filterChipGroup.removeView(chip);
        filterList.remove(chip.getText().toString());
        Query filteredColRef;
        if (filterList.size() > 0) {
            filteredColRef = collectionRef.whereArrayContainsAny("tags", filterList);
        } else {
            // If there are no filters, retrieve all hawker stalls
            filteredColRef = collectionRef.whereEqualTo("hawkerCentreId", hawkerCentreId);
        }
        updateRecyclerView(filteredColRef);
    }
    final Debouncer debouncer = new Debouncer();
    @Override
    public void finish(List<String> result) {
        filterChipGroup.removeAllViews();
        if(result != null && result.size() > 0){
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

            Query filteredColRef = collectionRef.whereArrayContainsAny("tags", result);

            updateRecyclerView(filteredColRef);
        }
    }

    private void updateRecyclerView(Query query) {
        System.out.println(query.toString());
        HawkerStallsService.filterHawkerCentre(
                query.whereEqualTo("hawkerCentreId", hawkerCentreId),
                new DbEventHandler<List<HawkerStall>>() {
                    @Override
                    public void onSuccess(List<HawkerStall> hawkerStallsFromDb) {
                        if(hawkerStallList != null && hawkerStallList.size() > 0){
                            hawkerStallList.clear();
                        }
                        hawkerStallList = hawkerStallsFromDb;
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hawker_stall_recycler_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mHawkerStallAdapter = new HawkerStallAdapter(getApplicationContext(), hawkerStallList);
                        recyclerView.setAdapter(mHawkerStallAdapter);

                        recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(getApplicationContext(), recyclerView,new RecyclerItemClickListener.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(HawkerStallActivity.this, IndividualStallActivity.class);
                                        HawkerStall currentHawkerStall = hawkerStallList.get(position);
                                        String centreId = currentHawkerStall.getHawkerCentreId();
                                        intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, centreId);
                                        intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentreName);
                                        intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, currentHawkerStall.getId());
                                        startActivityForResult(intent, Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM);
                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {
                                    }
                                })
                        );

                    }
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(HawkerStallActivity.this, "Error getting stalls", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}