package com.example.hawkergo.services.firebase.interfaces;

public interface DbEventHandler<T> {
    void onSuccess(T o);
    void onFailure(Exception e);
}



