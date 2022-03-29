package com.example.hawkergo.services.firebase.repositories;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.interfaces.UploadImageEventHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageRepository {
    final static FirebaseStorage storage = FirebaseStorage.getInstance();
    final static StorageReference storageRef = storage.getReference();

    public static void uploadImageUri(Uri uri, DbEventHandler<String> eventHandler){

        UploadTask uploadTask = storageRef.child(uri.getLastPathSegment()).putFile(uri);
        uploadTask.addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrl.addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        eventHandler.onSuccess(uri.toString());
                                    }
                                }
                        );
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        eventHandler.onFailure(e);
                    }
                }
        );
    }
}
