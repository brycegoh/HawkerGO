package com.example.hawkergo.services;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FirebaseStorageService {
    final static FirebaseStorage storage = FirebaseStorage.getInstance();
    final static StorageReference storageRef = storage.getReference();

    public static void uploadImage(ContentResolver ctxResolver, Uri uri, boolean toCompress, int compressQuality, DbEventHandler<String> eventHandler){
        UploadTask uploadTask = null;
        if(toCompress){
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(ctxResolver, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bmp != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, compressQuality, baos);
                byte[] data = baos.toByteArray();
                uploadTask = storageRef.child(uri.getLastPathSegment()).putBytes(data);
            }
        }
        if(!toCompress || uploadTask == null){
            uploadTask = storageRef.child(uri.getLastPathSegment()).putFile(uri);
        }
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
