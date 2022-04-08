package com.example.hawkergo.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentResultListener;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hawkergo.R;
import com.example.hawkergo.activities.baseActivities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.HawkerCentresService;
import com.example.hawkergo.services.FirebaseStorageService;
import com.example.hawkergo.utils.TextValidatorHelper;
import com.example.hawkergo.utils.Debouncer;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddHawkerCentreActivity extends AuthenticatedActivity {
    String[] openingDaysChipsOptions;

    // default opening and closing time
    Integer openingHour = 8, openingMinute = 30;
    Integer closingHour = 21, closingMinute = 30;

    Uri selectedImage;

    // view controllers
    ChipGroup openingDaysChipGrpController;
    EditText nameFieldController, streetNumberFieldController, streetNameFieldController, postalCodeFieldController;
    TextView openingHoursErrorTextController, selectCategoryErrorTextController, mainTitleController;
    Button openingTimeButtonController, closingTimeButtonController, submitButtonController;

    // chip selection tracker
    ArrayList<String> selectedOpeningDays = new ArrayList<>();

    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hawker_centre);
        super.initToolbar();
        this.initViews();
        this.inflateOpeningDaysChips();
        this.attachButtonEventListeners();
        this.addFragmentBundleListener();
    }

    private void addFragmentBundleListener() {
        getSupportFragmentManager().setFragmentResultListener("selectedImageString", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("uriString");
                Uri x = Uri.parse(result);
                selectedImage = x;
            }
        });
    }

    private void initViews() {
        openingDaysChipGrpController = findViewById(R.id.opening_days_chip_group);
//        categoriesChipGrpController = findViewById(R.id.categories_chip_group);
//        addMoreCategoryChip = findViewById(R.id.add_more_category_chip);
        mainTitleController = findViewById(R.id.hawker_centre_title);
        nameFieldController = findViewById(R.id.name_field);
        streetNumberFieldController = findViewById(R.id.street_number_field);
        streetNameFieldController = findViewById(R.id.street_name_field);
        postalCodeFieldController = findViewById(R.id.postal_code_field);
        openingTimeButtonController = findViewById(R.id.opening_time_button);
        closingTimeButtonController = findViewById(R.id.closing_time_button);
        openingHoursErrorTextController = findViewById(R.id.opening_hours_error);
        selectCategoryErrorTextController = findViewById(R.id.select_category_error);
        submitButtonController = findViewById(R.id.submit_button);
        openingHoursErrorTextController.setText("");
        if (openingHour != 0 && openingMinute != 0) {
            openingTimeButtonController.setText(String.format(Locale.getDefault(), "%02d:%02d", openingHour, openingMinute));
        }
        if (closingHour != 0 && closingMinute != 0) {
            closingTimeButtonController.setText(String.format(Locale.getDefault(), "%02d:%02d", closingHour, closingMinute));
        }
    }

    private void inflateOpeningDaysChips() {
        openingDaysChipsOptions = getResources().getStringArray(R.array.chip_options);
        for (int i = 0; i < openingDaysChipsOptions.length; i++) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, openingDaysChipGrpController, false);
            chip.setText(openingDaysChipsOptions[i]);
            chip.setId(View.generateViewId());
            openingDaysChipGrpController.addView(chip);
            addChipOnSelectListener(chip, selectedOpeningDays);
        }
    }

    private void addChipOnSelectListener(Chip chip, List<String> arr) {
        chip.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        String option = compoundButton.getText().toString();
                        if (checked) {
                            arr.add(option);
                        } else {
                            arr.remove(option);
                        }
                    }
                }
        );
    }

    private void attachButtonEventListeners() {
        submitButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isValid = validateSelectedImage() &&
                                validateStreetNumberField() &&
                                validateStreetNameField() &&
                                validatePostalCodeField() &&
                                validateOpeningHoursChips() &&
                                validateNameField();
                        if (isValid) {
                            debouncer.debounce(
                                    view,
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            onClickSubmitButton();
                                        }
                                    }
                            );
                        }
                    }
                }
        );
        openingTimeButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                openingHour = selectedHour;
                                openingMinute = selectedMinute;
                                openingTimeButtonController.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddHawkerCentreActivity.this, timePickerListener, openingHour, openingMinute, true);
                        timePickerDialog.setTitle("Opening time");
                        timePickerDialog.show();
                    }
                }
        );
        closingTimeButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                closingHour = selectedHour;
                                closingMinute = selectedMinute;
                                closingTimeButtonController.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                            }
                        };

                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddHawkerCentreActivity.this, timePickerListener, closingHour, closingMinute, true);
                        timePickerDialog.setTitle("Closing time");
                        timePickerDialog.show();
                    }
                }
        );
    }

    private boolean validateStreetNumberField() {
        boolean isValid = true;
        String text = streetNumberFieldController.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            streetNumberFieldController.setError("Please fill in the street number");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            isValid = false;
            streetNumberFieldController.setError("Please fill in only numeric values");
        }
        return isValid;
    }

    private boolean validateStreetNameField() {
        boolean isValid = true;
        String text = streetNameFieldController.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            streetNameFieldController.setError("Please fill in the street name");
        }
        return isValid;
    }

    private boolean validatePostalCodeField() {
        boolean isValid = true;
        String text = postalCodeFieldController.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            postalCodeFieldController.setError("Please fill in the postal code");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            isValid = false;
            postalCodeFieldController.setError("Please fill in only numeric values");
        }
        return isValid;
    }

    private boolean validateNameField() {
        boolean isValid = true;
        String text = nameFieldController.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            nameFieldController.setError("Please fill in the name");
        }
        return isValid;
    }

    private boolean validateOpeningHoursChips() {
        boolean isValid = true;
        List<Integer> x = openingDaysChipGrpController.getCheckedChipIds();
        if (x.size() > 0) {
            openingHoursErrorTextController.setText("");
        } else {
            isValid = false;
            openingHoursErrorTextController.setText("Please select at least 1 day");
        }
        return isValid;
    }

    private boolean validateSelectedImage() {
        if (selectedImage == null) {
            Toast.makeText(AddHawkerCentreActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
        return selectedImage != null;
    }

    private void onClickSubmitButton() {


        String centreName, formattedAddress, formattedOpeningDays, formattedOpeningTime;

        centreName = nameFieldController.getText().toString();
        formattedAddress = streetNumberFieldController.getText().toString() + " " + streetNameFieldController.getText().toString() + ", S" + postalCodeFieldController.getText().toString();
        if (selectedOpeningDays.size() == openingDaysChipsOptions.length) {
            formattedOpeningDays = "Daily";
        } else {
            String lastElement = selectedOpeningDays.get(selectedOpeningDays.size() - 1);
            StringBuilder formattedOpeningDaysBuilder = new StringBuilder("Opens every");
            for (String i : selectedOpeningDays) {
                formattedOpeningDaysBuilder.append(" ").append(i);
                if (!i.equals(lastElement)) formattedOpeningDaysBuilder.append(",");
            }
            formattedOpeningDays = formattedOpeningDaysBuilder.toString();
        }

        String formattedOpeningMinute = openingMinute.equals(0) ? "00" : Integer.toString(openingMinute);
        String formattedClosingMinute = closingMinute.equals(0) ? "00" : Integer.toString(closingMinute);

        formattedOpeningTime = Integer.toString(openingHour) + ":" + formattedOpeningMinute +
                " - " +
                Integer.toString(closingHour) + ":" + formattedClosingMinute;

        OpeningHours newOpeningHours = new OpeningHours(
                formattedOpeningDays,
                formattedOpeningTime
        );

        ArrayList<String> stallsID = new ArrayList<>();

        FirebaseStorageService.uploadImage(getContentResolver(), selectedImage, true, 40,
                new DbEventHandler<String>() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        HawkerCentre newHawkerCentre = new HawkerCentre(
                                formattedAddress,
                                centreName,
                                newOpeningHours,
                                downloadUrl,
                                stallsID
                        );
                        HawkerCentresService.addHawkerCentre(
                                newHawkerCentre,
                                new DbEventHandler<String>() {
                                    @Override
                                    public void onSuccess(String o) {
                                        Intent toHawkerCentreListingPage = new Intent(AddHawkerCentreActivity.this, HawkerCentreActivity.class);
                                        startActivity(toHawkerCentreListingPage);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(AddHawkerCentreActivity.this, "Failed to upload. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddHawkerCentreActivity.this, "Error submitting, please try again?", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        submitButtonController.setEnabled(true);
    }

}
