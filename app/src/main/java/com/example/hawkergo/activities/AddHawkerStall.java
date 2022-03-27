package com.example.hawkergo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hawkergo.R;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.repositories.HawkerCentresRepository;
import com.example.hawkergo.services.firebase.repositories.TagsRepository;
import com.example.hawkergo.utils.textValidator.TextValidatorHelper;
import com.example.hawkergo.utils.ui.DebouncedOnClickListener;
import com.example.hawkergo.utils.ui.DynamicEditTextManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddHawkerStall extends AppCompatActivity {
    private final int CAMERA_REQUEST_CODE = 1;
    private final int SELECT_PICTURE = 2;


    String[] openingDaysChipsOptions;
    List<String> categories;
    List<String> newCategories;
    HawkerCentre hawkerCentre;

    // default opening and closing time
    int openingHour = 8, openingMinute = 30;
    int closingHour = 21, closingMinute = 30;

    // view controllers
    ImageView imageViewController;
    Chip addMoreCategoryChip;
    ChipGroup openingHoursChipGrpController, categoriesChipGrpController;
    EditText nameFieldController, floorFieldController, unitNumFieldController, addMoreCategoryTextFieldController;
    TextView openingHoursErrorTextController, selectCategoryErrorTextController, mainTitleController;
    Button openingTimeButtonController, closingTimeButtonController, submitButtonController, addMoreFavFoodButtonController, addMoreCategoryButtonController;
    FloatingActionButton addPhotoButtonController;
    LinearLayout addMoreCategoryController;

    // chip selection tracker
    ArrayList<String> selectedOpeningDays = new ArrayList<>();
    ArrayList<String> selectedCategories = new ArrayList<>();

    // selected Image Uri
    Uri selectedImage;

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
        this.createActivityLaunchersForCameraAndGallery();
    }

    private void initDynamicEditTextManager(){
        dynamicEditTextManager = new DynamicEditTextManager();
        dynamicEditTextManager.init(this, findViewById(R.id.favourite_food_container));
        dynamicEditTextManager.addEditTextField();
    }

    private void createActivityLaunchersForCameraAndGallery(){
        cameraActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap image = (Bitmap) data.getExtras().get("data");
                            Uri tempUri = getImageUri(getApplicationContext(), image);
                            imageViewController.setImageURI(tempUri);
                            selectedImage = tempUri;
                        }
                    }
                });

        galleryActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri selectedImageUri = data.getData();
                            imageViewController.setImageURI(selectedImageUri);
                            selectedImage = selectedImageUri;
                        }
                    }
                });
    }

    /**
     * Bitmap to image URI through a temp storage is done with reference to
     *      https://stackoverflow.com/questions/20327213/getting-path-of-captured-image-in-android-using-camera-intent
     * */
    private Uri getImageUri(Context inContext, Bitmap bitImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitImage, Long.toString(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }


    private void handleIntent() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
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

    private void initViews() {
        imageViewController = findViewById(R.id.image_view);
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
        addPhotoButtonController = findViewById(R.id.add_photo_button);
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

    private void addChipOnSelectListener(Chip chip, List<String> arr){
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

    private void addChipToCategory(String text){
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip, categoriesChipGrpController, false);
        chip.setText(text);
        chip.setId(View.generateViewId());
        addChipOnSelectListener(chip, selectedCategories);
        categoriesChipGrpController.addView(chip);
    }


    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Integer id = menuItem.getItemId();
                        if( id.equals(R.id.gallery) ){
                            askForGalleryPermission();
                        } else if ( id.equals(R.id.camera) ){
                            askForCameraPermission();
                        }
                        return true;
                    }
                }
        );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.choose_photo_menu, popup.getMenu());
        popup.show();
    }

    private void askForCameraPermission(){
        System.out.println(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA));
        System.out.println(PackageManager.PERMISSION_GRANTED);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else{
            openCamera();
        }
    }

    private void askForGalleryPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_PICTURE);
        } else {
            openGallery();
        }
    }

    private void openGallery(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        galleryActivityLauncher.launch(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println(Arrays.toString(permissions));
        System.out.println(Arrays.toString(grantResults));
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
        }
        if(requestCode == SELECT_PICTURE){
            if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Read and Write permission is required", Toast.LENGTH_SHORT).show();
            } else if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityLauncher.launch(camera);
    }

    private void showAddCategoryErrorToast(){
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
                        if(!TextValidatorHelper.isNullOrEmpty(text) && !newCategories.contains(text) && !categories.contains(text)){
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
        addPhotoButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopup(view);
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
        System.out.println(selectedCategories.toString());
        System.out.println(newCategories.toString());
        submitButtonController.setEnabled(false);
        dynamicEditTextManager.getAllFavFoodItems();
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
            ArrayList<String> selectedCategories = new ArrayList<>();

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
                    formattedAddress, stallName, newOpeningHours, "", new ArrayList<>(), new ArrayList<>(), selectedCategories
            );

            submitButtonController.setEnabled(false);
        }else{
            submitButtonController.setEnabled(true);
        }

    }
}