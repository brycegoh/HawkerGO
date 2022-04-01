package com.example.hawkergo.services.interfaces;

import com.example.hawkergo.models.Review;

import java.util.List;

public interface ReviewQueryable {
    static void getAllReviews(String hawkerStallID, DbEventHandler<List<Review>> eventHandler){};
    static void getReviewByID(String hawkerStallID, String reviewID, DbEventHandler<Review> eventHandler){};
    static void addReview(String hawkerStallID, Review review, DbEventHandler<String> eventHandler){};
    static void deleteReview(String hawkerStallID, String reviewID, DbEventHandler<String> eventHandler){};
    static void editReview(String hawkerStallID, String reviewID, Review review, DbEventHandler<String> eventHandler){};
}
