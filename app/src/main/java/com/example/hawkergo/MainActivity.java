package com.example.hawkergo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hawkergo.activities.AddHawkerStall;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresRepository;
import com.example.hawkergo.services.firebase.repositories.TagsRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, AddHawkerStall.class);
        intent.putExtra("id", "8Esh2FzcoCwxNJPmjdYB");
        startActivity(intent);
    }
}