package com.example.hawkergo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.R;
import com.example.hawkergo.services.FirebaseStorageService;
import com.example.hawkergo.services.UserService;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.utils.ui.Debouncer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends ToolbarActivity {

    private TextInputEditText etRegName;
    private TextInputEditText etRegEmail;
    private TextInputEditText etRegPassword;
    private TextView tvLoginHere;
    private Button btnRegister;
    private ProgressBar progressbar;
    private Uri selectedImage;
    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        super.initToolbar();
        this.initViews();
        this.addFragmentBundleListener();
        this.attachOnClickListeners();
    }

    private void initViews(){
        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);
        progressbar = findViewById(R.id.progressBar);
    }

    private void attachOnClickListeners(){
        btnRegister.setOnClickListener(view -> {
            String name = etRegName.getText().toString();
            String email = etRegEmail.getText().toString();
            String password = etRegPassword.getText().toString();
            if (TextUtils.isEmpty(email)) {
                etRegEmail.setError("Email cannot be empty");
                etRegEmail.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                etRegPassword.setError("Password cannot be empty");
                etRegPassword.requestFocus();
            } else if (TextUtils.isEmpty(name)) {
                etRegName.setError("Name cannot be empty");
                etRegName.requestFocus();
            } else if (selectedImage == null) {
                Toast.makeText(this, "Add a profile pic!", Toast.LENGTH_SHORT).show();
            } else {
                debouncer.debounce(
                        view,
                        new Runnable() {
                            @Override
                            public void run() {
                                createUser(view, email, password);
                            }
                        }
                );
            }
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });
    }

    private void addFragmentBundleListener() {
        getSupportFragmentManager().setFragmentResultListener("selectedImageString", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("uriString");
                Uri x = Uri.parse(result);
                ImageView imageView = findViewById(R.id.image_view);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                selectedImage = x;
            }
        });
    }

    private void createUser(View view, String email, String password) {
        FirebaseStorageService.uploadImage(getContentResolver(), selectedImage, true, 15, new DbEventHandler<String>() {
            @Override
            public void onSuccess(String downloadUrl) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(etRegName.getText().toString()).setPhotoUri(Uri.parse(downloadUrl)).build();
                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                UserService.createUserAndUpdateUserProfile(
                        email,
                        password,
                        profileUpdates,
                        new DbEventHandler<String>() {
                            @Override
                            public void onSuccess(String o) {
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(RegisterActivity.this, "Registration Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(
                        RegisterActivity.this,
                        e.getMessage() != null ? e.getMessage().toString() : "Error registering",
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

}