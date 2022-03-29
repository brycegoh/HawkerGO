package com.example.hawkergo.models;

public class OpeningHours {
    public String days, hours, remarks;

    public OpeningHours(){};

    public OpeningHours(String days, String hours, String remarks){
        this.days = days;
        this.hours = hours;
        this.remarks = remarks;
    };

    public OpeningHours(String formattedOpeningDays, String formattedOpeningTime) {
        this.days = formattedOpeningDays;
        this.hours = formattedOpeningTime;
    }

}