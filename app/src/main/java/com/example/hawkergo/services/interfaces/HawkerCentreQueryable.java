package com.example.hawkergo.services.interfaces;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;

import java.util.List;
import java.util.Map;

/**
 *  Queryable interfaces are used to declare what methods are required by activities
 * */

public interface HawkerCentreQueryable {
    static void addHawkerCentre(HawkerCentre hawkerCentre, DbEventHandler<String> eventHandler ){};
    static void deleteHawkerCentre(String hawkerCentreID,DbEventHandler<String> callBack){};
    static void getHawkerCentreByID(String hawkerCentreID, DbEventHandler<HawkerCentre> eventHandler){};
    static void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, DbEventHandler<String> eventHandler){};
    static void getAllHawkerCentres(DbEventHandler<List<HawkerCentre>> eventHandler){};
}



