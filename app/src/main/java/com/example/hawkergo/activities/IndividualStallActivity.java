package com.example.hawkergo.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.hawkergo.R;
import com.example.hawkergo.activities.baseActivities.AuthenticatedActivity;
import com.example.hawkergo.adapters.ReviewsAdapter;
import com.example.hawkergo.adapters.SliderViewPagerAdapter;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.HawkerStallsService;
import com.example.hawkergo.services.ReviewService;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.utils.Constants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class IndividualStallActivity extends AuthenticatedActivity {

    List<String> imagesURL = new ArrayList<>();
    List<String> stallImagesURL = new ArrayList<>();
    ChipGroup tagsChipGrp;
    RecyclerView recyclerView;
    TextView stallNameTV, ratingTV, locationTV, openingTV, tagsHeader, sigFoodHeader;
    HawkerStall hawkerStall;
    Button btnAddReview;
    ViewPager sliderViewPager;
    SliderViewPagerAdapter sliderViewPagerAdapter;
    LinearLayout favFoodItems;
    private String hawkerStallId, hawkerCentreId, hawkerCentreName;

    /**
     * On back navigate handlers
     *
     * Add extra String data:
     * 1. hawkerCentreId
     * 2. hawkerCentreName
     */
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, hawkerCentreId);
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentreName);
        setResult(Constants.ResultCodes.TO_HAWKER_STALL_LISTING, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
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
        this.handleIntent();
        recyclerView = findViewById(R.id.individual_stall_recycler);
        ratingTV = findViewById(R.id.ratingTextView);
        locationTV = findViewById(R.id.locationTextView);
        openingTV = findViewById(R.id.openingHoursTextView);
        stallNameTV = findViewById(R.id.stallNameTextView);
        btnAddReview = findViewById(R.id.btnAddNewReview);
        tagsChipGrp = findViewById(R.id.tags_chip_group);
        favFoodItems = findViewById(R.id.fav_food_items);
        tagsHeader = findViewById(R.id.tags_header);
        sigFoodHeader = findViewById(R.id.sig_food_header);
        sliderViewPager = (ViewPager) findViewById(R.id.viewPagerSlider);

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualStallActivity.this, ReviewSubmissionActivity.class);
                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, hawkerStallId);
                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentreName);
                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, hawkerCentreId);
                startActivityForResult(intent, Constants.RequestCodes.HAWKER_STALL_TO_REVIEW_SUBMISSIONS);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        tagsChipGrp.removeAllViews();
        favFoodItems.removeAllViews();

    }

    @Override
        protected void onResume() {
            super.onResume();
            HawkerStallsService.getHawkerStallByID(
                    hawkerStallId,
                    new DbEventHandler<HawkerStall>() {
                        @Override
                        public void onSuccess(HawkerStall o) {
                            hawkerStall = o;
                            String formattedOpeningHours = o.getOpeningHours().getDays() + ", " + o.getOpeningHours().getHours();
                            stallNameTV.setText(o.getName());
                            locationTV.setText(o.getAddress());
                            openingTV.setText(formattedOpeningHours);
                            if (hawkerStall.getImageUrls().size() > 0) {
                                for (String url : o.getImageUrls()) {
                                    if (url != null && url.trim().length() > 0) {
                                        stallImagesURL.add(url);
                                    }
                                }
                                sliderViewPagerAdapter = new SliderViewPagerAdapter(getApplicationContext(), stallImagesURL);
                                sliderViewPager.setAdapter(sliderViewPagerAdapter);
                            }
                            if(hawkerStall.getTags() != null && hawkerStall.getTags().size() >0){
                                for(String tag : hawkerStall.getTags()){
                                    if(tag.length() > 0){
                                        Chip chip = new Chip(IndividualStallActivity.this);
                                        chip.setText(tag.substring(0,1).toUpperCase() + tag.substring(1).toLowerCase());
                                        chip.setCheckable(false);
                                        chip.setClickable(false);
                                        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(IndividualStallActivity.this, R.color.green_5)));
                                        chip.setId(View.generateViewId());
                                        tagsChipGrp.addView(chip);
                                    }
                                }
                            }else{
                                tagsHeader.setVisibility(View.GONE);
                            }
                            if(hawkerStall.getPopularItems() != null && hawkerStall.getPopularItems().size() > 0){
                                for(String item: hawkerStall.getPopularItems()){
                                    if(item.length() > 0){
                                        TextView textView = new TextView(IndividualStallActivity.this);
                                        textView.setText(item.substring(0,1).toUpperCase() + item.substring(1).toLowerCase());
                                        textView.setId(View.generateViewId());
                                        textView.setTextAppearance(IndividualStallActivity.this, R.style.header3);
                                        favFoodItems.addView(textView);
                                    }
                                }
                            }else{
                                sigFoodHeader.setVisibility(View.GONE);
                            }

                            IndividualStallActivity.this.getAllReviews();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(IndividualStallActivity.this, "Failed to get Reviews. Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
            );
        }

    private void getAllReviews(){
        ReviewService.getAllReviews(
                hawkerStallId,
                new DbEventHandler<List<Review>>() {
                    @Override
                    public void onSuccess(List<Review> o) {
                        ArrayList<Review> reviews = (ArrayList<Review>) o;
                        Double avg = hawkerStall.getAverageReview();
                        ratingTV.setText( avg != null ?  avg.toString() : "No ratings");
                        for (Review review : o) {
                            imagesURL.add(review.getProfilePicUrl());
                        }
                        //throws the card views and all into the activity main
                        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getApplicationContext(), reviews, imagesURL);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(reviewsAdapter);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(IndividualStallActivity.this, "Failed to get Reviews. Please try again", Toast.LENGTH_SHORT).show();
                    }
                        }
                );
            }
}
