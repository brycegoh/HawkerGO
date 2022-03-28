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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class ImageViewWithImageSelectorFragment extends Fragment {

    private ImageView imageViewController;
    private Uri selectedImage;
    private String currentPhotoPath;
    private ActivityResultLauncher<Intent> cameraActivityLauncher, galleryActivityLauncher;


    public ImageViewWithImageSelectorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            //TODO handle error
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cameraActivityLauncher.launch(takePictureIntent);
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        System.out.println("adding to gallery");
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        System.out.println(contentUri);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        getContext().sendBroadcast(mediaScanIntent);
    }

    private void createActivityLaunchersForCameraAndGallery() {
        cameraActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            setImageAndNotifyActivity();
                            galleryAddPic();
                        }
                    }
                });

        galleryActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            setImageAndNotifyActivity();
                            galleryAddPic();

                        }
                    }
                });
    }

    private void setImageAndNotifyActivity(){
        Uri uri = Uri.fromFile( new File(currentPhotoPath));
        imageViewController.setImageURI(uri);
        Bundle result = new Bundle();
        result.putString("uriString", uri.toString());
        requireActivity().getSupportFragmentManager().setFragmentResult("selectedImageString", result);
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