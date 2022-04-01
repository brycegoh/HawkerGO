package com.example.hawkergo.services.interfaces;

public interface UploadImageEventHandler {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
}
