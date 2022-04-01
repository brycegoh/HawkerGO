package com.example.hawkergo.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hawkergo.R;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.HawkerStallsService;
import com.example.hawkergo.services.ReviewService;
import com.example.hawkergo.utils.Constants;
import com.example.hawkergo.utils.adapters.IndividualStallAdapter;

import java.util.ArrayList;
import java.util.List;

public class IndividualStallActivity extends AuthenticatedActivity {

    List<String> imagesURL = new ArrayList<>();

    int[] images = {R.drawable.user, R.drawable.user};
    RecyclerView recyclerView;
    TextView stallNameTV, ratingTV, locationTV, openingTV;
    ImageView stall_Image;
    Button btnAddReview;
    private String hawkerStallId, hawkerCentreId, hawkerCentreName;

    /**
     * On back navigate handlers
     *
     * Add extra String data:
     *  1. hawkerCentreId
     *  2. hawkerCentreName
     * */
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, hawkerCentreId);
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME,hawkerCentreName);
        setResult(Constants.ResultCodes.TO_HAWKER_STALL_LISTING, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM &&
                resultCode == Constants.ResultCodes.TO_HAWKER_STALL_LISTING) {
            hawkerCentreName = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME);
            hawkerCentreId = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID);
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        hawkerStallId = hawkerStallId == null ? intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID) : hawkerStallId;
        hawkerCentreId = hawkerCentreId == null ? intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID) : hawkerCentreId;
        hawkerCentreName = hawkerCentreName == null ? intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME) : hawkerCentreName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_stall);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.handleIntent();
        imagesURL.add("https://firebasestorage.googleapis.com/v0/b/hawkergo-cfe05.appspot.com/o/image%3A31?alt=media&token=d5cd8838-fa25-4131-9399-3f58a1cac6aa");

        recyclerView = findViewById(R.id.individual_stall_recycler);
        ratingTV = findViewById(R.id.ratingTextView);
        locationTV = findViewById(R.id.locationTextView);
        openingTV = findViewById(R.id.openingHoursTextView);
        stallNameTV = findViewById(R.id.stallNameTextView);
        stall_Image = findViewById(R.id.stall_picture);
        btnAddReview = findViewById(R.id.btnAddNewReview);


        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualStallActivity.this, ReviewSubmissionActivity.class);
                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, hawkerStallId);
                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentreName);
                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, hawkerCentreId);
                startActivityForResult(intent, Constants.RequestCodes.HAWKER_STALL_TO_REVIEW_SUBMISSIONS);            }
        });

        HawkerStallsService.getHawkerStallByID(
                hawkerStallId,
                new DbEventHandler<HawkerStall>() {
                    @Override
                    public void onSuccess(HawkerStall o) {
                        stallNameTV.setText(o.getName());
                        locationTV.setText(o.getAddress());
                        openingTV.setText(o.getOpeningHours().getDays() + ", " + o.getOpeningHours().getHours());
                    }
                    @Override
                    public void onFailure(Exception e) {
                    }
                }
        );

        ReviewService.getAllReviews(
                hawkerStallId,
                new DbEventHandler<List<Review>>() {
                    @Override
                    public void onSuccess(List<Review> o) {
                        ArrayList<Review> reviews = (ArrayList<Review>) o;
                        if(reviews != null){
                            Double sum = 0.0;
                            for (Review review : o) {
                                double stars = review.getStars();
                                sum += stars;
                            }
                            Double avg = sum / o.size();
                            ratingTV.setText(avg.toString());
                        }
                        //throws the card views and all into the activity main
                        IndividualStallAdapter individualStallAdapter = new IndividualStallAdapter(getApplicationContext(), reviews, imagesURL);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(individualStallAdapter);

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }
        );
    }
}