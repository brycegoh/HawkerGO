package com.example.hawkergo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hawkergo.R;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.HawkerCentresService;
import com.example.hawkergo.utils.Constants;
import com.example.hawkergo.utils.RecyclerItemClickListener;
import com.example.hawkergo.utils.adapters.HawkerCentreAdapter;
import com.example.hawkergo.utils.ui.Debouncer;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HawkerCentreActivity extends SearchableActivity<HawkerCentre> {
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
        super.initToolbar(false);
        super.initSearchViews();
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
    protected void onResume() {
        super.onResume();
        getAllHawkerCentres();
    }

    private void getAllHawkerCentres(){
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

                                    @Override
                                    public void onLongItemClick(View view, int position) {
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

    @Override
    protected String onSearchResultItemClick(HawkerCentre selectedHawkerCentre){
        hawkerCentreList.clear();
        hawkerCentreList.add(selectedHawkerCentre);
        mHawkerCentreAdapter.notifyDataSetChanged();
        return selectedHawkerCentre.getName();
    }

    @Override
    protected void resetDataOnEmptySearchSubmit(){
            getAllHawkerCentres();
    }

    @Override
    protected void onSearchBoxTextChange(String s){
        HawkerCentresService.searchAllHawkerCentres(
                s,
                new DbEventHandler<List<HawkerCentre>>() {
                    @Override
                    public void onSuccess(List<HawkerCentre> o) {
                        HawkerCentreActivity.super.onSearchBoxTextChange(o);
                        hawkerCentreList.clear();
                        hawkerCentreList.addAll(o);
                        mHawkerCentreAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println(e.getMessage().toString());
                    }
                }
        );
    }


}