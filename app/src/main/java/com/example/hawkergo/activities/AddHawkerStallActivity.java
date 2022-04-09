package com.example.hawkergo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentResultListener;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hawkergo.R;
import com.example.hawkergo.activities.baseActivities.AuthenticatedActivity;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.HawkerCentresService;
import com.example.hawkergo.services.FirebaseStorageService;
import com.example.hawkergo.services.TagsService;
import com.example.hawkergo.utils.Constants;
import com.example.hawkergo.utils.TextValidatorHelper;
import com.example.hawkergo.utils.Debouncer;
import com.example.hawkergo.activities.helpers.DynamicEditTextManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddHawkerStallActivity extends AuthenticatedActivity {
    private String hawkerCentreId;

    private String[] openingDaysChipsOptions;
    private List<String> categories;
    private List<String> newCategories;
    private HawkerCentre hawkerCentre;

    // default opening and closing time
    private Integer openingHour = 8, openingMinute = 30;
    private Integer closingHour = 21, closingMinute = 30;

    private Uri selectedImage;

    // view controllers
    private Chip addMoreCategoryChip;
    private ChipGroup openingHoursChipGrpController, categoriesChipGrpController;
    private EditText nameFieldController, floorFieldController, unitNumFieldController, addMoreCategoryTextFieldController;
    private TextView openingHoursErrorTextController, selectCategoryErrorTextController, mainTitleController;
    private Button openingTimeButtonController, closingTimeButtonController, submitButtonController, addMoreFavFoodButtonController, addMoreCategoryButtonController;
    private LinearLayout addMoreCategoryController;

    // chip selection tracker
    private ArrayList<String> selectedOpeningDays = new ArrayList<>();
    private ArrayList<String> selectedCategories = new ArrayList<>();

    private final Debouncer debouncer = new Debouncer();


    /**
     * Dynamic edit text
     * This manager abstracts out logic required to programmatically add EditText views into the form
     * Enables user to add as many favourite foods as they want to
     */
    private DynamicEditTextManager dynamicEditTextManager;


    /**
     * On back navigate handlers
     * <p>
     * Add extra String data:
     * 1. hawkerCentreId
     * 2. hawkerCentreName
     */
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID, hawkerCentreId);
        resultIntent.putExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME, hawkerCentre.getName());
        setResult(Constants.ResultCodes.TO_HAWKER_STALL_LISTING, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hawker_stall);
        newCategories = new ArrayList<>();
        this.initViews();
        this.handleIntent();
        this.inflateOpeningDaysChips();
        this.getAllTagsAndInflateChips();
        this.initDynamicEditTextManager();
        this.attachButtonEventListeners();
        this.addFragmentBundleListener();
    }

    private void addFragmentBundleListener() {
        getSupportFragmentManager().setFragmentResultListener("selectedImageString", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("uriString");
                Uri x = Uri.parse(result);
                ImageView imageView = findViewById(R.id.image_view);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                selectedImage = x;
            }
        });
    }


    private void initDynamicEditTextManager() {
        dynamicEditTextManager = new DynamicEditTextManager();
        dynamicEditTextManager.init(this, findViewById(R.id.favourite_food_container));
        dynamicEditTextManager.addEditTextField();
    }


    private void handleIntent() {
        Intent intent = getIntent();
        hawkerCentreId = hawkerCentreId == null ? intent.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID) : hawkerCentreId;
        if (hawkerCentreId != null) {
            HawkerCentresService.getHawkerCentreByID(hawkerCentreId, new DbEventHandler<HawkerCentre>() {
                @Override
                public void onSuccess(HawkerCentre o) {
                    hawkerCentre = o;
                    mainTitleController.setText("Adding a stall to " + hawkerCentre.getName());
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(AddHawkerStallActivity.this, "Failed to load hawker centre. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViews() {
        openingHoursChipGrpController = findViewById(R.id.opening_days_chip_group);
        categoriesChipGrpController = findViewById(R.id.categories_chip_group);
        addMoreCategoryChip = findViewById(R.id.add_more_category_chip);
        mainTitleController = findViewById(R.id.hawker_centre_title);
        nameFieldController = findViewById(R.id.name_field);
        floorFieldController = findViewById(R.id.floor_field);
        unitNumFieldController = findViewById(R.id.unit_num_field);
        openingTimeButtonController = findViewById(R.id.opening_time_button);
        closingTimeButtonController = findViewById(R.id.closing_time_button);
        openingHoursErrorTextController = findViewById(R.id.opening_hours_error);
        selectCategoryErrorTextController = findViewById(R.id.select_category_error);
        addMoreFavFoodButtonController = findViewById(R.id.add_more_button);
        submitButtonController = findViewById(R.id.submit_button);
        addMoreCategoryTextFieldController = findViewById(R.id.add_more_categories_text_field);
        addMoreCategoryButtonController = findViewById(R.id.add_more_categories_button);
        addMoreCategoryController = findViewById(R.id.add_more_categories);
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
        for (String openingDaysChipsOption : openingDaysChipsOptions) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, openingHoursChipGrpController, false);
            chip.setText(openingDaysChipsOption);
            chip.setId(View.generateViewId());
            openingHoursChipGrpController.addView(chip);
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

    private void getAllTagsAndInflateChips() {
        TagsService.getAllTags(
                new DbEventHandler<Tags>() {
                    @Override
                    public void onSuccess(Tags o) {
                        categories = o.getCategories();
                        for (int i = 0; i < categories.size(); i++) {
                            addChipToCategory(categories.get(i));
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }
        );
    }

    private void addChipToCategory(String text) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, categoriesChipGrpController, false);
        chip.setText(text);
        chip.setId(View.generateViewId());
        addChipOnSelectListener(chip, selectedCategories);
        categoriesChipGrpController.addView(chip);
    }

    private void attachButtonEventListeners() {
        addMoreCategoryChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addMoreCategoryController.setVisibility(View.VISIBLE);
                    }
                }
        );
        addMoreCategoryButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = addMoreCategoryTextFieldController.getText().toString().toLowerCase().trim();
                        if (!TextValidatorHelper.isNullOrEmpty(text) && !newCategories.contains(text) && !categories.contains(text)) {
                            debouncer.debounce(
                                    view,
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            TagsService.addTag(text, new DbEventHandler<String>() {
                                                @Override
                                                public void onSuccess(String o) {
                                                    newCategories.add(text);
                                                    addChipToCategory(text);
                                                    addMoreCategoryTextFieldController.setText(null);
                                                }

                                                @Override
                                                public void onFailure(Exception e) {
                                                    Toast.makeText(AddHawkerStallActivity.this, "Adding of category failed, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                            );

                        }
                    }
                }
        );

        addMoreFavFoodButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dynamicEditTextManager.addEditTextField();
                    }
                }
        );
        submitButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAllValid = validateOpeningHoursChips() &&
                                validateFloorField() &&
                                validateUnitNumField() &&
                                validateNameField() &&
                                validateCategoriesChips();
                        if (isAllValid) {
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
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddHawkerStallActivity.this, timePickerListener, openingHour, openingMinute, true);
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

                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddHawkerStallActivity.this, timePickerListener, closingHour, closingMinute, true);
                        timePickerDialog.setTitle("Closing time");
                        timePickerDialog.show();
                    }
                }
        );
    }

    private boolean validateFloorField() {
        System.out.println("floor 1");
        boolean isValid = true;
        System.out.println("floor 2");
        String text = floorFieldController.getText().toString();
        System.out.println("floor 3");
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            System.out.println("floor 4");
            isValid = false;
            System.out.println("floor 5");
            floorFieldController.setError("Please fill in the floor number");
            System.out.println("floor 6");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            System.out.println("floor 7");
            isValid = false;
            System.out.println("floor 8");
            floorFieldController.setError("Please fill in only numeric values");
            System.out.println("floor 9");
        }
        System.out.println("floor 10");
        return isValid;
    }

    private boolean validateUnitNumField() {
        System.out.println("num field 1");
        boolean isValid = true;
        System.out.println("num field 2");
        String text = unitNumFieldController.getText().toString();
        System.out.println("num field 3");
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            System.out.println("num field 4");
            isValid = false;
            System.out.println("num field 5");
            unitNumFieldController.setError("Please fill in the unit number");
            System.out.println("num field 6");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            System.out.println("num field 7");
            isValid = false;
            System.out.println("num field 8");
            unitNumFieldController.setError("Please fill in only numeric values");
            System.out.println("num field 9");
        }
        System.out.println("num field 10");
        return isValid;
    }

    private boolean validateNameField() {
        System.out.println("name 1");
        boolean isValid = true;
        System.out.println("name 2");
        String text = nameFieldController.getText().toString();
        System.out.println("name 3");
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            System.out.println("name 4");
            isValid = false;
            System.out.println("name 5");
            nameFieldController.setError("Please fill in the name");
            System.out.println("name 6");
        }
        System.out.println("name 7");
        return isValid;
    }

    private boolean validateOpeningHoursChips() {
        System.out.println("open chips 1");
        boolean isValid = true;
        System.out.println("open chips 2");
        List<Integer> x = openingHoursChipGrpController.getCheckedChipIds();
        System.out.println("open chips 3");
        if (x.size() <= 0) {
            System.out.println("open chips 6");
            isValid = false;
            System.out.println("open chips 7");
            openingHoursErrorTextController.setText("Please select at least 1 day");
            System.out.println("open chips 8");
        }
        System.out.println("open chips 9");
        return isValid;
    }

    private boolean validateCategoriesChips() {
        System.out.println("chips 1");
        boolean isValid = true;
        System.out.println("chips 2");
        List<Integer> x = categoriesChipGrpController.getCheckedChipIds();
        System.out.println("chips 3");
        if (x.size() <= 0) {
            System.out.println("chips 5");
            isValid = false;
            System.out.println("chips 6");
            selectCategoryErrorTextController.setText("Please select at least 1 category");
            System.out.println("chips 7");
        }
        System.out.println("chips 8");
        return isValid;
    }

    private void onClickSubmitButton() {
        System.out.println("is valid");
        // init fields needed to be saved to firestore
        String stallName, formattedAddress, formattedOpeningDays, formattedOpeningTime;
        List<String> favouriteFoods = dynamicEditTextManager.getAllFavFoodItems();

        stallName = nameFieldController.getText().toString();
        formattedAddress = "#" + floorFieldController.getText().toString() + "-" + unitNumFieldController.getText().toString();
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
                " - " + Integer.toString(closingHour) + ":" + formattedClosingMinute;

        OpeningHours newOpeningHours = new OpeningHours(
                formattedOpeningDays,
                formattedOpeningTime
        );

        FirebaseStorageService.uploadImage(getContentResolver(), selectedImage, true, 40,
                new DbEventHandler<String>() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        List<String> imageUrls = new ArrayList<>();
                        imageUrls.add(downloadUrl);
                        HawkerStall newHawkerStall = new HawkerStall(
                                formattedAddress,
                                stallName,
                                newOpeningHours,
                                imageUrls,
                                favouriteFoods,
                                selectedCategories,
                                hawkerCentreId
                        );
                        HawkerCentresService.addStallIntoHawkerCentre(
                                hawkerCentreId,
                                newHawkerStall,
                                new DbEventHandler<String>() {
                                    @Override
                                    public void onSuccess(String o) {
                                        Toast.makeText(AddHawkerStallActivity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(AddHawkerStallActivity.this, "Failed to upload. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddHawkerStallActivity.this, "Error submitting, please try again?", Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

}