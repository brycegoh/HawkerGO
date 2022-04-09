package com.example.hawkergo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.R;
import com.example.hawkergo.activities.baseActivities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.HawkerCentresService;
import com.example.hawkergo.utils.Constants;
import com.example.hawkergo.activities.helpers.RecyclerItemClickListener;
import com.example.hawkergo.adapters.HawkerCentreAdapter;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HawkerCentreActivity extends AuthenticatedActivity {
    private static final String TAG = "HawkerCentreActivity";
    private List<HawkerCentre> hawkerCentreList = new ArrayList<>();
    private HawkerCentreAdapter mHawkerCentreAdapter;
    private ImageButton filterButton;
    private ChipGroup filterChipGroup;
    private TextView header;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hawker_list);
        this.filterButton = findViewById(R.id.filter_button);
        this.header = findViewById(R.id.header);
        this.filterChipGroup = findViewById(R.id.chipGroup);
        this.floatingActionButton = findViewById(R.id.floatingActionButton);
        this.filterButton.setVisibility(View.GONE);
        this.header.setText("All Hawker Centres");
        this.filterChipGroup.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAddHawkerCentre = new Intent(HawkerCentreActivity.this, AddHawkerCentreActivity.class);
                        startActivity(toAddHawkerCentre);

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Closing App")
                .setMessage("Are you sure?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

                                        intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, centreId);
                                        intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentreName);
                                        startActivity(intent);
                                    }
                                })
                        );
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(HawkerCentreActivity.this, "Failed to get hawker centres", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}