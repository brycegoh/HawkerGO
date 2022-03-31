package com.example.hawkergo.services.firebase.repositories;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.interfaces.ReviewQueryable;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ReviewService implements ReviewQueryable {
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_STALLS;
    private static final CollectionReference collectionRef = FirebaseRef.getCollectionReference(collectionId);

    /**
     * Get all reviews for a hawker stall
     *
     * @param hawkerStallID ID of the hawker stall document
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void getAllReviews(String hawkerStallID, DbEventHandler<List<Review>> eventHandler) {
        DocumentReference docRef = collectionRef.document(hawkerStallID);
        CollectionReference colRef = docRef.collection("reviews");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        List<Review> reviewsList = querySnapshot.toObjects(Review.class);
                        eventHandler.onSuccess(reviewsList);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    };

    /**
     * Get a review by its ID
     *
     * @param hawkerStallID ID of the hawker stall document
     * @param reviewID      ID of the review document
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void getReviewByID(String hawkerStallID, String reviewID, DbEventHandler<Review> eventHandler){
        DocumentReference docRef = collectionRef.document(hawkerStallID);
        DocumentReference reviewRef = docRef.collection("reviews").document(reviewID);
        reviewRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        System.out.println("yes!");
                        System.out.println(document.get("name"));
                        System.out.println(document.get("stars"));
                        Review review = document.toObject(Review.class);
                        eventHandler.onSuccess(review);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    };

    /**
     * Adds review document to review collection of specific hawker stall
     * @param hawkerStallID ID of the hawker stall document where review is to be inserted
     * @param review        New review to be inserted
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void addReview(String hawkerStallID, Review review, String selectedImage, DbEventHandler<String> eventHandler){
        DocumentReference docRef = collectionRef.document(hawkerStallID);
        DocumentReference reviewRef = docRef.collection("reviews").document();
        reviewRef
                .set(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HawkerStallsService.incrementReviewAndAddPhotoCount(
                                hawkerStallID,
                                selectedImage,
                                new DbEventHandler<String>() {
                                    @Override
                                    public void onSuccess(String o) {
                                        eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        eventHandler.onFailure(e);
                                    }
                                }
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        eventHandler.onFailure(e);
                    }
                });
    };

    /**
     * Delete a review
     * @param hawkerStallID ID of the hawker stall document
     * @param reviewID      ID of review to be deleted
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void deleteReview(String hawkerStallID, String reviewID, DbEventHandler<String> eventHandler){
        DocumentReference reviewReference = collectionRef.document(hawkerStallID).collection("reviews").document(reviewID);

        reviewReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    };

    /**
     * Edit review document
     * @param hawkerStallID ID of the hawker stall document
     * @param reviewID      ID of the review document
     * @param newReview     New review to replace old review
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void editReview(String hawkerStallID, String reviewID, Review newReview, DbEventHandler<String> eventHandler){
        // TODO: check if builder pattern is necessary for update function
        DocumentReference reviewReference = collectionRef.document(hawkerStallID).collection("reviews").document(reviewID);
        reviewReference.set(newReview).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    };
}
