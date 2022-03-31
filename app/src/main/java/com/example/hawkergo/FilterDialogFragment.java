package com.example.hawkergo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterDialogFragment extends DialogFragment {
    public static final String TAG = "FilterDialogFragment";
    private String[] categoriesArray;


    public FilterDialogFragment() {
        // Required empty public constructor
    }

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
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(categoriesArray[which]);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(categoriesArray[which]);
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: Implement backend filter functionality
                        MyDialogListener activity = (MyDialogListener) getActivity();
                        activity.finish(selectedItems);




                        // 1. Execute query
                        // 2. Pass result back to HawkerStallActivity.java

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });




        return builder.create();

    }


    public interface MyDialogListener {
        void finish(List<String> result);
    }


}