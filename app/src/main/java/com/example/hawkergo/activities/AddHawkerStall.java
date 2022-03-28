package com.example.hawkergo.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hawkergo.R;
import com.example.hawkergo.fragments.ImageViewWithImageSelectorFragment;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.interfaces.UploadImageEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresRepository;
import com.example.hawkergo.services.firebase.repositories.StorageRepository;
import com.example.hawkergo.services.firebase.repositories.TagsRepository;
import com.example.hawkergo.utils.textValidator.TextValidatorHelper;
import com.example.hawkergo.utils.ui.DebouncedOnClickListener;
import com.example.hawkergo.utils.ui.DynamicEditTextManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddHawkerStall extends AppCompatActivity implements ImageViewWithImageSelectorFragment.OnImageSelected {
    private String hawkerCentreId;


    String[] openingDaysChipsOptions;
    List<String> categories;
    List<String> newCategories;
    HawkerCentre hawkerCentre;

    // default opening and closing time
    int openingHour = 8, openingMinute = 30;
    int closingHour = 21, closingMinute = 30;

    Uri selectedImage;

    // view controllers
    ImageViewWithImageSelectorFragment imageSelectorFragment;
    Chip addMoreCategoryChip;
    ChipGroup openingHoursChipGrpController, categoriesChipGrpController;
    EditText nameFieldController, floorFieldController, unitNumFieldController, addMoreCategoryTextFieldController;
    TextView openingHoursErrorTextController, selectCategoryErrorTextController, mainTitleController;
    Button openingTimeButtonController, closingTimeButtonController, submitButtonController, addMoreFavFoodButtonController, addMoreCategoryButtonController;
    LinearLayout addMoreCategoryController;

    // chip selection tracker
    ArrayList<String> selectedOpeningDays = new ArrayList<>();
    ArrayList<String> selectedCategories = new ArrayList<>();


    /**
     * Dynamic edit text
     * This manager abstracts out logic required to programmatically add EditText views into the form
     * Enables user to add as many favourite foods as they want to
     */
    DynamicEditTextManager dynamicEditTextManager;
    ActivityResultLauncher<Intent> cameraActivityLauncher, galleryActivityLauncher;


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
        imageSelectorFragment = new ImageViewWithImageSelectorFragment();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
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
        String id = intent.getStringExtra("id");
        hawkerCentreId = id;
        if (id != null) {
            HawkerCentresRepository.getHawkerCentreByID(id, new DbEventHandler<HawkerCentre>() {
                @Override
                public void onSuccess(HawkerCentre o) {
                    hawkerCentre = o;
                    mainTitleController.setText("Adding a stall to " + hawkerCentre.name);
                }

                @Override
                public void onFailure(Exception e) {

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
        for (int i = 0; i < openingDaysChipsOptions.length; i++) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, openingHoursChipGrpController, false);
            chip.setText(openingDaysChipsOptions[i]);
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
        TagsRepository.getAllTags(
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

    private void showAddCategoryErrorToast() {
        Toast.makeText(this, "Adding of category failed, please try again", Toast.LENGTH_SHORT).show();
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
                new DebouncedOnClickListener() {
                    @Override
                    public void onDebouncedClick(View view) {
                        String text = addMoreCategoryTextFieldController.getText().toString().toLowerCase().trim();
                        if (!TextValidatorHelper.isNullOrEmpty(text) && !newCategories.contains(text) && !categories.contains(text)) {
                            TagsRepository.addTag(text, new DbEventHandler<String>() {
                                @Override
                                public void onSuccess(String o) {
                                    newCategories.add(text);
                                    addChipToCategory(text);
                                    addMoreCategoryTextFieldController.setText(null);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    showAddCategoryErrorToast();
                                }
                            });
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
                new DebouncedOnClickListener() {
                    @Override
                    public void onDebouncedClick(View view) {
                        onClickSubmitButton();
                    }
                }
        );
        openingTimeButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onOpeningTimeButtonClick();
                    }
                }
        );
        closingTimeButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClosingTimeButtonClick();
                    }
                }
        );
    }

    private boolean validateFloorField() {
        boolean isValid = true;
        String text = floorFieldController.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            floorFieldController.setError("Please fill in the floor number");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            isValid = false;
            floorFieldController.setError("Please fill in only numeric values");
        }
        return isValid;
    }

    private boolean validateUnitNumField() {
        boolean isValid = true;
        String text = unitNumFieldController.getText().toString();
        if (TextValidatorHelper.isNullOrEmpty(text)) {
            isValid = false;
            unitNumFieldController.setError("Please fill in the unit number");
        }
        if (!TextValidatorHelper.isNumeric(text)) {
            isValid = false;
            unitNumFieldController.setError("Please fill in only numeric values");
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
        List<Integer> x = openingHoursChipGrpController.getCheckedChipIds();
        if (x.size() > 0) {
            openingHoursErrorTextController.setText("");
        } else {
            isValid = false;
            openingHoursErrorTextController.setText("Please select at least 1 day");
        }
        return isValid;
    }

    private boolean validateCategoriesChips() {
        boolean isValid = true;
        List<Integer> x = categoriesChipGrpController.getCheckedChipIds();
        if (x.size() > 0) {
            selectCategoryErrorTextController.setText("");
        } else {
            isValid = false;
            selectCategoryErrorTextController.setText("Please select at least 1 category");
        }
        return isValid;
    }

    private void onOpeningTimeButtonClick() {
        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                openingHour = selectedHour;
                openingMinute = selectedMinute;
                openingTimeButtonController.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
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
                closingTimeButtonController.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timePickerListener, closingHour, closingMinute, true);
        timePickerDialog.setTitle("Closing time");
        timePickerDialog.show();
    }

    private void onClickSubmitButton() {
        submitButtonController.setEnabled(false);
        Boolean[] validationArray = {
                validateOpeningHoursChips(),
                validateFloorField(),
                validateUnitNumField(),
                validateNameField(),
                validateCategoriesChips()
        };
        dynamicEditTextManager.getAllFavFoodItems();
        boolean isAllValid = !Arrays.asList(validationArray).contains(false);

        if (isAllValid) {
            // init fields needed to be saved to firestore
            String stallName, formattedAddress, formattedOpeningDays, formattedOpeningTime;
            List<String> favouriteFoods = dynamicEditTextManager.getAllFavFoodItems();
            ;

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

            formattedOpeningTime = Integer.toString(openingHour) + ":" + Integer.toString(openingMinute) +
                    " - " +
                    Integer.toString(closingHour) + ":" + Integer.toString(closingMinute);

            OpeningHours newOpeningHours = new OpeningHours(
                    formattedOpeningDays,
                    formattedOpeningTime
            );

            HawkerStall newHawkerStall = new HawkerStall(
                    formattedAddress,
                    stallName,
                    newOpeningHours,
                    "downloadUrl",
                    favouriteFoods,
                    selectedCategories
            );


            StorageRepository.uploadImageUri(selectedImage,
                    new UploadImageEventHandler() {
                        @Override
                        public void onSuccess(String downloadUrl) {
                            // String address, String name, HashMap<String,String> openingHours, String imageUrl, List<String> reviewsIds
                            HawkerStall newHawkerStall = new HawkerStall(
                                    formattedAddress,
                                    stallName,
                                    newOpeningHours,
                                    downloadUrl,
                                    favouriteFoods,
                                    selectedCategories
                            );
                            HawkerCentresRepository.addStallIntoHawkerCentre(
                                    hawkerCentreId,
                                    newHawkerStall,
                                    new DbEventHandler<String>() {
                                        @Override
                                        public void onSuccess(String o) {
                                            System.out.println(o);
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println(e.toString());
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println(e.toString());
                        }
                    }
            );


        }

        submitButtonController.setEnabled(true);
    }

    @Override
    public void onSelectImage(Uri uri) {
        selectedImage = uri;
    }
}