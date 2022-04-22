package com.example.hawkergo.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.hawkergo.R;

import java.util.ArrayList;
import java.util.List;

/**
 *  This fragment defined a pop up modal that the user can select categories to filer from
 * */
public class FilterDialogFragment extends DialogFragment {
    public static final String TAG = "FilterDialogFragment";
    private String[] categoriesArray;


    public FilterDialogFragment() {}

    public FilterDialogFragment(String[] filters) {
        this.categoriesArray = filters;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        List<String> selectedItems = new ArrayList<>();  // Where we track the selected items

        // Set the dialog title
        builder.setTitle(R.string.categories)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(categoriesArray, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if(isChecked){
                                    selectedItems.add(categoriesArray[which]);
                                } else {
                                    selectedItems.remove(categoriesArray[which]);
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // pass back selectedItems
                        MyDialogListener activity = (MyDialogListener) getActivity();
                        activity.finish(selectedItems);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // no action, just cancel
                    }
                });




        return builder.create();

    }


    public interface MyDialogListener {
        void finish(List<String> result);
    }


}