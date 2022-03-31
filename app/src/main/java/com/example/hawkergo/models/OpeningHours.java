package com.example.hawkergo.models;

public class OpeningHours {
    private String days, hours, remarks;

    public String getDays() {
        return days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setDays(String days) {
        this.days = days;
    }

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