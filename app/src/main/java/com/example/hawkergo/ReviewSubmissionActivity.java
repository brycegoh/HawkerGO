package com.example.hawkergo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.ReviewRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class ReviewSubmissionActivity extends AppCompatActivity {

    Intent intent = getIntent();
    /*TODO get hawkerStallID from previous activity: HawkerStallActivity
    String hawkerStallID = intent.getStringExtra(HawkerStallActivity.KEY, null);

     */

    String hawkerStallID = "woaOm6sNdT11WeJDfi7N";

    private RatingBar ratingBar;
    private EditText editText;
    private Button submitButton;
    public double reviewStars;
    public String reviewContent;

    private FirebaseAuth mAuth;
    String userDisplayName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_submission);

        ratingBar = (RatingBar) findViewById(R.id.reviewRatingBar);
        editText = (EditText) findViewById(R.id.edit_review) ;
        submitButton = (Button) findViewById(R.id.review_submit_btn);

        // get user's name <String>
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userDisplayName = user.getDisplayName();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewStars = ratingBar.getRating();
                reviewContent = editText.getText().toString();

                // get date reviewed = current date <Date>
                Date currentDate = new Date();

                Review review = new Review(userDisplayName, reviewContent, reviewStars, currentDate, hawkerStallID);


                ReviewRepository.addReview(hawkerStallID, review, new DbEventHandler<String>() {
                    @Override
                    public void onSuccess(String o) {
                        Log.i(TAG, "Successfully added review into Firestore for: "+ hawkerStallID);
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
        });
    }
}
