package com.example.hawkergo.FOR_REFERENCE;

import com.example.hawkergo.models.HawkerStall;

import java.util.List;

public interface QueryHawkerStallEventHandler {
    void onSuccess(List<HawkerStall> o);
    void onFailure(Exception e);
}
