package com.example.hawkergo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.activities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresService;
import com.example.hawkergo.utils.RecyclerItemClickListener;
import com.example.hawkergo.utils.adapters.HawkerCentreAdapter;

import java.util.ArrayList;
import java.util.List;

public class HawkerCentreActivity extends AuthenticatedActivity {
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
                                        String centreId = hawkerCentreList.get(position).getId();
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






    }
}