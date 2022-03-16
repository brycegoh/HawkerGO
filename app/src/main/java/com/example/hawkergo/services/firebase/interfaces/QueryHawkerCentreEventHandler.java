package com.example.hawkergo.services.firebase.interfaces;

import com.example.hawkergo.models.HawkerCentre;

import java.util.List;

public interface QueryHawkerCentreEventHandler {
    void onSuccess(List<HawkerCentre> o);
    void onFailure(Exception e);
}
