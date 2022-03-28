package com.example.hawkergo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.hawkergo.utils.adapters.IndividualStallAdapter;
/*
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
*/

public class IndividualStallActivity extends AppCompatActivity {
    String s1[], s2[],s3[];
    int images[] = {R.drawable.test_stall_four,R.drawable.test_stall_one,R.drawable.test_stall_three};
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_stall);

        /* Remove comment if need slider
        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.user1));
        slideModels.add(new SlideModel(R.drawable.user1));
        slideModels.add(new SlideModel(R.drawable.user1));
        imageSlider.setImageList(slideModels,true);
        */


        s1 = getResources().getStringArray(R.array.username);
        s2 = getResources().getStringArray(R.array.rating);
        s3 = getResources().getStringArray(R.array.reviews);

        recyclerView = findViewById(R.id.individual_stall_recycler);
        //recyclerView.setNestedScrollingEnabled(false);

        //throws the card views and all into the activity main
        IndividualStallAdapter individualStallAdapter = new IndividualStallAdapter(this, s1, s2, s3, images);
        recyclerView.setAdapter(individualStallAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}