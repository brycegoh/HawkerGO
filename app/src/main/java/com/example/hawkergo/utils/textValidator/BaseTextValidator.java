package com.example.hawkergo.utils.textValidator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Abstracts out the afterTextChanged Method from TextWatcher since form validation
 * only cares about afterTextChanged
 */

public abstract class BaseTextValidator implements TextWatcher {
    private final TextView textView;

    public BaseTextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // does not matter for validation
    }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {
        // does not matter for validation
    }
}