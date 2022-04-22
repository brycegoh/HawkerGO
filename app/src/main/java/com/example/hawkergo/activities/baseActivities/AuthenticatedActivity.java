package com.example.hawkergo.activities.baseActivities;

import android.content.Intent;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.activities.LoginActivity;
import com.example.hawkergo.services.UserService;
import com.google.firebase.auth.FirebaseUser;

/**
 *
 * Authenticated Activity abstracts out logic needed for screens that require authentication to access
 *
 * */

public class AuthenticatedActivity extends ToolbarActivity {
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = UserService.getAuthenticatedUser();
        // check for user, if not redirect to login screen
        if (user == null){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}





