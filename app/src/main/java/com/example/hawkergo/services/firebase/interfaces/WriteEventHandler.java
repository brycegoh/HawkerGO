package com.example.hawkergo.services.firebase.interfaces;

import com.example.hawkergo.services.firebase.utils.FirebaseConstants;

public interface WriteEventHandler {
    void onSuccess(String o);
    void onFailure(Exception e);
}
