package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HawkerStallActivity extends AppCompatActivity implements FilterDialogFragment.MyDialogListener {
    private static final String TAG = "HawkerStallActivity";
    private List<HawkerStall> hawkerStallList = new ArrayList<>();
    private HawkerStallAdapter mHawkerStallAdapter;
    private String hawkerCentreId;
    private ImageButton filterButton;
    private TextView filterTag;
    private TextView header;
    private FilterDialogFragment filterDialog;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
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




        Query stallColRef;

        if(hawkerCentreId != null) {
            stallColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereEqualTo("hawkerCentreId", hawkerCentreId);

        } else {
            stallColRef = FirebaseRef.getCollectionReference(FirebaseConstants.CollectionIds.HAWKER_STALLS);
        }

        stallColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
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

    @Override
    public void finish(List<String> result) {
        Log.d(TAG, "finish: " + result);

        Query filteredColRef = db.collection(FirebaseConstants.CollectionIds.HAWKER_STALLS).whereArrayContainsAny("tags", result);

        // TODO: Refactor code
        // TODO: Update tags on view accordingly
        this.filterTag.setText(result.get(0));
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.hawker_list_constraint);
        ConstraintSet set = new ConstraintSet();
        List<TextView> filterViews = new ArrayList<>(Collections.singletonList(this.filterTag));



        TextView view = new TextView(this);
        view.setId(View.generateViewId());  // cannot set id after add
        view.setText(result.get(1));
        layout.addView(view, 0);
        set.clone(layout);
        filterViews.add(view);
        int n = filterViews.size();
        int marginValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());

        set.connect(filterViews.get(n-2).getId(), ConstraintSet.END, filterViews.get(n - 1).getId(), ConstraintSet.START,   marginValue);
        set.connect(filterViews.get(n-2).getId(), ConstraintSet.TOP, filterViews.get(n - 1).getId(), ConstraintSet.TOP,   0);
        set.connect(filterViews.get(n-2).getId(), ConstraintSet.BOTTOM, filterViews.get(n - 1).getId(), ConstraintSet.BOTTOM,   0);
//        for (int i = 1; i < result.size(); i++)  {
//
//
//        }
        set.applyTo(layout);

        filteredColRef.get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        hawkerStallList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            HawkerStall newHawkerStall = document.toObject(HawkerStall.class);
                            hawkerStallList.add(newHawkerStall);
                        }
                        Log.d(TAG, "onComplete: FilteredColRef " + hawkerStallList.size());

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