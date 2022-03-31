package com.example.hawkergo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.activities.AddHawkerStall;
import com.example.hawkergo.activities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresService;
import com.example.hawkergo.utils.K;
import com.example.hawkergo.utils.RecyclerItemClickListener;
import com.example.hawkergo.utils.adapters.HawkerCentreAdapter;
import com.example.hawkergo.utils.ui.DebouncedOnClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HawkerCentreActivity extends AuthenticatedActivity {
    private static final String TAG = "HawkerCentreActivity";
    private List<HawkerCentre> hawkerCentreList = new ArrayList<>();
    private HawkerCentreAdapter mHawkerCentreAdapter;
    private ImageButton filterButton;
    private TextView filterTag;
    private TextView header;
    private FloatingActionButton floatingActionButton;
    private final String HAWKER_CENTRE_NAME = "HAWKER_CENTRE_NAME";
    private SharedPreferences mPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);

        mPreferences = getSharedPreferences(K.GLOBAL_SHARED_PREFS, MODE_PRIVATE);

        // Remove FilterButton for now
        this.filterButton = findViewById(R.id.filter_button);
        this.filterTag = findViewById(R.id.filter_tag);
        this.header = findViewById(R.id.header);
        this.floatingActionButton = findViewById(R.id.floatingActionButton);
        this.filterButton.setVisibility(View.GONE);
        this.filterTag.setVisibility(View.GONE);
        this.header.setText("All Hawker Centres");

        HawkerCentresService.getAllHawkerCentres(
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
                                        HawkerCentre currentHawkerCentre = hawkerCentreList.get(position);
                                        String centreId = currentHawkerCentre.getId();
                                        String hawkerCentreName = currentHawkerCentre.getName();

                                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                        preferencesEditor.putString(HAWKER_CENTRE_NAME, hawkerCentreName);
                                        preferencesEditor.apply();

                                        intent.putExtra("hawkerCentreId", centreId);
                                        intent.putExtra("hawkerCentreName", hawkerCentreName);
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







    }
}