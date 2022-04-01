package com.example.hawkergo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerStallsService;
import com.example.hawkergo.services.firebase.repositories.ReviewService;
import com.example.hawkergo.utils.Constants;
import com.example.hawkergo.utils.DownloadImageTask;
import com.example.hawkergo.utils.adapters.IndividualStallAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

*/

public class IndividualStallActivity extends AppCompatActivity {

    List<String> imagesURL = new ArrayList<>();

    int images[] = {R.drawable.user, R.drawable.user};
    RecyclerView recyclerView;
    TextView stallNameTV, ratingTV, locationTV, openingTV;
    ImageView stall_Image;
    Button btnAddReview;
    String hawkerStallId;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM &&
                resultCode == Constants.ResultCodes.REVIEW_SUBMISSION_TO_HAWKER_STALL) {
            hawkerStallId = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_stall);

        imagesURL.add("https://firebasestorage.googleapis.com/v0/b/hawkergo-cfe05.appspot.com/o/image%3A31?alt=media&token=d5cd8838-fa25-4131-9399-3f58a1cac6aa");
        imagesURL.add("https://firebasestorage.googleapis.com/v0/b/hawkergo-cfe05.appspot.com/o/image%3A31?alt=media&token=d5cd8838-fa25-4131-9399-3f58a1cac6aa");

        //Get ID from Hawker Stall List
        //Intent intent = getIntent();
        //hawkerStallId = intent.getStringExtra("hawkerStallId");
        //String hawkerStallId = "woaOm6sNdT11WeJDfi7N";
        //Log.d("ID Pass in", hawkerStallId);

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
                intent.putExtra("hawkerStallId", hawkerStallId);
                startActivityForResult(intent, Constants.RequestCodes.HAWKER_STALL_TO_REVIEW_SUBMISSIONS);            }
        });

        HawkerStallsService.getHawkerStallByID(
                hawkerStallId,
                new DbEventHandler<HawkerStall>() {
                    @Override
                    public void onSuccess(HawkerStall o) {

                        //Picasso.get().load(o.imageUrl).into(stall_Image);
                        //need to change to slider code
                        //image
                        //new DownloadImageTask(stall_Image).execute(o.getImageUrls());
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
                        Double sum = 0.0;
                        //sort by dates
                        Collections.sort(o,
                                (o1, o2) -> o1.getDateReviewed().compareTo(o2.getDateReviewed()));
                        for (Review review : o) {
                            sum += review.getStars();
                        }
                        Double avg = sum / o.size();
                        ratingTV.setText(avg.toString());
                        //throws the card views and all into the activity main
                        IndividualStallAdapter individualStallAdapter = new IndividualStallAdapter(getApplicationContext(), o, imagesURL);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(individualStallAdapter);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }
        );

        /* Remove comment if need slider
        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.user1));
        slideModels.add(new SlideModel(R.drawable.user1));
        slideModels.add(new SlideModel(R.drawable.user1));
        imageSlider.setImageList(slideModels,true);
        */
    }
}