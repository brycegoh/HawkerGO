package com.example.hawkergo.services;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.utils.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserService {
    final static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getAuthenticatedUser(){
        return mAuth.getCurrentUser();
    }

    public static Uri getUserProfilePic(){
        FirebaseUser user = getAuthenticatedUser();
        return user!=null ?  user.getPhotoUrl() : null;
    }

    public static void logoutUser(){
        mAuth.signOut();
    }

    public static void loginUser(String email, String password, DbEventHandler<String> eventHandler){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    System.out.println("success");
                    eventHandler.onSuccess(FirebaseHelper.DbResponse.SUCCESS);
                }else{
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }

    public static void createUserAndUpdateUserProfile(String email, String password, UserProfileChangeRequest profileUpdates, DbEventHandler<String> eventHandler){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser user = getAuthenticatedUser();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");
                                    }
                                }
                            });

                    eventHandler.onSuccess(FirebaseHelper.DbResponse.SUCCESS);
                }else{
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }
}
