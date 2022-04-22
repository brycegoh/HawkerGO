package com.example.hawkergo.activities.baseActivities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.R;
import com.example.hawkergo.services.UserService;

/**
 *
 * Toolbar Activity abstracts out logic needed for screens that have the toolbar/action bar
 *
 * */

public class ToolbarActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.actionbar_menu, menu);
        MenuItem item = menu.findItem(R.id.logout_button);
        if(UserService.getAuthenticatedUser() == null){
            invalidateOptionsMenu();
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_button){
            UserService.logoutUser();
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }
}



