package com.example.hawkergo.models;

public class OpeningHours {
    public String days, hours, remarks;

    OpeningHours(){};

    OpeningHours(String days, String hours, String remarks){
        this.days = days;
        this.hours = hours;
        this.remarks = remarks;
    };

    public OpeningHours(String formattedOpeningDays, String formattedOpeningTime) {
    }

}
