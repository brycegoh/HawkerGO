package com.example.hawkergo.services.firebase.interfaces;

public interface ListenerEventHandler {
    void onInsert(Object o);
    void onUpdate(Object o);
    void onDelete(Object o);
    void onError(Object o);
}