package com.example.hawkergo.services.firebase.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class FirebaseFilters {
    final ArrayList<String> operators = new ArrayList<String>(
            Arrays.asList(
                    "==", "!=",
                    ">", "<", ">=", "<=",
                    "contains", "contains-any",
                    "in", "not-in"
            )
    );

    FirebaseFilters(String field, String operator, String comparator){

    }
}
