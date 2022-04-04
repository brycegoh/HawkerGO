package com.example.hawkergo.utils.textValidator;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidatorHelper {

    public static boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

    public static boolean isValidEmail(String string){
        return Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string.trim());
    }
}
