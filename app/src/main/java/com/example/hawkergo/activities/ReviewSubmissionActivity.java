package com.example.hawkergo.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import com.example.hawkergo.R;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.FirebaseStorageService;
import com.example.hawkergo.services.ReviewService;
import com.example.hawkergo.services.UserService;
import com.example.hawkergo.services.interfaces.DbEventHandler;

import com.example.hawkergo.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class ReviewSubmissionActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText editText;
    private Button submitButton;
    private double reviewStars;
    private String reviewContent;
    private Uri selectedImage;
    private String selectedImageString;
    private String userDisplayName;
    private String hawkerStallID, hawkerCentreId, hawkerCentreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_submission);

        Intent intent = getIntent();
        hawkerStallID = intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID);
        hawkerCentreId = intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID);
        hawkerCentreName = intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME);

        ratingBar = (RatingBar) findViewById(R.id.reviewRatingBar);
        editText = (EditText) findViewById(R.id.edit_review);
        submitButton = (Button) findViewById(R.id.review_submit_btn);

        // get user's name <String>
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userDisplayName = user.getDisplayName();
        }

        addOnClickHandlers();
        addFragmentBundleListener();
    }

    private void addFragmentBundleListener(){
        getSupportFragmentManager().setFragmentResultListener("selectedImageString", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                selectedImageString = bundle.getString("uriString");
                selectedImage = Uri.parse(selectedImageString);
                ImageView imageView = findViewById(R.id.image_view);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        });
    }


    private void addOnClickHandlers(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewStars = ratingBar.getRating();
                reviewContent = editText.getText().toString();

                // get date reviewed = current date <Date>
                Date currentDate = new Date();
                Uri profilePic = UserService.getUserProfilePic();
                String profilePicUrl = profilePic != null ? profilePic.toString() : null;
                Review review = new Review(userDisplayName, reviewContent, reviewStars, currentDate, hawkerStallID, profilePicUrl, selectedImageString);

                FirebaseStorageService.uploadImageUri(selectedImage, new DbEventHandler<String>() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        ReviewService.addReview(hawkerStallID, review, downloadUrl, new DbEventHandler<String>() {
                            @Override
                            public void onSuccess(String o) {
                                Log.i(TAG, "Successfully added review into Firestore for: " + hawkerStallID);
                                Toast.makeText(ReviewSubmissionActivity.this, R.string.reviewSubmitted, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ReviewSubmissionActivity.this, IndividualStallActivity.class);
                                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, hawkerStallID);
                                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentreName);
                                intent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, hawkerCentreId);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e(TAG, "Failed to add review into Firestore for: " + hawkerStallID);
                                Toast.makeText(ReviewSubmissionActivity.this, R.string.reviewFailed, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });


            }
        });
    }

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
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_STALL_ID, hawkerStallID);
        setResult(Constants.ResultCodes.REVIEW_SUBMISSION_TO_HAWKER_STALL, resultIntent);
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


}