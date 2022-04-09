package com.example.hawkergo.activities.baseActivities;

import android.content.Intent;

import com.example.hawkergo.MainActivity;
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





