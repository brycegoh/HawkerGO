package com.example.hawkergo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.AuthService;
import com.example.hawkergo.services.firebase.repositories.FirebaseStorageService;
import com.example.hawkergo.services.firebase.repositories.ReviewService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class ReviewSubmissionActivity extends AppCompatActivity {

    // Intent intent = getIntent();
    /*TODO get hawkerStallID from previous activity: HawkerStallActivity
    String hawkerStallID = intent.getStringExtra(HawkerStallActivity.KEY, null);

     */

    String hawkerStallID = "woaOm6sNdT11WeJDfi7N";

    private RatingBar ratingBar;
    private EditText editText;
    private Button submitButton;
    private double reviewStars;
    private String reviewContent;
    private Uri selectedImage;
    private String userDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_submission);

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
                String result = bundle.getString("uriString");
                Uri x = Uri.parse(result);
                ImageView imageView = findViewById(R.id.image_view);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                selectedImage = x;
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
                Uri profilePic = AuthService.getUserProfilePic();
                String image = profilePic != null ? profilePic.toString() : null;
                Review review = new Review(userDisplayName, reviewContent, reviewStars, currentDate, hawkerStallID, image);

                FirebaseStorageService.uploadImageUri(selectedImage, new DbEventHandler<String>() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        ReviewService.addReview(hawkerStallID, review, downloadUrl, new DbEventHandler<String>() {
                            @Override
                            public void onSuccess(String o) {
                                Log.i(TAG, "Successfully added review into Firestore for: " + hawkerStallID);
                                Toast.makeText(ReviewSubmissionActivity.this, R.string.reviewSubmitted, Toast.LENGTH_LONG).show();
                                Intent reviewSubmittedIntent = new Intent(ReviewSubmissionActivity.this, MainActivity.class);
                                startActivity(reviewSubmittedIntent);
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


}
