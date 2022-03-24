package com.example.hawkergo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hawkergo.R;
import com.example.hawkergo.utils.textValidator.BaseTextValidator;
import com.example.hawkergo.utils.textValidator.TextValidatorHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddHawkerStall extends AppCompatActivity {
    ChipGroup chipGrp;
    String[] chipOptions;
    EditText nameField, floorField, unitNumField;
    TextView openingHoursErrorText;
    Button openingTimeButton, closingTimeButton, submitbutton;

    // default opening and closing time
    int openingHour = 8, openingMinute = 30;
    int closingHour = 21, closingMinute = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hawker_stall);
        this.initViews();
        this.generateChipViews();
//        this.attachEditTextListeners();
        this.attachButtonEventListeners();
    }

    private void initViews() {
        chipGrp = findViewById(R.id.chip_group);
        nameField = findViewById(R.id.name_field);
        floorField = findViewById(R.id.floor_field);
        unitNumField = findViewById(R.id.unit_num_field);
        openingTimeButton = findViewById(R.id.opening_time_button);
        closingTimeButton = findViewById(R.id.closing_time_button);
        openingHoursErrorText = findViewById(R.id.opening_hours_error);
        submitbutton = findViewById(R.id.submit_button);
        openingHoursErrorText.setText("");
        if (openingHour != 0 && openingMinute != 0) {
            openingTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", openingHour, openingMinute));
        }
        if (closingHour != 0 && closingMinute != 0) {
            closingTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", closingHour, closingMinute));
        }
    }

    private void generateChipViews() {
        chipOptions = getResources().getStringArray(R.array.chip_options);
        for (int i = 0; i < chipOptions.length; i++) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, chipGrp, false);
            chip.setText(chipOptions[i]);
            chip.setId(i);
            chip.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                            validateChips();
                        }
                    }
            );
            chipGrp.addView(chip);
        }
    }

    private void attachButtonEventListeners() {
        submitbutton.setOnClickListener(
                new View.OnClickListener(
                ) {
                    @Override
                    public void onClick(View view) {
                        onClickSubmitButton();
                    }
                }
        );
        openingTimeButton.setOnClickListener(
                new View.OnClickListener(
                ) {
                    @Override
                    public void onClick(View view) {
                        onOpeningTimeButtonClick();
                    }
                }
        );
        closingTimeButton.setOnClickListener(
                new View.OnClickListener(
                ) {
                    @Override
                    public void onClick(View view) {
                        onClosingTimeButtonClick();
                    }
                }
        );
    }

//    private void attachEditTextListeners() {
//        floorField.addTextChangedListener(
//                new BaseTextValidator(floorField) {
//                    @Override
//                    public void validate(TextView textView, String text) {
//                        validateFloorField(textView, text);
//                    }
//                }
//        );
//        unitNumField.addTextChangedListener(
//                new BaseTextValidator(unitNumField) {
//                    @Override
//                    public void validate(TextView textView, String text) {
//                        validateUnitNumField(textView, text);
//                    }
//                }
//        );
//        nameField.addTextChangedListener(
//                new BaseTextValidator(nameField) {
//                    @Override
//                    public void validate(TextView textView, String text) {
//                        validateNameField(textView, text);
//                    }
//                }
//        );
//    }

    private boolean validateFloorField() {
        boolean isValid = true;
        String text = floorField.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            floorField.setError("Please fill in the floor number");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            isValid = false;
            floorField.setError("Please fill in only numeric values");
        }
        return isValid;
    }

    private boolean validateUnitNumField() {
        boolean isValid = true;
        String text = unitNumField.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            unitNumField.setError("Please fill in the unit number");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            isValid = false;
            unitNumField.setError("Please fill in only numeric values");
        }
        return isValid;
    }

    private boolean validateNameField() {
        boolean isValid = true;
        String text = nameField.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            nameField.setError("Please fill in the name");
        }
        return isValid;
    }

    private boolean validateChips() {
        boolean isValid = true;
        List<Integer> x = chipGrp.getCheckedChipIds();
        if (x.size() > 0) {
            openingHoursErrorText.setText("");
        } else {
            isValid = false;
            openingHoursErrorText.setText("Please select at least 1 day");
        }
        return isValid;
    }

    private void onOpeningTimeButtonClick() {
        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                openingHour = selectedHour;
                openingMinute = selectedMinute;
                openingTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timePickerListener, openingHour, openingMinute, true);
        timePickerDialog.setTitle("Opening time");
        timePickerDialog.show();
    }

    private void onClosingTimeButtonClick() {
        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                closingHour = selectedHour;
                closingMinute = selectedMinute;
                closingTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timePickerListener, closingHour, closingMinute, true);
        timePickerDialog.setTitle("Closing time");
        timePickerDialog.show();
    }

    private void onClickSubmitButton() {
        Boolean[] validationArray = {
                validateChips(),
                validateFloorField(),
                validateUnitNumField(),
                validateNameField()
        };
        boolean isAllValid = !Arrays.asList(validationArray).contains(false);
    }
}