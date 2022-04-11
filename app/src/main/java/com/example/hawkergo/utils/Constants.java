package com.example.hawkergo.utils;

public class Constants {

    public static class RequestCodes{
        public static final Integer HAWKER_STALL_LISTING_TO_ADD_STALL_FORM = 1;
        public static final Integer HAWKER_STALL_TO_REVIEW_SUBMISSIONS = 2;
    }

    public static class ResultCodes{
        public static final Integer TO_HAWKER_STALL_LISTING = 3;
        public static final Integer REVIEW_SUBMISSION_TO_HAWKER_STALL = 5;
    }

    public static class IntentExtraDataKeys{
        public static final String HAWKER_CENTRE_NAME = "HAWKER_CENTRE_NAME";
        public static final String HAWKER_CENTRE_ID = "HAWKER_CENTRE_ID";
        public static final String HAWKER_STALL_ID = "HAWKER_STALL_ID";
    }
}
