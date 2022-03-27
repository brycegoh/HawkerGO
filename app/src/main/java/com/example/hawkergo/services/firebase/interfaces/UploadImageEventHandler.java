package com.example.hawkergo.services.firebase.interfaces;

public interface UploadImageEventHandler {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
}
