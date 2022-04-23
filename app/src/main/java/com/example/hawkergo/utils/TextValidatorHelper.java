package com.example.hawkergo.utils;

import android.text.TextUtils;
import androidx.core.util.PatternsCompat;

/**
 *
 * This class will define common text/form validation needed throughout the app
 *
 * */

public class TextValidatorHelper {

    public static boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

    public static boolean isValidEmail(String string){
        return PatternsCompat.EMAIL_ADDRESS.matcher(string).matches();
    }

    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string.trim());
    }
}
