package com.example.hawkergo.services.interfaces;

/**
 *  DbEventHandler is used to interface activities and services
 *
 *  Generic class is used to define what the DB will return
 *
 * */

public interface DbEventHandler<T> {
    void onSuccess(T o);
    void onFailure(Exception e);
}



