package com.example.hawkergo.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.hawkergo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Map;


public class ImageViewWithCamera extends Fragment {

    private ImageView imageViewController;
    private Uri selectedImage;
    private ActivityResultLauncher<Intent> cameraActivityLauncher, galleryActivityLauncher;
    private OnImageSelected listener;


    public ImageViewWithCamera() {}

    public interface OnImageSelected {
        public void onSelectImage(Uri uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Listener for fragment to communicate with activity
     * Code done with reference to
     *          https://guides.codepath.com/android/Creating-and-Using-Fragments#fragment-listener
     * */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // cast to interface
        listener = (OnImageSelected) context;
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Integer id = menuItem.getItemId();
                        if (id.equals(R.id.gallery)) {
                            askForGalleryPermission();
                        } else if (id.equals(R.id.camera)) {
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

    private void askForCameraPermission() {
        askCameraPermission.launch(Manifest.permission.CAMERA);
    }

    private void askForGalleryPermission() {
        askGalleryPermission.launch(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
        );
    }

    private void openGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        galleryActivityLauncher.launch(i);
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityLauncher.launch(camera);
    }

    private void createActivityLaunchersForCameraAndGallery() {
        cameraActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap image = (Bitmap) data.getExtras().get("data");
                            Uri tempUri = getImageUri(getContext(), image);
                            imageViewController.setImageURI(tempUri);
                            listener.onSelectImage(tempUri);
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
                            listener.onSelectImage(selectedImageUri);
                        }
                    }
                });
    }

    private Uri getImageUri(Context inContext, Bitmap bitImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitImage, Long.toString(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    private ActivityResultLauncher<String> askCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        openCamera();
                    } else {
                        Toast.makeText(getContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private ActivityResultLauncher<String[]> askGalleryPermission = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    if (!result.containsValue(false)) {
                        openGallery();
                    } else {
                        Toast.makeText(getContext(), "Read and Write permission is required", Toast.LENGTH_SHORT).show();
                    }
                }

            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_view_with_camera, container, false);

        imageViewController = v.findViewById(R.id.image_view);
        FloatingActionButton addPhotoButtonController = v.findViewById(R.id.add_photo_button);
        addPhotoButtonController.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopup(view);
                    }
                }
        );
        createActivityLaunchersForCameraAndGallery();
        return v;
    }
}