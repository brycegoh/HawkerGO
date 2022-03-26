package com.example.hawkergo.utils.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DynamicEditTextManager {

    private LinearLayout favouriteFoodsContainerController;
    private HashMap<Integer,EditText> favouriteFoodsEditTextControllers = new HashMap<>();
    private int favouriteFoodsTextFieldCounter = 0;
    private Context context;

    public void init(Context context, LinearLayout favFoodContainer){
        this.context = context;
        this.favouriteFoodsContainerController = favFoodContainer;
    }

    public void addEditTextField() {
        favouriteFoodsTextFieldCounter++;

        LinearLayout container = new LinearLayout(context);
        LinearLayout.LayoutParams linearLayoutWithConstraints = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(linearLayoutWithConstraints);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setId(favouriteFoodsTextFieldCounter);

        EditText newEditTextLine = new EditText(context);
        LinearLayout.LayoutParams noWidthLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        noWidthLayout.weight = 6;
        newEditTextLine.setLayoutParams(noWidthLayout);
        newEditTextLine.setId(favouriteFoodsTextFieldCounter);
        newEditTextLine.setHint(Integer.toString(favouriteFoodsTextFieldCounter) + ". " + "Add a favourite food item");
        favouriteFoodsEditTextControllers.put(favouriteFoodsTextFieldCounter, newEditTextLine);


        noWidthLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        noWidthLayout.weight = 1;
        newEditTextLine.setLayoutParams(noWidthLayout);
        Button btn = new Button(context);
        btn.setId(favouriteFoodsTextFieldCounter);
        btn.setText("—");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeEditTextField(container, newEditTextLine);
            }
        });

        container.addView(newEditTextLine);
        container.addView(btn);
        favouriteFoodsContainerController.addView(container);
    }

    public void removeEditTextField(View viewToRemove, View editTextControllerToRemove) {
        favouriteFoodsContainerController.removeView(viewToRemove);
        int idToRemove = editTextControllerToRemove.getId();
        favouriteFoodsEditTextControllers.remove(idToRemove);
        favouriteFoodsTextFieldCounter--;
    }

    public List<String> getAllFavFoodItems(){
        ArrayList<String> list = new ArrayList<>();
        for(EditText controller : favouriteFoodsEditTextControllers.values()){
            list.add(controller.getText().toString());
        }
        System.out.println(list);
        return list;
    }
}
