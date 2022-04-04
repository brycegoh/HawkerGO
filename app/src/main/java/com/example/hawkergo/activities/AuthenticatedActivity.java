package com.example.hawkergo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.R;
import com.example.hawkergo.services.UserService;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticatedActivity extends ToolbarActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = UserService.getAuthenticatedUser();
        if (user == null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}