package com.example.hawkergo.services.interfaces;

import com.example.hawkergo.models.HawkerStall;

import java.util.List;
import java.util.Map;

/**
 *  Queryable interfaces are used to declare what methods are required by activities
 * */

public interface HawkerStallQueryable {
    static void addHawkerStall(HawkerStall hawkerStall, DbEventHandler<String> eventHandler ){};
    static void deleteHawkerStall(String hawkerStallID,DbEventHandler<String> callBack){};
    static void getHawkerStallByID(String hawkerStallID, DbEventHandler<HawkerStall> eventHandler){};
    static void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, DbEventHandler<String> eventHandler){};
    static void getAllHawkerStalls(DbEventHandler<List<HawkerStall>> eventHandler){};
}




