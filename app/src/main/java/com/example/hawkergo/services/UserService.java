package com.example.hawkergo.services;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.utils.FirebaseConstants;
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

    /**
     *
     * Get user's profile pic
     *
     * @return  Uri of profile pic
     *
     * */
    public static Uri getUserProfilePic(){
        FirebaseUser user = getAuthenticatedUser();
        return user!=null ?  user.getPhotoUrl() : null;
    }

    /**
     * Logs out user
     * */
    public static void logoutUser(){
        mAuth.signOut();
    }

    public static void loginUser(String email, String password, DbEventHandler<String> eventHandler){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    System.out.println("success");
                    eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
                }else{
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }

    /**
     * Creates a new user
     *
     * @param email email string
     * @param password password string
     * @param profileUpdates profile data to be added with user
     * @param eventHandler event handler that defines onSuccess and onFailure callback funcs
     * */
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

                    eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
                }else{
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }
}
