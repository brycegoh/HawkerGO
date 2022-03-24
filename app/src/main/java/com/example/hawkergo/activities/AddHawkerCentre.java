package com.example.hawkergo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hawkergo.R;
import com.example.hawkergo.utils.textValidator.BaseTextValidator;
import com.example.hawkergo.utils.textValidator.TextValidatorHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class AddHawkerCentre extends AppCompatActivity {
    ChipGroup chipGrp;
    String[] chipOptions;
    EditText nameField, addressField;
    Button openingTimeButton, closingTimeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hawker_centre);

        this.initViews();
        this.initChipViews();
        this.initEditTextListeners();

    }

    private void addButtonPressListener(){
//        TimePickerDialog.OnTimeSetListener timePickerDiag = new TimePickerDialog.OnTimeSetListener(
//
//        ) {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                timePicker.getTime
//            }
//        };

    }

    private void initViews(){
        chipGrp = findViewById(R.id.chip_group);
        nameField = findViewById(R.id.name_field);
        addressField = findViewById(R.id.address_field);
        openingTimeButton = findViewById(R.id.opening_time_button);
        closingTimeButton = findViewById(R.id.closing_time_button);
    }
    private void initChipViews(){
        chipOptions=getResources().getStringArray(R.array.chip_options);
        for(int i = 0 ; i < chipOptions.length; i++){
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, chipGrp, false);
            chip.setText(chipOptions[i]);
            chip.setId(i);

            chipGrp.addView(chip);
        }

    }

    private void initEditTextListeners(){
        addressField.addTextChangedListener(
                new BaseTextValidator(addressField) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if(TextValidatorHelper.isNullOrEmpty(text)){
                            nameField.setError("Please fill in the address");
                        }
                    }
                }
        );
        nameField.addTextChangedListener(
                new BaseTextValidator(nameField) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if(TextValidatorHelper.isNullOrEmpty(text)){
                            nameField.setError("Please fill in the name");
                        }
                    }
                }
        );
    }
}