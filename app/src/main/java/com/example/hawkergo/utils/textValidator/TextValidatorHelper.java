package com.example.hawkergo.utils.textValidator;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidatorHelper {

    public static boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

    public static boolean isValidEmail(String string){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string.trim());
    }
}
