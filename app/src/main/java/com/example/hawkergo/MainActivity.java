package com.example.hawkergo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresRepository;
import com.example.hawkergo.services.firebase.repositories.HawkerStallsRepository;
import com.example.hawkergo.services.firebase.repositories.ReviewRepository;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Review review = new Review("abc", "mr ong", "very nice", 2.0, new Date(), "\n" +
//                "auh5XN9zE5RFphfgscI8");

//        HawkerStallsRepository.addStallIntoHawkerCentre(
//                "auh5XN9zE5RFphfgscI8",
//                "abcd",
//                "test addStallIntoHawkerCentre",
//                new DbEventHandler<String>() {
//                    @Override
//                    public void onSuccess(String o) {
//                        System.out.println(o);
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                    }
//                }
//        );

//        ReviewRepository.addReview("auh5XN9zE5RFphfgscI8", o, new DbEventHandler<String>() {
//            @Override
//            public void onSuccess(String o) {
//                System.out.println("yes!");
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//            }
//        });

    }
}