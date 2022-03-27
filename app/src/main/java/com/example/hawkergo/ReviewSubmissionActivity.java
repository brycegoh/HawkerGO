package com.example.hawkergo;

import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewSubmissionActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText editText;
    private Button submitButton;
    public float reviewStars;
    public String reviewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_submission);

        ratingBar = (RatingBar) findViewById(R.id.reviewRatingBar);
        editText = (EditText) findViewById(R.id.edit_review) ;
        submitButton = (Button) findViewById(R.id.review_submit_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewStars = ratingBar.getRating();
                reviewContent = editText.getText().toString();
                Toast.makeText(ReviewSubmissionActivity.this, R.string.reviewSubmitted, Toast.LENGTH_LONG).show();
                // HawkerStallsRepository.addReview(user_name, reviewStars, reviewContent)
            }
        });
    }
}
