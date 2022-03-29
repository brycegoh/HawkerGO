package com.example.hawkergo.services.firebase.repositories;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hawkergo.MainActivity;
import com.example.hawkergo.RegisterActivity;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthRepository {
    final static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getAuthenticatedUser(){
        return mAuth.getCurrentUser();
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
                    eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
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

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
